package com.softpunk.tgnotifier;

import com.softpunk.bot.Bot;
import com.softpunk.config.AppSettings;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import com.softpunk.localization.LocalesGetter;
import com.softpunk.service.ScheduleTransportService;
import com.softpunk.transportclient.TransportDto;
import com.softpunk.transportclient.mockimpl.TransportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class Notifier {
    private final Bot bot;
    private final LocalesGetter localesGetter;
    private final TransportClient transportClient;
    private final AppSettings appSettings;
    private final ScheduleTransportService scheduleTransportService;

    @Scheduled(fixedDelayString  = "${app.notifier.periodMs}")
    public void notifyUsers() {
        if (!appSettings.getNotifier().enable()) {
            return;
        }

        String defaultLocale = appSettings.getSession().defaultLocale();
        Instant now = Instant.now();
        List<Schedule> outOfDate = new ArrayList<>();
        List<Schedule> actual = new ArrayList<>();
        int maxIntervalMinutes = appSettings.getSchedule().maxIntervalMinutes();

        scheduleTransportService.findAllCompleteFalse().forEach(sch -> {
            if (now.isAfter(sch.getTs().plusSeconds(60L * maxIntervalMinutes))) {
                outOfDate.add(sch);
            } else {
                actual.add(sch);
            }
        });

        Map<String, List<String>> transportsByStops = new HashMap<>();

        for (Schedule schedule : actual) {
            String stopId = schedule.getTransport().getStopId();
            String transportName = schedule.getTransport().getTransportName();

            if (!transportsByStops.containsKey(stopId)) {
                transportsByStops.put(stopId, new ArrayList<>());
            }

            transportsByStops.get(stopId).add(transportName);
        }

        Map<String, List<TransportDto>> transportsWithTimeArrived = Map.of();

        if (!transportsByStops.isEmpty()) {
            transportsWithTimeArrived = transportClient.getEstimated(transportsByStops);
        } else {
            log.debug("'transportsByStops' is empty. Skip using transportClient.");
        }

        List<Schedule> undefined = new ArrayList<>();
        List<Schedule> comingSoon = new ArrayList<>();
        Map<String, Map<String, String>> comingSoonTimes = new HashMap<>();

        for (Schedule schedule : actual) {
            String stopId = schedule.getTransport().getStopId();
            String transportName = schedule.getTransport().getTransportName();
            int delay = schedule.getDelay();

            if (!transportsWithTimeArrived.containsKey(stopId)) {
                undefined.add(schedule);
                continue;
            }

            Optional<TransportDto> busOpt = transportsWithTimeArrived.get(stopId).stream()
                    .filter(b -> b.transportName().equals(transportName))
                    .findFirst();

            if (busOpt.isEmpty()) {
                undefined.add(schedule);
                continue;
            }

            TransportDto transportDto = busOpt.get();
            Instant estimated = transportDto.estimated();

            if (estimated != null && now.plusSeconds(60L * delay).isAfter(estimated)) {
                comingSoon.add(schedule);

                if (!comingSoonTimes.containsKey(stopId)) {
                    comingSoonTimes.put(stopId, new HashMap<>());
                }

                if (!comingSoonTimes.get(stopId).containsKey(transportName)) {
                    comingSoonTimes.get(stopId).put(transportName, getDelayString(estimated.getEpochSecond() - now.getEpochSecond()));
                }
            }
        }

        List<Schedule> completed = new ArrayList<>();
        List<NotifyElement> toProcess = List.of(
                new NotifyElement(outOfDate, Aliases.MSG_OUT_OF_DATE_NOTIFY, (s) -> new Object[] {
                        s.getTransport().getCommonName()
                }),
                new NotifyElement(undefined, Aliases.MSG_UNDEFINED_NOTIFY, (s) -> new Object[] {
                        s.getTransport().getCommonName()
                }),
                new NotifyElement(comingSoon, Aliases.MSG_COMING_SOON_NOTIFY, (s) -> new Object[] {
                        s.getTransport().getCommonName(),
                        comingSoonTimes.get(s.getTransport().getStopId()).get(s.getTransport().getTransportName())
                })
        );

        for (NotifyElement notifyElement : toProcess) {
            List<Schedule> list = notifyElement.list();

            for (Schedule schedule : list) {
                schedule.setComplete(true);
                completed.add(schedule);
                SendMessage sendMessage = new SendMessage(
                        String.valueOf(schedule.getUserId()),
                        localesGetter.getText(notifyElement.alias(), defaultLocale, notifyElement.argsGetter.apply(schedule))
                );
                sendMessage.enableHtml(true);
                bot.sendMessage(sendMessage);
            }
        }

        scheduleTransportService.saveAll(completed);
        randomDelay();
    }

    private String getDelayString(long seconds) {
        return seconds / 60 + " мин.";
    }

    private void randomDelay() {
        int minDelayMs = appSettings.getNotifier().minDelayMs();
        int maxDelayMs = appSettings.getNotifier().maxDelayMs();
        Random rnd = new Random();
        long delay = (long) (minDelayMs + (maxDelayMs - minDelayMs) * rnd.nextDouble());
        log.debug("Random delay for notifier thread is {} ms.", delay);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            log.warn("Thread with notifier was interrupted.");
            return;
        }

        log.debug("Delay was finished.");
    }

    private record NotifyElement(List<Schedule> list, String alias, Function<Schedule, Object[]> argsGetter) {}
}

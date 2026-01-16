package com.softpunk.assistant.service.usersession.state.schedule.commands;

import com.softpunk.bot.command.AutowiredCommand;
import com.softpunk.bot.command.FallbackCommand;
import com.softpunk.bot.command.StartCommand;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.schedule.SetScheduleIntervalState;
import com.softpunk.config.AppSettings;
import com.softpunk.dao.TransportRepository;
import com.softpunk.dao.model.Transport;
import com.softpunk.localization.Aliases;
import com.softpunk.service.ScheduleTransportService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Setter
public class SetScheduleIntervalCommand extends AutowiredCommand {
    @Autowired
    private AppSettings appSettings;

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private FallbackCommand fallbackCommand;

    @Autowired
    private ScheduleTransportService scheduleTransportService;

    @Autowired
    private TransportRepository transportRepository;

    @Override
    public boolean isAcceptable(UserSession session, Message message) {
        try {
            Integer.parseInt(message.getText());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public void doCommand(UserSession session, Message message) {
        int interval = 0;

        try {
            int input = Integer.parseInt(message.getText());

            if (input >= 1 && input <= appSettings.getSchedule().getMaxIntervalMinutes()) {
                interval = input;
            }
        } catch (Exception ignored) {}

        if (interval == 0) {
            fallbackCommand.doCommand(session, message);
            return;
        }

        SetScheduleIntervalState state = (SetScheduleIntervalState) session.getStateMachine().getCurrentState();
        scheduleTransportService.scheduleTransport(message.getChatId(), state.getScheduleTransportId(), interval);
        Transport transport = transportRepository.findById(state.getScheduleTransportId()).get();

        SendMessage sendMessage = new SendMessage(
                message.getChatId().toString(),
                localesGetter.getText(
                        Aliases.MSG_SUCCESSFUL_SCHEDULE,
                        session.getLang(),
                        transport.getCommonName(),
                        interval
                )
        );
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
        startCommand.doCommand(session, message);
    }
}

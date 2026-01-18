package com.softpunk.tgassistant.usersession.state.schedule.commands;

import com.softpunk.dao.TransportRepository;
import com.softpunk.dao.model.Transport;
import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.command.commands.AutowiredCommand;
import com.softpunk.tgassistant.command.commands.FallbackCommand;
import com.softpunk.tgassistant.command.commands.StartCommand;
import com.softpunk.tgassistant.config.AppSettings;
import com.softpunk.tgassistant.service.ScheduleTransportService;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.schedule.SetScheduleIntervalState;
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

            if (input >= 1 && input <= appSettings.getSchedule().maxIntervalMinutes()) {
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

package com.softpunk.tgassistant.usersession.state.userlist.commands;

import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.command.commands.AutowiredCommand;
import com.softpunk.tgassistant.command.commands.FallbackCommand;
import com.softpunk.tgassistant.config.AppSettings;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.userlist.ChangeScheduleIntervalState;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Setter
public class ChangeScheduleIntervalCommand extends AutowiredCommand {
    @Autowired
    private AppSettings appSettings;

    @Autowired
    private FallbackCommand fallbackCommand;

    @Autowired
    private ScheduleRepository scheduleRepository;

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

        ChangeScheduleIntervalState state = (ChangeScheduleIntervalState) session.getStateMachine().getCurrentState();
        Schedule schedule = scheduleRepository.findById(state.getId()).get();
        schedule.setDelay(interval);
        scheduleRepository.save(schedule);

        SendMessage sendMessage = new SendMessage(
                message.getChatId().toString(),
                localesGetter.getText(
                        Aliases.MSG_SUCCESSFUL_SCHEDULE,
                        session.getLang(),
                        schedule.getTransport().getCommonName(),
                        interval
                )
        );
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
        session.getStateMachine().back();
        session.getStateMachine().back();
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

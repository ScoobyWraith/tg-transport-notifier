package com.softpunk.assistant.service.usersession.state.userlist;

import com.softpunk.bot.keyboards.KbBack;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.State;
import com.softpunk.bot.usersession.state.userlist.commands.ChangeScheduleIntervalCommand;
import com.softpunk.config.AppSettings;
import com.softpunk.dao.ScheduleRepository;
import com.softpunk.localization.Aliases;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Getter
@Setter
@Component
@Scope("prototype")
public class ChangeScheduleIntervalState extends State {
    private long id;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private KbBack kbBack;

    @Autowired
    private AppSettings appSettings;

    public ChangeScheduleIntervalState() {
        registerCommands(List.of(
                ChangeScheduleIntervalCommand.class
        ));
    }

    @Override
    public void init() {
        super.init();
        id = 0;
    }

    @Override
    public void fillMessages(UserSession session) {
        int maxIntervalMinutes = appSettings.getSchedule().getMaxIntervalMinutes();
        SendMessage sendMessage = new SendMessage(
                String.valueOf(session.getChatId()),
                localesGetter.getText(Aliases.MSG_CHOOSE_SCHEDULE_INTERVAL, session.getLang(), maxIntervalMinutes)
        );
        kbBack.addKeyboard(sendMessage, session.getLang());
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }
}

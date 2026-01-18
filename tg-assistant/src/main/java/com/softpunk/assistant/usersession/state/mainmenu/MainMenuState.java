package com.softpunk.assistant.usersession.state.mainmenu;

import com.softpunk.assistant.keyboard.KbMainMenu;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.State;
import com.softpunk.assistant.usersession.state.mainmenu.callbacks.ToCreateNewScheduleCallback;
import com.softpunk.assistant.usersession.state.mainmenu.callbacks.ToUserScheduleListCallback;
import com.softpunk.dao.ScheduleRepository;
import com.softpunk.localization.Aliases;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Setter
@Component
@Scope("prototype")
public class MainMenuState extends State {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private KbMainMenu kbMainMenu;

    public MainMenuState() {
        registerCallbacks(List.of(
                ToCreateNewScheduleCallback.class,
                ToUserScheduleListCallback.class
        ));
    }

    @Override
    public void fillMessages(UserSession session) {
        long chatId = session.getChatId();
        long userScheduledListSize = scheduleRepository.countByUserIdAndCompleteFalse(session.getChatId());

        SendMessage sendMessage = new SendMessage(
                String.valueOf(chatId),
                localesGetter.getText(Aliases.MSG_WELCOME, session.getLang())
        );

        kbMainMenu.loadData(userScheduledListSize > 0);
        kbMainMenu.addKeyboard(sendMessage, session.getLang());
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }
}

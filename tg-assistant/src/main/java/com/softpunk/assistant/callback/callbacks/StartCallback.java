package com.softpunk.assistant.callback.callbacks;

import com.softpunk.assistant.callback.Callback;
import com.softpunk.assistant.callback.dto.CallbackData;
import com.softpunk.assistant.command.commands.StartCommand;
import com.softpunk.assistant.usersession.UserSession;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Slf4j
public class StartCallback extends Callback {
    @Autowired
    @Setter
    private StartCommand startCommand;

    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.TO_MAIN_MENU, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        startCommand.doCommand(session, query.getMessage());
    }
}

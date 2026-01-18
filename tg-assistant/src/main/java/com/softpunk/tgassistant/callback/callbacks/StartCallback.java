package com.softpunk.tgassistant.callback.callbacks;

import com.softpunk.tgassistant.callback.Callback;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.command.commands.StartCommand;
import com.softpunk.tgassistant.usersession.UserSession;
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

package com.softpunk.assistant.callback.callbacks;

import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.bot.command.StartCommand;
import com.softpunk.bot.usersession.UserSession;
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

package com.softpunk.assistant.callback.callbacks;

import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.bot.usersession.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class EmptyCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.EMPTY, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        // do nothing
    }
}

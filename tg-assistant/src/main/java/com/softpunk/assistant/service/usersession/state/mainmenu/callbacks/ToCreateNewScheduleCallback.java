package com.softpunk.assistant.service.usersession.state.mainmenu.callbacks;

import com.softpunk.bot.callback.Callback;
import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.schedule.AllTransportsListState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ToCreateNewScheduleCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.TO_NEW_SCHEDULE, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        session.getStateMachine().toState(AllTransportsListState.class);
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

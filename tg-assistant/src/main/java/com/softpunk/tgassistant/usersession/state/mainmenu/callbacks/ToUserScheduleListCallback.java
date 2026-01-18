package com.softpunk.tgassistant.usersession.state.mainmenu.callbacks;

import com.softpunk.tgassistant.callback.Callback;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.userlist.AllSchedulesState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ToUserScheduleListCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.TO_USER_SCHEDULE_LIST, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery callbackQuery) {
        session.getStateMachine().toState(AllSchedulesState.class);
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

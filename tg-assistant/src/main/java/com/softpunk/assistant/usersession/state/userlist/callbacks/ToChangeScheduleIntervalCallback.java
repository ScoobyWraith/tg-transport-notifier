package com.softpunk.assistant.usersession.state.userlist.callbacks;

import com.softpunk.assistant.callback.Callback;
import com.softpunk.assistant.callback.dto.CallbackData;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.userlist.ChangeScheduleIntervalState;
import com.softpunk.assistant.usersession.state.userlist.callbacks.dto.ScheduleItemCallbackData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Slf4j
public class ToChangeScheduleIntervalCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.EDIT_SCHEDULE_ITEM_INTERVAL, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        ScheduleItemCallbackData scheduleItem = getData(query.getData(), ScheduleItemCallbackData.class);
        session.getStateMachine().toState(ChangeScheduleIntervalState.class);
        ChangeScheduleIntervalState state = (ChangeScheduleIntervalState) session.getStateMachine().getCurrentState();
        state.setId(scheduleItem.getId());
        state.fillMessages(session);
    }
}

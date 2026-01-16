package com.softpunk.assistant.service.usersession.state.userlist.callbacks;

import com.softpunk.bot.callback.Callback;
import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.userlist.ScheduleItemState;
import com.softpunk.bot.usersession.state.userlist.callbacks.dto.ChooseScheduleItemCallbackData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Slf4j
public class ToScheduleItemCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.CHOOSE_SCHEDULE_ITEM, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        ChooseScheduleItemCallbackData chooseScheduleItemCallbackData
                = getData(query.getData(), ChooseScheduleItemCallbackData.class);
        session.getStateMachine().toState(ScheduleItemState.class);
        ScheduleItemState state = (ScheduleItemState) session.getStateMachine().getCurrentState();
        state.setId(chooseScheduleItemCallbackData.getId());
        state.fillMessages(session);
    }
}

package com.softpunk.tgassistant.usersession.state.schedule.callbacks;

import com.softpunk.dao.ScheduleRepository;
import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.callback.Callback;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.schedule.SetScheduleIntervalState;
import com.softpunk.tgassistant.usersession.state.schedule.callbacks.dto.ChooseTransportCallbackData;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Slf4j
public class ChooseTransportCallback extends Callback {
    @Autowired
    @Setter
    private ScheduleRepository scheduleRepository;

    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.CHOOSE_TRANSPORT, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        ChooseTransportCallbackData chooseTransportCallbackData
                = getData(query.getData(),  ChooseTransportCallbackData.class);
        long transportId = chooseTransportCallbackData.getId();
        boolean alreadyScheduled = scheduleRepository
                .countByUserIdAndTransportIdCompleteFalse(session.getChatId(), transportId) >= 1;

        if (alreadyScheduled) {
            SendMessage sendMessage = new SendMessage(
                    query.getMessage().getChatId().toString(),
                    localesGetter.getText(Aliases.MSG_ERR_REPEAT_SCHEDULE, session.getLang())
            );
            session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
            session.getStateMachine().getCurrentState().fillMessages(session);
            return;
        }

        session.getStateMachine().toState(SetScheduleIntervalState.class);
        SetScheduleIntervalState state = (SetScheduleIntervalState) session.getStateMachine().getCurrentState();
        state.setScheduleTransportId(transportId);
        state.fillMessages(session);
    }
}

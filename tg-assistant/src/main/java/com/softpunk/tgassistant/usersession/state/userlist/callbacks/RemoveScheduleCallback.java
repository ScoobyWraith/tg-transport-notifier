package com.softpunk.tgassistant.usersession.state.userlist.callbacks;

import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.callback.Callback;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.userlist.callbacks.dto.ScheduleItemCallbackData;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Setter
@Component
@Slf4j
public class RemoveScheduleCallback extends Callback {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.REMOVE_ITEM, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        ScheduleItemCallbackData scheduleItem = getData(query.getData(), ScheduleItemCallbackData.class);
        Schedule schedule = scheduleRepository.findById(scheduleItem.getId()).get();
        String name = schedule.getTransport().getCommonName();
        int interval = schedule.getDelay();
        String text = localesGetter.getText(Aliases.ITM_SCHEDULED, session.getLang(), name, interval);
        scheduleRepository.deleteById(scheduleItem.getId());

        SendMessage sendMessage = new SendMessage(
                query.getMessage().getChatId().toString(),
                localesGetter.getText(Aliases.MSG_SUCCESSFUL_REMOVE, session.getLang(), text)
        );
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
        session.getStateMachine().back();
        session.getStateMachine().back();
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

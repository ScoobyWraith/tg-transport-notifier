package com.softpunk.assistant.usersession.state.userlist;

import com.softpunk.assistant.keyboard.KbScheduleItem;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.State;
import com.softpunk.assistant.usersession.state.userlist.callbacks.RemoveScheduleCallback;
import com.softpunk.assistant.usersession.state.userlist.callbacks.ToChangeScheduleIntervalCallback;
import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Setter
@Component
@Scope("prototype")
public class ScheduleItemState extends State {
    private long id;

    @Autowired
    @Setter
    private KbScheduleItem kbScheduleItem;

    @Autowired
    @Setter
    private ScheduleRepository scheduleRepository;

    public ScheduleItemState() {
        registerCallbacks(List.of(
                RemoveScheduleCallback.class,
                ToChangeScheduleIntervalCallback.class
        ));
    }

    @Override
    public void init() {
        super.init();
        id = 0;
    }

    @Override
    public void fillMessages(UserSession session) {
        Schedule schedule = scheduleRepository.findById(id).get();
        String name = schedule.getTransport().getCommonName();
        int interval = schedule.getDelay();
        String text = localesGetter.getText(Aliases.ITM_SCHEDULED, session.getLang(), name, interval);

        SendMessage sendMessage = new SendMessage(
                String.valueOf(session.getChatId()),
                text
        );
        kbScheduleItem.loadData(id);
        kbScheduleItem.addKeyboard(sendMessage, session.getLang());
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }
}

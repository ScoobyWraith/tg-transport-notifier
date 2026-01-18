package com.softpunk.assistant.command;

import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.State;
import com.softpunk.localization.LocalesGetter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class Command {
    @Autowired @Setter
    protected LocalesGetter localesGetter;

    public abstract void doCommand(UserSession session, Message message);

    public boolean isAcceptable(UserSession session, Message message) {
        return true;
    }

    protected boolean isState(Class<? extends State> state, UserSession session) {
        return session.getStateMachine().getCurrentState().getClass().equals(state);
    }

    protected boolean isCommand(String command, Message message) {
        return message.getText().startsWith(command);
    }

    protected boolean isCommand(Message message) {
        return message.getText().startsWith("/");
    }
}

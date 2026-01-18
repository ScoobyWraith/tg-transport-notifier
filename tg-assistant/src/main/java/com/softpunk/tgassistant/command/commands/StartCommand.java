package com.softpunk.tgassistant.command.commands;

import com.softpunk.tgassistant.constants.Commands;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.mainmenu.MainMenuState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartCommand extends AutowiredCommand {

    @Override
    public boolean isAcceptable(UserSession session, Message message) {
        return isCommand(Commands.START, message);
    }

    @Override
    public void doCommand(UserSession session, Message message) {
        session.getStateMachine().toState(MainMenuState.class);
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

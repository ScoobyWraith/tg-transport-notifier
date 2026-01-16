package com.softpunk.assistant.command.commands;

import com.softpunk.bot.Commands;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.mainmenu.MainMenuState;
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
        session.getMessagesDispatcher().prepareMessageToRemoveAll();
        session.getStateMachine().toState(MainMenuState.class);
        session.getStateMachine().getCurrentState().fillMessages(session);
    }
}

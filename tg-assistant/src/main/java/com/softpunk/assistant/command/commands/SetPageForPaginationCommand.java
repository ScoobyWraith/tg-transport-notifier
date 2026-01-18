package com.softpunk.assistant.command.commands;

import com.softpunk.assistant.paginator.Paginator;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SetPageForPaginationCommand extends AutowiredCommand {
    @Override
    public boolean isAcceptable(UserSession session, Message message) {
        State state = session.getStateMachine().getCurrentState();

        if (!state.hasPagination()) {
            return false;
        }

        try {
            Long.parseLong(message.getText());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public void doCommand(UserSession session, Message message) {
        State state = session.getStateMachine().getCurrentState();
        Paginator paginator = state.getPaginator();
        paginator.toPage(Long.parseLong(message.getText()));
        state.fillMessages(session);
    }
}

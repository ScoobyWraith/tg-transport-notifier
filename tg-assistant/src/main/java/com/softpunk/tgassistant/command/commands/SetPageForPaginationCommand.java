package com.softpunk.tgassistant.command.commands;

import com.softpunk.tgassistant.paginator.Paginator;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.State;
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

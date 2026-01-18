package com.softpunk.tgassistant.command;

import com.softpunk.tgassistant.command.commands.AutowiredCommand;
import com.softpunk.tgassistant.command.commands.FallbackCommand;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandsDispatcher {
    private final List<AutowiredCommand> commands;
    private final FallbackCommand fallbackCommand;

    public boolean process(UserSession session, Update update) {
        State state = session.getStateMachine().getCurrentState();
        boolean processed = false;

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            List<AutowiredCommand> activeCommands = commands.stream()
                    .filter(c -> state.isCommandAcceptable(c) && c.isAcceptable(session, message))
                    .toList();

            if (activeCommands.size() == 1) {
                activeCommands.get(0).doCommand(session, message);
                processed = true;
            } else {
                log.warn("activeCommands has incorrect size: {}.", activeCommands);
            }
        }

        return processed;
    }

    public void fallback(UserSession session, Update update) {
        Message message;

        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage();
        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
        } else {
            log.warn("Update '{}' hasn't message.", update);
            return;
        }

        fallbackCommand.doCommand(session, message);
    }
}

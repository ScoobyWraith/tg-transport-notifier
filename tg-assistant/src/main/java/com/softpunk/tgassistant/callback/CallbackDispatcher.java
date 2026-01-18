package com.softpunk.tgassistant.callback;

import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackDispatcher {
    private final List<Callback> callbacks;

    public boolean process(UserSession session, Update update) {
        State state = session.getStateMachine().getCurrentState();
        boolean processed = false;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            List<Callback> activeCallbacks = callbacks.stream()
                    .filter(c -> state.isRollbackAcceptable(c) && c.isAcceptable(session, callbackQuery))
                    .toList();

            if (activeCallbacks.size() == 1) {
                activeCallbacks.get(0).processCallback(session, callbackQuery);
                processed = true;
            } else {
                log.warn("activeCallbacks has incorrect size: {}.", activeCallbacks);
            }
        }

        return processed;
    }
}

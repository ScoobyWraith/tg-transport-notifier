package com.softpunk.tgassistant.service;

import com.softpunk.bot.UpdateReceiver;
import com.softpunk.tgassistant.callback.CallbackDispatcher;
import com.softpunk.tgassistant.command.CommandsDispatcher;
import com.softpunk.tgassistant.config.AppSettings;
import com.softpunk.tgassistant.usersession.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpdateReceiverImpl implements UpdateReceiver {
    private final AppSettings appSettings;
    private final AutowireCapableBeanFactory factory;
    private final CommandsDispatcher commandsDispatcher;
    private final CallbackDispatcher callbackDispatcher;

    private final Map<Long, UserSession> sessions = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        UserSession session = this.getSession(update);
        session.getMessagesDispatcher().clear();
        session.setLastUpdate(Instant.now());
        boolean commandProcessed = commandsDispatcher.process(session, update);
        boolean callbackProcessed = callbackDispatcher.process(session, update);

        if (!commandProcessed && !callbackProcessed) {
            commandsDispatcher.fallback(session, update);
        }
    }

    @Override
    public List<SendMessage> getMessagesToSend(Update update) {
        UserSession session = this.getSession(update);
        return session.getMessagesDispatcher().getMessagesToSend();
    }

    public static long getChatIdFromUpdate(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    private UserSession getSession(Update update) {
        long chatId = UpdateReceiverImpl.getChatIdFromUpdate(update);

        if (sessions.containsKey(chatId)) {
            return sessions.get(chatId);
        }

        int maxSessionsCount = appSettings.getSession().limit();
        int currentSessionsCount = sessions.size();

        if (currentSessionsCount >= maxSessionsCount) {
            long oldestChatId = sessions.keySet().stream()
                    .min((k1, k2) -> {
                        long t1 = sessions.get(k1).getLastUpdate().toEpochMilli();
                        long t2 = sessions.get(k2).getLastUpdate().toEpochMilli();
                        return Math.toIntExact(t1 - t2);
                    })
                    .orElse(chatId);
            sessions.remove(oldestChatId);
        }

        UserSession session = factory.createBean(UserSession.class);
        session.setChatId(chatId);
        session.setLang(appSettings.getSession().defaultLocale());
        sessions.put(chatId, session);

        return session;
    }
}

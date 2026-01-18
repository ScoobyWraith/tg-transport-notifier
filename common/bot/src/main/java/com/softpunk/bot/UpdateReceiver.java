package com.softpunk.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UpdateReceiver {
    void onUpdateReceived(Update update);

    List<SendMessage> getMessagesToSend(Update update);
}

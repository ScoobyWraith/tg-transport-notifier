package com.softpunk.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateReceiver {
    void onUpdateReceived(Update update);
}

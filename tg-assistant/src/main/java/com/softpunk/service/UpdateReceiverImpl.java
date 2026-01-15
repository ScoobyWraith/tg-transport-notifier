package com.softpunk.service;

import com.softpunk.bot.UpdateReceiver;
import com.softpunk.localization.LocalesGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateReceiverImpl implements UpdateReceiver {
    private final LocalesGetter localesGetter;

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public List<SendMessage> getMessagesToSend() {
        return List.of();
    }
}

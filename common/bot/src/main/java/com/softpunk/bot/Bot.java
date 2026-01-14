package com.softpunk.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class Bot extends TelegramLongPollingBot {
    private static TelegramBotsApi TELEGRAM_BOTS_API;

    private final InitBotSettings settings;

    public Bot(InitBotSettings initBotSettings) throws TelegramApiException {
        super(initBotSettings.token());

        this.settings = initBotSettings;
        TelegramBotsApi telegramBotsApi = Bot.getApi();
        telegramBotsApi.registerBot(this);
        log.debug("TG bot with name {} was registered!", settings.name());
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateReceiver updateReceiver = this.settings.updateReceiver();

        if (updateReceiver != null) {
            updateReceiver.onUpdateReceived(update);
        }
    }

    @Override
    public String getBotUsername() {
        return this.settings.name();
    }

    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Error on sending message '{}'.", sendMessage);
        }
    }

    private static TelegramBotsApi getApi() throws TelegramApiException {
        if (Bot.TELEGRAM_BOTS_API == null) {
            Bot.TELEGRAM_BOTS_API = new TelegramBotsApi(DefaultBotSession.class);
        }

        return Bot.TELEGRAM_BOTS_API;
    }
}

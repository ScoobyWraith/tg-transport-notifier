package com.softpunk.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpunk.bot.Bot;
import com.softpunk.bot.InitBotSettings;
import com.softpunk.bot.UpdateReceiver;
import com.softpunk.localization.LocalesGetter;
import com.softpunk.transportclient.mockimpl.TransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class Beans {
    @Bean
    public ObjectMapper mapper() {
        ObjectMapper result = new ObjectMapper();
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return result;
    }

    @Bean
    public LocalesGetter localesGetter(ObjectMapper mapper, AppSettings appSettings) {
        return new LocalesGetter(mapper, appSettings.getLocalization().path());
    }

    @Bean
    public TransportClient transportClient() {
        return new TransportClient();
    }

    @Bean
    public Bot bot(BotCredentials botCredentials, UpdateReceiver updateReceiver) throws TelegramApiException {
        return new Bot(new InitBotSettings(botCredentials.getToken(), botCredentials.getName(), updateReceiver));
    }
}

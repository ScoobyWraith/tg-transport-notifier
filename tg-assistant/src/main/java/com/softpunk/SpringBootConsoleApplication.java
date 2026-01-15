package com.softpunk;

import com.softpunk.bot.Bot;
import com.softpunk.bot.InitBotSettings;
import com.softpunk.bot.UpdateReceiver;
import com.softpunk.config.BotCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {
    @Autowired
    private BotCredentials botCredentials;

    @Autowired
    private UpdateReceiver updateReceiver;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Bot(new InitBotSettings(this.botCredentials.getToken(), this.botCredentials.getName(), this.updateReceiver));
        log.debug("TG assistant is running...");
    }
}

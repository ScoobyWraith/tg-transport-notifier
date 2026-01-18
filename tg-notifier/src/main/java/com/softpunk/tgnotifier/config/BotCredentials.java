package com.softpunk.tgnotifier.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "credentials.bot")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BotCredentials {
    String token;
    String name;
}

package com.softpunk.tgnotifier.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "app")
public class AppSettings {
    LocalizationSettings localization;
    NotifierSettings notifier;
    SessionSettings session;
    ScheduleSettings schedule;

    public record LocalizationSettings(String path) {}
    public record SessionSettings(String defaultLocale) {}
    public record ScheduleSettings(int maxIntervalMinutes) {}
    public record NotifierSettings(int periodMs, int minDelayMs, int maxDelayMs) {}
}

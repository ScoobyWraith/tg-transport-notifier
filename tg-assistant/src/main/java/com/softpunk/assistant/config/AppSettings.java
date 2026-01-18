package com.softpunk.assistant.config;

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
    SessionSettings session;
    PaginationSettings pagination;
    ScheduleSettings schedule;
    StatesSettings states;
    boolean useMainMenuButton;

    public record LocalizationSettings(String path) {}
    public record SessionSettings(int limit, String defaultLocale) {}
    public record PaginationSettings(int pageSize) {}
    public record ScheduleSettings(int maxIntervalMinutes) {}
    public record StatesSettings(int statesSizeToBackButton) {}
}

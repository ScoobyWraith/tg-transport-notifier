package com.softpunk.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpunk.localization.LocalesGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}

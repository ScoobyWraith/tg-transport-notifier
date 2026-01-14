package com.softpunk.localization;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LocalesGetter {
    private final Map<String, Map<String, String>> locales;

    public LocalesGetter(ObjectMapper mapper, String filePath) {
        Map<String, Map<String, String>> tmpLocales;

        try {
            tmpLocales = mapper.readValue(Paths.get(filePath).toFile(), Map.class);
        } catch (IOException e) {
            log.warn("Can't set locales.", e);
            tmpLocales = Map.of();
        }

        locales = tmpLocales;
    }

    public String getText(String alias, String lang, Object... params) {
        String errText = "err_" + alias;

        if (!locales.containsKey(alias)) {
            log.warn("Alias '{}' not found.", alias);
            return errText;
        }

        Map<String, String> aliases = locales.get(alias);

        if (!aliases.containsKey(lang)) {
            log.warn("Alias '{}' has not lang '{}'.", alias, lang);
            return errText;
        }

        return String.format(aliases.get(lang), params);
    }
}

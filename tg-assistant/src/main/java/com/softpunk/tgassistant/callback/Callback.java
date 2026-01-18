package com.softpunk.tgassistant.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpunk.localization.LocalesGetter;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.usersession.UserSession;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
public abstract class Callback {
    @Autowired @Setter
    protected LocalesGetter localesGetter;

    @Autowired @Setter
    protected ObjectMapper objectMapper;

    public abstract boolean isAcceptable(UserSession session, CallbackQuery query);

    public abstract void processCallback(UserSession session, CallbackQuery query);

    protected boolean checkType(CallbackData.CallbackType type, CallbackQuery query) {
        CallbackData callbackData = getData(query.getData(), CallbackData.class);
        return callbackData != null && callbackData.getType() == type;
    }

    public <T> T getData(String data, Class<T> tClass) {
        T result;

        try {
            result = objectMapper.readValue(data, tClass);
        } catch (JsonProcessingException e) {
            log.warn("Error on parsing JSON into {}: '{}'.", tClass, data, e);
            return null;
        }

        return result;
    }
}

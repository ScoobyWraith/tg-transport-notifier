package com.softpunk.assistant.keyboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.localization.LocalesGetter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public abstract class Keyboard<Data> {
    @Autowired
    @Setter
    @Getter
    protected LocalesGetter localesGetter;

    @Autowired
    @Setter
    protected ObjectMapper objectMapper;

    public abstract void addKeyboard(SendMessage message, String lang);

    public abstract void loadData(Data data);

    protected InlineKeyboardMarkup createInlineKeyboardMarkup(List<KbButtonsSettings> data) {
        int rows = 0;
        int cols = 0;
        Map<Integer, Map<Integer, KbButtonsSettings>> dataMap = new HashMap<>();

        for (KbButtonsSettings settings : data) {
            int row = settings.getRow();
            int col = settings.getCol();

            rows = Math.max(rows, row);
            cols = Math.max(cols, col);

            if (!dataMap.containsKey(row)) {
                dataMap.put(row, new HashMap<>());
            }

            Map<Integer, KbButtonsSettings> dataRow = dataMap.get(row);
            dataRow.put(col, settings);
        }

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> kb = new ArrayList<>();

        for (int r = 0; r <= rows; r++) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            for (int c = 0; c <= cols; c++) {
                Map<Integer, KbButtonsSettings> rowSettings = dataMap.get(r);

                if (rowSettings == null) {
                    continue;
                }

                KbButtonsSettings settings = rowSettings.get(c);

                if (settings == null) {
                    continue;
                }

                InlineKeyboardButton button = createInlineButton(settings.getText(), settings.getData());
                row.add(button);
            }

            kb.add(row);
        }

        result.setKeyboard(kb);
        return result;
    }

    protected InlineKeyboardButton createInlineButton(String text, Object data) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);

        try {
            button.setCallbackData(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.warn("Error on creating JSON for keyboard.", e);
        }

        return button;
    }

    protected InlineKeyboardButton getEmptyButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(" ");

        try {
            button.setCallbackData(objectMapper.writeValueAsString(new CallbackData()));
        } catch (JsonProcessingException e) {
            log.warn("Error on creating JSON for keyboard.", e);
        }

        return button;
    }
}

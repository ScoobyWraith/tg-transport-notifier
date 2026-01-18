package com.softpunk.tgassistant.keyboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.callback.dto.NavigatePaginationCallbackData;
import com.softpunk.tgassistant.paginator.PaginatorData;
import com.softpunk.tgassistant.paginator.PaginatorRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KbPaginator extends Keyboard<PaginatorData> {
    private PaginatorData data;

    @Override
    public void addKeyboard(SendMessage message, String lang) {
        if (data == null) {
            log.warn("Data for keyboard is null.");
            return;
        }

        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> kb = new ArrayList<>();

        for (PaginatorRecord record : data.getData()) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(record.title());

            try {
                button.setCallbackData(objectMapper.writeValueAsString(record.data()));
            } catch (JsonProcessingException e) {
                log.warn("Error on creating JSON for keyboard.", e);
            }

            row.add(button);
            kb.add(row);
        }

        InlineKeyboardButton nextButton = getEmptyButton();
        InlineKeyboardButton previousButton = getEmptyButton();
        int navigationButtons = 0;

        if (data.isHasNext()) {
            NavigatePaginationCallbackData nextData = new NavigatePaginationCallbackData(NavigatePaginationCallbackData.Navigation.NEXT);
            nextButton.setText(localesGetter.getText(Aliases.BTN_NEXT_PAGE, lang));

            try {
                nextButton.setCallbackData(objectMapper.writeValueAsString(nextData));
                navigationButtons++;
            } catch (JsonProcessingException e) {
                log.warn("Error on creating JSON for keyboard.", e);
            }
        }

        if (data.isHasPrevious()) {
            NavigatePaginationCallbackData previousData = new NavigatePaginationCallbackData(NavigatePaginationCallbackData.Navigation.PREVIOUS);
            previousButton.setText(localesGetter.getText(Aliases.BTN_PREVIOUS_PAGE, lang));

            try {
                previousButton.setCallbackData(objectMapper.writeValueAsString(previousData));
                navigationButtons++;
            } catch (JsonProcessingException e) {
                log.warn("Error on creating JSON for keyboard.", e);
            }
        }

        if (navigationButtons > 0) {
            kb.add(List.of(previousButton, nextButton));
        }

        InlineKeyboardButton backButton = createInlineButton(
                localesGetter.getText(Aliases.BTN_BACK, lang),
                new CallbackData(CallbackData.CallbackType.BACK)
        );
        kb.add(List.of(backButton));

        result.setKeyboard(kb);
        message.setReplyMarkup(result);
    }

    @Override
    public void loadData(PaginatorData paginatorData) {
        data = paginatorData;
    }
}

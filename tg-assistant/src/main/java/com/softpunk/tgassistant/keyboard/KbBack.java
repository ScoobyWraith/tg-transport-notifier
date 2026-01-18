package com.softpunk.tgassistant.keyboard;

import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class KbBack extends Keyboard<Void> {
    @Override
    public void addKeyboard(SendMessage message, String lang) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(List.of(
                new KbButtonsSettings(
                        localesGetter.getText(Aliases.BTN_BACK, lang),
                        new CallbackData(CallbackData.CallbackType.BACK),
                        0, 0
                )
        ));

        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public void loadData(Void unused) {}
}

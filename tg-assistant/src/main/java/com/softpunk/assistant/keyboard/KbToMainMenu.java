package com.softpunk.assistant.keyboard;

import com.softpunk.bot.callback.dto.CallbackData;
import com.softpunk.localization.Aliases;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class KbToMainMenu extends Keyboard<Void> {
    @Value("${app.useMainMenuButton}")
    private boolean useMainMenuButton;

    @Override
    public void addKeyboard(SendMessage message, String lang) {
        if (!useMainMenuButton) {
            return;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(List.of(
                new KbButtonsSettings(
                        localesGetter.getText(Aliases.BTN_TO_MENU, lang),
                        new CallbackData(CallbackData.CallbackType.TO_MAIN_MENU),
                        0, 0
                )
        ));

        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public void loadData(Void unused) {
    }
}

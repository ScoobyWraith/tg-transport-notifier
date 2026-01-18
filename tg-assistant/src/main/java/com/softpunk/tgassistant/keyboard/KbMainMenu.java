package com.softpunk.tgassistant.keyboard;

import com.softpunk.localization.Aliases;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Component
public class KbMainMenu extends Keyboard<Boolean> {
    private boolean hasList;

    @Override
    public void addKeyboard(SendMessage message, String lang) {
        List<KbButtonsSettings> list = new ArrayList<>();
        list.add(new KbButtonsSettings(
                localesGetter.getText(Aliases.BTN_NEW_SCHEDULE, lang),
                new CallbackData(CallbackData.CallbackType.TO_NEW_SCHEDULE),
                0, 0
        ));

        if (hasList) {
            list.add(new KbButtonsSettings(
                    localesGetter.getText(Aliases.BTN_MY_SCHEDULE, lang),
                    new CallbackData(CallbackData.CallbackType.TO_USER_SCHEDULE_LIST),
                    0, 1
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(list);
        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public void loadData(Boolean hasList) {
        this.hasList = hasList;
    }
}

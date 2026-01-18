package com.softpunk.assistant.keyboard;

import com.softpunk.assistant.callback.dto.CallbackData;
import com.softpunk.assistant.usersession.state.userlist.callbacks.dto.ScheduleItemCallbackData;
import com.softpunk.localization.Aliases;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class KbScheduleItem extends Keyboard<Long> {
    private long id;

    @Override
    public void addKeyboard(SendMessage message, String lang) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(List.of(
                new KbButtonsSettings(
                        localesGetter.getText(Aliases.BTN_EDIT, lang),
                        new ScheduleItemCallbackData(CallbackData.CallbackType.EDIT_SCHEDULE_ITEM_INTERVAL, this.id),
                        0, 0
                ),
                new KbButtonsSettings(
                        localesGetter.getText(Aliases.BTN_REMOVE, lang),
                        new ScheduleItemCallbackData(CallbackData.CallbackType.REMOVE_ITEM, this.id),
                        0, 1
                ),
                new KbButtonsSettings(
                        localesGetter.getText(Aliases.BTN_BACK, lang),
                        new CallbackData(CallbackData.CallbackType.BACK),
                        1, 0
                )
        ));

        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public void loadData(Long id) {
        this.id = id;
    }
}

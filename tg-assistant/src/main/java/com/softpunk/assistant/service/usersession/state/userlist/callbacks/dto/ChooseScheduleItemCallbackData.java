package com.softpunk.assistant.service.usersession.state.userlist.callbacks.dto;

import com.softpunk.bot.callback.dto.CallbackData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChooseScheduleItemCallbackData extends CallbackData {
    private long id;

    public ChooseScheduleItemCallbackData(long id) {
        type = CallbackType.CHOOSE_SCHEDULE_ITEM;
        this.id = id;
    }
}

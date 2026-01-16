package com.softpunk.assistant.service.usersession.state.userlist.callbacks.dto;

import com.softpunk.bot.callback.dto.CallbackData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ScheduleItemCallbackData extends CallbackData {
    private long id;

    public ScheduleItemCallbackData(CallbackType type, long id) {
        this.type = type;
        this.id = id;
    }
}

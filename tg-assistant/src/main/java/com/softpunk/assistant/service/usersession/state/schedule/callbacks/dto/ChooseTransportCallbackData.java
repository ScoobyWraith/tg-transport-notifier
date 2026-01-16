package com.softpunk.assistant.service.usersession.state.schedule.callbacks.dto;

import com.softpunk.bot.callback.dto.CallbackData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChooseTransportCallbackData extends CallbackData {
    private long id;

    public ChooseTransportCallbackData(long id) {
        type = CallbackType.CHOOSE_TRANSPORT;
        this.id = id;
    }
}

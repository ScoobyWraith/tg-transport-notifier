package com.softpunk.tgassistant.callback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallbackData {
    protected CallbackType type = CallbackType.EMPTY;

    public static enum CallbackType {
        EMPTY,
        TO_MAIN_MENU,
        TO_NEW_SCHEDULE,
        TO_USER_SCHEDULE_LIST,
        PAGINATION,
        CHOOSE_TRANSPORT,
        CHOOSE_SCHEDULE_ITEM,
        BACK,
        EDIT_SCHEDULE_ITEM_INTERVAL,
        REMOVE_ITEM
    }
}

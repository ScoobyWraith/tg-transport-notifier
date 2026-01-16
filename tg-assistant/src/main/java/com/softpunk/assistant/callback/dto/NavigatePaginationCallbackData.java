package com.softpunk.assistant.callback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NavigatePaginationCallbackData extends CallbackData {
    protected Navigation navigation;

    public NavigatePaginationCallbackData(Navigation navigation) {
        type = CallbackType.PAGINATION;
        this.navigation = navigation;
    }

    public static enum Navigation {
        PREVIOUS,
        NEXT
    }
}

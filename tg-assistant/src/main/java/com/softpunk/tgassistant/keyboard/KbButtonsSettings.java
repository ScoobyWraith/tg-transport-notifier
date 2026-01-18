package com.softpunk.tgassistant.keyboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KbButtonsSettings {
    private final String text;
    private final Object data;
    private final int row;
    private final int col;
}

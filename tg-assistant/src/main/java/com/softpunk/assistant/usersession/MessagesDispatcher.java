package com.softpunk.assistant.usersession;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@Scope("prototype")
public class MessagesDispatcher {
    private final List<SendMessage> messagesToSend = new ArrayList<>();

    public void prepareMessageToSend(SendMessage sendMessage) {
        sendMessage.enableHtml(true);
        messagesToSend.add(sendMessage);
    }

    public void clear() {
        messagesToSend.clear();
    }
}

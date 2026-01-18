package com.softpunk.assistant.usersession;

import com.softpunk.assistant.usersession.state.StateMachine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Setter
@Getter
@Component
@Scope("prototype")
public class UserSession {
    private Instant lastUpdate;
    private long chatId;
    private String lang;
    @Autowired private StateMachine stateMachine;
    @Autowired private MessagesDispatcher messagesDispatcher;
}

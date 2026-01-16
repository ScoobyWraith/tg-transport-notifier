package com.softpunk.service.usersession;

import com.softpunk.service.usersession.state.StateMachine;
import lombok.Getter;
import lombok.Setter;
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
    private StateMachine stateMachine;
}

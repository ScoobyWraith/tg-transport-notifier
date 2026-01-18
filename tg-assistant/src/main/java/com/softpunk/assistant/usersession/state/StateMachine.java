package com.softpunk.assistant.usersession.state;

import com.softpunk.assistant.config.AppSettings;
import com.softpunk.assistant.usersession.state.mainmenu.MainMenuState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Component
@Scope("prototype")
@Slf4j
public class StateMachine {
    @Autowired
    private AppSettings appSettings;

    private Map<Class<? extends State>, State> states = new HashMap<>();
    private Deque<State> statesQueue = new ArrayDeque<>();

    @Autowired
    public StateMachine(List<State> states) {
        states.forEach(s -> {
            if (s instanceof MainMenuState) {
                statesQueue.addLast(s);
            }

            this.states.put(s.getClass(), s);
        });
    }

    public void toState(Class<? extends State> stateClass) {
        State newState = states.get(stateClass);

        if (newState.equals(getCurrentState())) {
            return;
        }

        statesQueue.addLast(newState);

        if (statesQueue.size() > appSettings.getStates().statesSizeToBackButton()) {
            statesQueue.pollFirst();
        }

        newState.init();
    }

    public State getCurrentState() {
        return statesQueue.getLast();
    }

    public void back() {
        if (statesQueue.size() == 1) {
            toState(MainMenuState.class);
            return;
        }

        statesQueue.pollLast();
    }
}

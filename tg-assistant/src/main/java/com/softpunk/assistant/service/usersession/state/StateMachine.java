package com.softpunk.assistant.service.usersession.state;

import lombok.Getter;
import lombok.Setter;
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
public class StateMachine {
    private Map<Class<? extends State>, State> states = new HashMap<>();
    private Deque<State> statesQueue = new ArrayDeque<>();

    @Autowired
    public StateMachine(List<State> states) {
        MainMenuState mainMenuState;

        states.forEach(s -> {
            if (s instanceof MainMenuState) {
                statesQueue.addLast(s);
            }

            this.states.put(s.getClass(), s);
        });
    }

    public void toState(Class<? extends State> stateClass) {
        State currentState = null;

        if (states.containsKey(stateClass)) {
            currentState = states.get(stateClass);
        } else {
            try {
                currentState = stateClass.getConstructor().newInstance();
                states.put(stateClass, currentState);
            } catch (Exception e) {
                log.warn("Error on creating state {}.", stateClass.getName(), e);
                currentState = states.get(MainMenuState.class);
            }
        }

        if (currentState.equals(getCurrentState())) {
            return;
        }

        statesQueue.addLast(currentState);

        if (statesQueue.size() > 5) {
            statesQueue.pollFirst();
        }

        currentState.init();
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

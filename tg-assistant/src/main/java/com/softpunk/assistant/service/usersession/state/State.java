package com.softpunk.assistant.service.usersession.state;

import com.softpunk.localization.LocalesGetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Component
public abstract class State {
    @Setter
    @Autowired
    protected LocalesGetter localesGetter;

    protected Paginator paginator;

    protected Set<Class<? extends Command>> commands = new HashSet<>(Set.of(
            StartCommand.class
    ));
    protected Set<Class<? extends Callback>> callbacks = new HashSet<>(List.of(
            StartCallback.class,
            BackCallback.class
    ));

    public abstract void fillMessages(UserSession session);

    public boolean isCommandAcceptable(Command command) {
        return commands.contains(command.getClass());
    }

    public boolean isRollbackAcceptable(Callback callback) {
        return callbacks.contains(callback.getClass());
    }

    public void init() {
        if (paginator != null) {
            paginator.init();
        }
    }

    public boolean hasPagination() {
        return paginator != null;
    }

    protected void registerCommands(List<Class<? extends Command>> commands) {
        this.commands.addAll(commands);
    }

    protected void registerCallbacks(List<Class<? extends Callback>> callbacks) {
        this.callbacks.addAll(callbacks);
    }
}

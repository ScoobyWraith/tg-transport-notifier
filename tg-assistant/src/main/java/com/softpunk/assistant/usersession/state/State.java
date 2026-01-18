package com.softpunk.assistant.usersession.state;

import com.softpunk.assistant.callback.Callback;
import com.softpunk.assistant.callback.callbacks.BackCallback;
import com.softpunk.assistant.callback.callbacks.StartCallback;
import com.softpunk.assistant.command.Command;
import com.softpunk.assistant.command.commands.StartCommand;
import com.softpunk.assistant.paginator.Paginator;
import com.softpunk.assistant.usersession.UserSession;
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

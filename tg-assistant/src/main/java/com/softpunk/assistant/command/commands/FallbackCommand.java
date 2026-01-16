package com.softpunk.assistant.command.commands;

import com.softpunk.bot.keyboards.KbToMainMenu;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.localization.Aliases;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FallbackCommand extends Command {
    @Autowired
    @Setter
    private KbToMainMenu kbToMainMenu;

    @Override
    public void doCommand(UserSession session, Message message) {
        SendMessage sendMessage = new SendMessage(
                message.getChatId().toString(),
                localesGetter.getText(Aliases.MSG_FALLBACK, session.getLang())
        );
        kbToMainMenu.addKeyboard(sendMessage, session.getLang());
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }
}

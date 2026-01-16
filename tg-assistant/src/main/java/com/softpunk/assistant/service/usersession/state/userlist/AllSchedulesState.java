package com.softpunk.assistant.service.usersession.state.userlist;

import com.softpunk.bot.callback.NavigatePaginationCallback;
import com.softpunk.bot.command.SetPageForPaginationCommand;
import com.softpunk.bot.keyboards.KbPaginator;
import com.softpunk.bot.paginator.Paginator;
import com.softpunk.bot.paginator.PaginatorDataGetter;
import com.softpunk.bot.paginator.PaginatorRecord;
import com.softpunk.bot.usersession.UserSession;
import com.softpunk.bot.usersession.state.State;
import com.softpunk.bot.usersession.state.userlist.callbacks.ToScheduleItemCallback;
import com.softpunk.bot.usersession.state.userlist.callbacks.dto.ChooseScheduleItemCallbackData;
import com.softpunk.config.AppSettings;
import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import com.softpunk.localization.LocalesGetter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

@Setter
@Component
@Scope("prototype")
public class AllSchedulesState extends State implements PaginatorDataGetter {
    private UserSession session;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private LocalesGetter localesGetter;

    @Autowired
    private KbPaginator kbPaginator;

    @Autowired
    private AppSettings appSettings;

    public AllSchedulesState() {
        registerCommands(List.of(
                SetPageForPaginationCommand.class
        ));

        registerCallbacks(List.of(
                NavigatePaginationCallback.class,
                ToScheduleItemCallback.class
        ));

        paginator = new Paginator();
    }

    @Override
    public void fillMessages(UserSession session) {
        this.session = session;
        SendMessage sendMessage = paginator.getListWithNavigation(
                this,
                appSettings.getPagination().getPageSize(),
                kbPaginator,
                session
        );
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }

    @Override
    public long totalCount() {
        return scheduleRepository.countByUserIdAndCompleteFalse(session.getChatId());
    }

    @Override
    public List<PaginatorRecord> getData(long offset, int size) {
        long chatId = session.getChatId();
        String lang = session.getLang();
        List<Schedule> scheduleList = scheduleRepository.findAllActiveScheduleForUserPage(chatId, offset, size);
        List<PaginatorRecord> result = new ArrayList<>();

        for (Schedule sh : scheduleList) {
            String name = sh.getTransport().getCommonName();
            int interval = sh.getDelay();
            String text = localesGetter.getText(Aliases.ITM_SCHEDULED, lang, name, interval);
            PaginatorRecord record = new PaginatorRecord(text, new ChooseScheduleItemCallbackData(sh.getId()));
            result.add(record);
        }

        return result;
    }
}

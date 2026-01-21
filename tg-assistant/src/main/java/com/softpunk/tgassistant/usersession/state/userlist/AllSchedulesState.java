package com.softpunk.tgassistant.usersession.state.userlist;

import com.softpunk.config.AppSettings;
import com.softpunk.dao.ScheduleRepository;
import com.softpunk.dao.model.Schedule;
import com.softpunk.localization.Aliases;
import com.softpunk.localization.LocalesGetter;
import com.softpunk.tgassistant.callback.callbacks.NavigatePaginationCallback;
import com.softpunk.tgassistant.command.commands.SetPageForPaginationCommand;
import com.softpunk.tgassistant.keyboard.KbPaginator;
import com.softpunk.tgassistant.paginator.Paginator;
import com.softpunk.tgassistant.paginator.PaginatorDataGetter;
import com.softpunk.tgassistant.paginator.PaginatorRecord;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.State;
import com.softpunk.tgassistant.usersession.state.userlist.callbacks.ToScheduleItemCallback;
import com.softpunk.tgassistant.usersession.state.userlist.callbacks.dto.ChooseScheduleItemCallbackData;
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
                appSettings.getPagination().pageSize(),
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

package com.softpunk.assistant.usersession.state.schedule;

import com.softpunk.assistant.callback.callbacks.NavigatePaginationCallback;
import com.softpunk.assistant.command.commands.SetPageForPaginationCommand;
import com.softpunk.assistant.config.AppSettings;
import com.softpunk.assistant.keyboard.KbPaginator;
import com.softpunk.assistant.paginator.Paginator;
import com.softpunk.assistant.paginator.PaginatorDataGetter;
import com.softpunk.assistant.paginator.PaginatorRecord;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.assistant.usersession.state.State;
import com.softpunk.assistant.usersession.state.schedule.callbacks.ChooseTransportCallback;
import com.softpunk.assistant.usersession.state.schedule.callbacks.dto.ChooseTransportCallbackData;
import com.softpunk.dao.TransportRepository;
import com.softpunk.dao.model.Transport;
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
public class AllTransportsListState extends State implements PaginatorDataGetter {
    @Autowired
    private KbPaginator kbPaginator;

    @Autowired
    private AppSettings appSettings;

    @Autowired
    private TransportRepository transportRepository;

    public AllTransportsListState() {
        registerCommands(List.of(
                SetPageForPaginationCommand.class
        ));

        registerCallbacks(List.of(
                NavigatePaginationCallback.class,
                ChooseTransportCallback.class
        ));

        paginator = new Paginator();
    }

    @Override
    public void fillMessages(UserSession session) {
        SendMessage sendMessage = paginator.getListWithNavigation(this,
                appSettings.getPagination().pageSize(),
                kbPaginator,
                session
        );
        session.getMessagesDispatcher().prepareMessageToSend(sendMessage);
    }

    @Override
    public long totalCount() {
        return transportRepository.getTransportCount();
    }

    @Override
    public List<PaginatorRecord> getData(long offset, int size) {
        List<Transport> transportPageable = transportRepository.getTransportPageable(offset, size);
        List<PaginatorRecord> result = new ArrayList<>();

        for (Transport tr : transportPageable) {
            PaginatorRecord record = new PaginatorRecord(tr.getCommonName(), new ChooseTransportCallbackData(tr.getId()));
            result.add(record);
        }

        return result;
    }
}

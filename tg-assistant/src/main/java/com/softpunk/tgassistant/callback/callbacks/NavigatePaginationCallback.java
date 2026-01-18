package com.softpunk.tgassistant.callback.callbacks;

import com.softpunk.tgassistant.callback.Callback;
import com.softpunk.tgassistant.callback.dto.CallbackData;
import com.softpunk.tgassistant.callback.dto.NavigatePaginationCallbackData;
import com.softpunk.tgassistant.paginator.Paginator;
import com.softpunk.tgassistant.usersession.UserSession;
import com.softpunk.tgassistant.usersession.state.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Component
public class NavigatePaginationCallback extends Callback {
    @Override
    public boolean isAcceptable(UserSession session, CallbackQuery query) {
        return checkType(CallbackData.CallbackType.PAGINATION, query);
    }

    @Override
    public void processCallback(UserSession session, CallbackQuery query) {
        State state = session.getStateMachine().getCurrentState();
        Paginator paginator = state.getPaginator();
        NavigatePaginationCallbackData navigatePaginationCallbackData = getData(query.getData(), NavigatePaginationCallbackData.class);

        if (navigatePaginationCallbackData == null) {
            log.warn("Can't find data in '{}'.", query.getData());
            return;
        }

        if (navigatePaginationCallbackData.getNavigation().equals(NavigatePaginationCallbackData.Navigation.NEXT)) {
            paginator.nextPage();
        } else {
            paginator.previousPage();
        }

        state.fillMessages(session);
    }
}

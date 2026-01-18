package com.softpunk.assistant.paginator;

import com.softpunk.assistant.keyboard.KbPaginator;
import com.softpunk.assistant.usersession.UserSession;
import com.softpunk.localization.Aliases;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class Paginator {
    private long currentPage;

    public Paginator() {
        init();
    }

    public void init() {
        currentPage = 1;
    }

    public void toPage(long page) {
        currentPage = page;
    }

    public void nextPage() {
        toPage(currentPage + 1);
    }

    public void previousPage() {
        toPage(currentPage - 1);
    }

    public SendMessage getListWithNavigation(PaginatorDataGetter paginatorDataGetter,
                                             int pageSize,
                                             KbPaginator kbPaginator,
                                             UserSession session) {
        String lang = session.getLang();
        long totalCount = paginatorDataGetter.totalCount();
        long pages = (long) Math.ceil(1.0 * totalCount / pageSize);
        currentPage = Math.max(1, Math.min(pages, currentPage));
        long offset = (currentPage - 1) * pageSize;

        List<PaginatorRecord> data = paginatorDataGetter.getData(offset, pageSize);
        PaginatorData paginatorData = new PaginatorData();
        paginatorData.setData(data);
        paginatorData.setHasPrevious(currentPage > 1);
        paginatorData.setHasNext(currentPage < pages);

        String title = pages > 1
                ? kbPaginator.getLocalesGetter().getText(Aliases.MSG_PAGINATION_TITLE, lang, currentPage, pages)
                : kbPaginator.getLocalesGetter().getText(Aliases.MSG_PAGINATION_TITLE_EMPTY, lang);

        SendMessage sendMessage = new SendMessage(String.valueOf(session.getChatId()), title);

        kbPaginator.loadData(paginatorData);
        kbPaginator.addKeyboard(sendMessage, lang);
        return sendMessage;
    }
}

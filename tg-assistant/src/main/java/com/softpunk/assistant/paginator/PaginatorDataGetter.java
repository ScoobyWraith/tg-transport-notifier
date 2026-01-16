package com.softpunk.assistant.paginator;

import java.util.List;

public interface PaginatorDataGetter {
    long totalCount();

    List<PaginatorRecord> getData(long offset, int size);
}

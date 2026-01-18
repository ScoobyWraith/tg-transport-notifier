package com.softpunk.tgassistant.paginator;

import java.util.List;

public interface PaginatorDataGetter {
    long totalCount();

    List<PaginatorRecord> getData(long offset, int size);
}

package com.softpunk.tgassistant.paginator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatorData {
    private List<PaginatorRecord> data;
    private boolean hasPrevious;
    private boolean hasNext;
}

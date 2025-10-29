package esvar.ua.dekanatcontrolpanel.dto;

import java.util.List;

public class ErrorLogPageDto {

    private final List<ErrorLogSummaryDto> items;
    private final long total;
    private final int page;
    private final int pageSize;

    public ErrorLogPageDto(List<ErrorLogSummaryDto> items, long total, int page, int pageSize) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<ErrorLogSummaryDto> getItems() {
        return items;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
package application.remindermeeting.models;

import java.util.List;

public class PagedList<T> {
    //Membuat Response sendiri untuk Pagination yang akan ditampilakn dalam JSON
    private List<T> list;
    private Integer page;
    private Integer size;
    private Long total;

    public PagedList() { }
    public PagedList(List<T> list, Integer page, Integer size, Long total) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

package application.remindermeeting.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommonService<T,ID> {
    T save (T entity);
    T removeById(ID id);
    T findById(ID id);
    List<T> findAll();
    Page<T> findAll(T search, int page, int size, Sort.Direction direction);
}

package com.yummie.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PageUtil {

    public <T> Page<T> getPage(Integer page,
                               Integer size,
                               Sort sort,
                               Function<Pageable, Page<T>> function) {
        if (page == null || page < 1) page = 1;
        if (sort == null) sort = Sort.unsorted();
        Page<T> resultPage = function.apply(PageRequest.of(page - 1, size, sort));
        if (resultPage.getTotalPages() > 0 && page >= resultPage.getTotalPages()) {
            int lastPage = resultPage.getTotalPages() - 1;
            return function.apply(PageRequest.of(lastPage, size, sort));
        }
        return resultPage;
    }
}
package com.tuotiansudai.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageValidUtils {

    @Value("${mobile.app.page.size.limit}")
    private int defaultPageMaxSize;

    private final static int DEFAULT_PAGE_SIZE = 10;

    public int validPageSizeLimit(Integer pageSize) {
        if(pageSize == null || pageSize <= 0){
            return DEFAULT_PAGE_SIZE;
        }

        if (pageSize > defaultPageMaxSize) {
            return defaultPageMaxSize;
        }
        return pageSize;
    }
}

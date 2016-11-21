package com.tuotiansudai.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PageValidUtils {

    @Value("${mobile.app.page.size.limit}")
    private int defaultPageMaxSize;

    private final int defaultPageSize = 10;

    public int validPageSizeLimit(Integer pageSize) {
        if(pageSize == null){
            return defaultPageSize;
        }

        if (pageSize > defaultPageMaxSize) {
            return defaultPageMaxSize;
        }
        return pageSize;
    }
}

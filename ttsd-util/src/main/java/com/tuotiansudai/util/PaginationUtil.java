package com.tuotiansudai.util;

public class PaginationUtil {

    public static int calculateMaxPage(long count, int pageSize) {
        return (int) (count == 0 || count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
    }

    public static int calculateOffset(int index, int pageSize, long count) {
        int maxPage = calculateMaxPage(count, pageSize);
        int offset = index > maxPage ? maxPage : index;
        return (offset - 1) * pageSize;
    }

    public static int validateIndex(int index, int pageSize, long count) {
        int maxPage = calculateMaxPage(count, pageSize);
        return index > maxPage ? maxPage : index;
    }
}

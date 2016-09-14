package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class UserMessageResponseDataDto extends BaseResponseDataDto {
    private long index;
    private long pageSize;
    private long totalCount;
    private List<UserMessageDto> messages;

    public UserMessageResponseDataDto(long index, long pageSize, long totalCount, List<UserMessageDto> userMessages) {
        this.index = index;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.messages = userMessages;
    }

    public long getIndex() {
        return index;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<UserMessageDto> getMessages() {
        return messages;
    }
}

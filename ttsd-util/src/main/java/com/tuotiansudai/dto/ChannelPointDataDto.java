package com.tuotiansudai.dto;

public class ChannelPointDataDto extends BaseDataDto {
    private Long id;

    public ChannelPointDataDto() {
    }

    public ChannelPointDataDto(boolean status, Long id) {
        this.status = status;
        this.id = id;
    }

    public ChannelPointDataDto(boolean status, String message) {
        super(status, message);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.tuotiansudai.fudian.dto.request;

public class QueryUserRequestDto extends BaseRequestDto {

    public QueryUserRequestDto(String userName, String accountNo) {
        super(Source.WEB, null, null, userName, accountNo);
    }
}

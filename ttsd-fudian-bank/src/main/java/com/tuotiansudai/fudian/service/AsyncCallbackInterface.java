package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.response.ResponseDto;

public interface AsyncCallbackInterface {

    void returnCallback(ResponseDto responseData);

    ResponseDto callback(String responseData);

    Boolean isSuccess(String orderNo);
}

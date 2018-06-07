package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.response.ResponseDto;

public interface ReturnCallbackInterface {

    void returnCallback(ResponseDto responseData);

    Boolean isSuccess(String orderNo);
}

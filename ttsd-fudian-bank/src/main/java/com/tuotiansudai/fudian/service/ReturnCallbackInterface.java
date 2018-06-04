package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.response.ResponseDto;

public interface ReturnCallbackInterface {

    void returnCallback(ResponseDto responseData);

    Boolean isSuccess(String orderNo);
}

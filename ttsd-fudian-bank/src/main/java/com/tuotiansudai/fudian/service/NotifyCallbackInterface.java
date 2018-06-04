package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.response.ResponseDto;

public interface NotifyCallbackInterface {

    Gson gson = new GsonBuilder().create();

    long FIXED_DELAY = 1000 * 5;

    ResponseDto notifyCallback(String responseData);
}

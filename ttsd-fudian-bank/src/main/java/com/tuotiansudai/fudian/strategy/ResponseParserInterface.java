package com.tuotiansudai.fudian.strategy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.response.BaseContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;

public interface ResponseParserInterface<T extends BaseContentDto> {

    Gson gson = new GsonBuilder().create();

    ResponseDto<T> parse(String data);
}

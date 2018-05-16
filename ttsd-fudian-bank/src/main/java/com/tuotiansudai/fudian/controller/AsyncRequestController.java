package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankAsyncData;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

public abstract class AsyncRequestController {

    protected BankConfig bankConfig;

    public AsyncRequestController(BankConfig bankConfig) {
        this.bankConfig = bankConfig;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Source.class, new SourcePropertyEditor());
    }

    protected BankAsyncData generateAsyncRequestData(BaseRequestDto requestDto, ApiType apiType) {
        if (requestDto == null || Strings.isNullOrEmpty(requestDto.getRequestData())) {
            return new BankAsyncData(null, null, false, "请求数据生成失败");
        }

        return new BankAsyncData(bankConfig.getBankUrl() + apiType.getPath(), requestDto.getRequestData(), true, "SUCCESS");
    }

    protected boolean isBadRequest(List<String> values) {
        return Lists.newArrayList(values).stream().anyMatch(Strings::isNullOrEmpty);
    }
}

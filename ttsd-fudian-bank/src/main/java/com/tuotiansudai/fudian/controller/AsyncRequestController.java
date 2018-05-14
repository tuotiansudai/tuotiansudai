package com.tuotiansudai.fudian.controller;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class AsyncRequestController {

    private BankConfig bankConfig;

    public AsyncRequestController(BankConfig bankConfig) {
        this.bankConfig = bankConfig;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Source.class, new SourcePropertyEditor());
    }

    protected ResponseEntity<String> generateAsyncRequestData(BaseRequestDto requestDto, ApiType apiType) {
        JsonObject fields = new JsonObject();
        fields.addProperty("reqData", requestDto == null ? null : requestDto.getRequestData());

        JsonObject formData = new JsonObject();
        formData.addProperty("url", bankConfig.getBankUrl() + apiType.getPath());
        formData.addProperty("status", requestDto != null);
        formData.addProperty("message", requestDto != null ? "数据生成成功" : "数据生成异常");
        formData.add("fields", fields);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.add("data", formData);


        return ResponseEntity.ok(new GsonBuilder().create().toJson(jsonObject));
    }
}

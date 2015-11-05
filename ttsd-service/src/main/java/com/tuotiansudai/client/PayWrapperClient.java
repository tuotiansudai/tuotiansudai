package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PayWrapperClient {

    static Logger logger = Logger.getLogger(PayWrapperClient.class);

    private final static String URL_TEMPLATE = "http://{host}:{port}{context}{uri}";

    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${paywrapper.host}")
    private String host;

    @Value("${paywrapper.port}")
    private String port;

    @Value("${paywrapper.context}")
    private String context;

    private String registerPath = "/register";

    private String rechargePath = "/recharge";

    private String bindCardPath = "/bind-card";

    private String loanPath = "/loan";

    private String loanOutPath = "/loan/loan-out";

    private String withdrawPath = "/withdraw";

    private String investPath = "/invest";

    private String investNopwdPath = "/invest-nopwd";

    private String agreementPath = "/agreement";

    private String repayPath = "/repay";

    private ObjectMapper objectMapper = new ObjectMapper();

    public BaseDto<PayDataDto> register(RegisterAccountDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(registerPath, requestJson);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }


        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payFormDataDto = new PayDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(rechargePath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> withdraw(WithdrawDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(withdrawPath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(bindCardPath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(investPath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> agreement(AgreementDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(agreementPath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayFormDataDto> repay(RepayDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(repayPath, requestJson);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayDataDto> investNopwd(InvestDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(investNopwdPath, requestJson);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        return baseDto;
    }

    public BaseDto<PayDataDto> createLoan(LoanDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(loanPath, requestJson);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payFormDataDto = new PayDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayDataDto> updateLoan(LoanDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.put(loanPath, requestJson);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payFormDataDto = new PayDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<PayDataDto> loanOut(LoanOutDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseJson = this.post(loanOutPath, requestJson);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payFormDataDto = new PayDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    public BaseDto<MonitorDataDto> monitor() {
        String responseJson = this.get("/monitor");
        if (!Strings.isNullOrEmpty(responseJson)) {
            try {
                return objectMapper.readValue(responseJson, new TypeReference<BaseDto<MonitorDataDto>>() {});
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        BaseDto<MonitorDataDto> resultDto = new BaseDto<>();
        MonitorDataDto dataDto = new MonitorDataDto();
        dataDto.setStatus(false);
        resultDto.setData(dataDto);

        return resultDto;
    }

    public BaseDto<BaseDataDto> investCallback() {
        String responseJson = this.post("/job/async_invest_notify", "");
        if (!Strings.isNullOrEmpty(responseJson)) {
            try {
                return objectMapper.readValue(responseJson, new TypeReference<BaseDto<BaseDataDto>>() {});
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        BaseDto<BaseDataDto> resultDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(false);
        resultDto.setData(dataDto);

        return resultDto;
    }

    private String get(String path) {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", path);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    private String post(String path, String requestJson) {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", path);
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private String put(String path, String requestJson) {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", path);
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder().url(url).put(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private BaseDto<PayDataDto> parsePayResponseJson(String json) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<PayDataDto>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<PayFormDataDto> parsePayFormJson(String json) {
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<PayFormDataDto>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setContext(String context) {
        this.context = context;
    }
}

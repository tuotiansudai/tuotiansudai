package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.request.WithdrawRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.dto.response.WithdrawContentDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public WithdrawService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public WithdrawRequestDto withdraw(Source source, String loginName, String mobile, String userName, String accountNo, String amount) {
        WithdrawRequestDto dto = new WithdrawRequestDto(source, loginName, mobile, userName, accountNo, amount);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[withdraw] sign error, userName: {}, accountNo: {}, amount: {}", userName, accountNo, amount);
            return null;
        }

        insertMapper.insertWithdraw(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[withdraw callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.WITHDRAW.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[withdraw callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateWithdraw(responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.WITHDRAW.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<WithdrawContentDto> responseDto = (ResponseDto<WithdrawContentDto>) ApiType.WITHDRAW.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}

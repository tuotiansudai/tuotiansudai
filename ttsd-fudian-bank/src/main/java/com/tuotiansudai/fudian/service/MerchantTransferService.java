package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankMerchantTransferDto;
import com.tuotiansudai.fudian.dto.request.MerchantTransferRequestDto;
import com.tuotiansudai.fudian.dto.response.MerchantTransferContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankMerchantTransferMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantTransferService {

    private static Logger logger = LoggerFactory.getLogger(MerchantTransferService.class);

    private static final ApiType API_TYPE = ApiType.MERCHANT_TRANSFER;

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public MerchantTransferService(BankClient bankClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankMerchantTransferMessage transfer(BankMerchantTransferDto bankMerchantTransferDto) {
        MerchantTransferRequestDto dto = new MerchantTransferRequestDto(bankMerchantTransferDto.getLoginName(),
                bankMerchantTransferDto.getMobile(),
                bankMerchantTransferDto.getBankUserName(),
                bankMerchantTransferDto.getBankAccountNo(),
                AmountUtils.toYuan(bankMerchantTransferDto.getAmount()));

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant transfer] sign error, date: {}", bankMerchantTransferDto);
            return new BankMerchantTransferMessage(false, "签名失败");
        }

        insertMapper.insertMerchantTransfer(dto);

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());
        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[merchant transfer] verify sign error, response: {}", responseData);
            return new BankMerchantTransferMessage(false, "验签失败");
        }

        ResponseDto<MerchantTransferContentDto> responseDto = (ResponseDto<MerchantTransferContentDto>) API_TYPE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[merchant transfer] parse response error, response: {}", responseData);
            return new BankMerchantTransferMessage(false, "银行数据解析失败");
        }

        this.updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        if (responseDto.isSuccess() && responseDto.getContent().isSuccess()) {
            new BankMerchantTransferMessage(responseDto.getContent().getOrderNo(), responseDto.getContent().getOrderDate());
        }

        return new BankMerchantTransferMessage(false, responseDto.getRetMsg());
    }
}

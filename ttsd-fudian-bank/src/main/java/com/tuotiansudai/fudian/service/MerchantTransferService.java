package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.MerchantTransferRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantTransferService {

    private static Logger logger = LoggerFactory.getLogger(MerchantTransferService.class);

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

    public ResponseDto transfer(String userName, String accountNo, String amount) {
        MerchantTransferRequestDto dto = new MerchantTransferRequestDto(userName, accountNo, amount);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant transfer] sign error, userName: {}, accountNo: {}, amount: {}", userName, accountNo, amount);
            return null;
        }

        insertMapper.insertMerchantTransfer(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.MERCHANT_TRANSFER);
        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[merchant transfer] send error, userName: {}, accountNo: {}, amount: {}", userName, accountNo, amount);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[merchant transfer] verify sign error, userName: {}, accountNo: {}, amount: {}", userName, accountNo, amount);
            return null;
        }

        ResponseDto responseDto = ApiType.MERCHANT_TRANSFER.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[merchant transfer] parse response error, userName: {}, accountNo: {}, amount: {}", userName, accountNo, amount);
            return null;
        }

        this.updateMapper.updateMerchantTransfer(responseDto);
        return responseDto;
    }
}

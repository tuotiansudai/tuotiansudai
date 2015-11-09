package com.ttsd.api.service;

import com.esoft.jdp2p.bankcard.model.BankCard;
import com.ttsd.api.dto.BankCardReplaceRequestDto;
import com.ttsd.api.dto.BaseResponseDto;

/**
 * Created by tuotian on 15/8/7.
 */
public interface MobileAppBankCardService {
    /**
     * @function 查询绑卡状态
     * @param userId 绑卡或签约用户ID
     * @return boolean
     */
    boolean queryBindAndSginStatus(String userId,String operationType);

    BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto);

    void save(BankCard bankCard);
}

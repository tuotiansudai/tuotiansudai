package com.tuotiansudai.service;

import com.tuotiansudai.dto.ReplaceBankCardDto;

import java.util.List;


public interface BandCardManagerService {

    int queryCountReplaceBankCardRecord(String mobile);

    List<ReplaceBankCardDto> queryReplaceBankCardRecord(String mobile, int index, int pageSize);

    void updateRemark(long bankCardId,String remark);

    String findRemarkByBankCardId(long bankCardId);

    void stopBankCard(long bankCardId);
}

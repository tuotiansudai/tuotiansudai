package com.tuotiansudai.service;

import com.tuotiansudai.dto.ReplaceBankCardDto;

import java.util.List;


public interface BandCardManagerService {

    int queryCountReplaceBankCardRecord(String loginName, String mobile);

    List<ReplaceBankCardDto> queryReplaceBankCardRecord(String loginName, String mobile, int index, int pageSize);

    void updateRemark(long bankCardId, String remark);

    String updateBankCard(String loginName, long bankCardId, String ip);
}

package com.tuotiansudai.service;

import com.tuotiansudai.dto.ReplaceBankCardDto;

import java.util.List;


public interface BandCardManagerService {

    int queryCountReplaceBankCard(String loginName, String mobile);

    List<ReplaceBankCardDto> queryReplaceBankCard(String loginName, String mobile, int index, int pageSize);

    void updateRemark(long bankCardId, String remark);

    String updateBankCard(String loginName, long bankCardId, String ip);
}

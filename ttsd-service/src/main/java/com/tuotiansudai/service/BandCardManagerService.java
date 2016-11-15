package com.tuotiansudai.service;

import com.tuotiansudai.dto.ReplaceBankCardDto;

import java.util.List;


public interface BandCardManagerService {

    int queryCountReplaceBankCardRecord(String loginName);

    List<ReplaceBankCardDto> queryReplaceBankCardRecord(String loginName, int index, int pageSize);
}

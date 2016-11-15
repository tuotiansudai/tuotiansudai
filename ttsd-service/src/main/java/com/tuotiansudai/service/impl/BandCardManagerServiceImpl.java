package com.tuotiansudai.service.impl;


import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.ReplaceBankCardDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BandCardManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class BandCardManagerServiceImpl implements BandCardManagerService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int queryCountReplaceBankCardRecord(String loginName) {
        return bankCardMapper.findCountReplaceBankCardByLoginName(loginName);
    }

    @Override
    public List<ReplaceBankCardDto> queryReplaceBankCardRecord(String loginName, int index, int pageSize) {
        List<BankCardModel> replaceBankCards = bankCardMapper.findReplaceBankCardByLoginName(loginName, index, pageSize);

        Iterator<ReplaceBankCardDto> replaceBankCardDtoIterator = Iterators.transform(replaceBankCards.iterator(), input -> {
            String mobile = userMapper.findByLoginName(input.getLoginName()).getMobile();
            BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(input.getLoginName());
            return new ReplaceBankCardDto(input.getId(), input.getLoginName(), mobile, bankCardModel != null ? bankCardModel.getBankCode() : "",
                    bankCardModel != null ? bankCardModel.getCardNumber() : "", input.getBankCode(), input.getCardNumber(), input.getCreatedTime(), input.getStatus(), "");
        });

        return Lists.newArrayList(replaceBankCardDtoIterator);
    }
}

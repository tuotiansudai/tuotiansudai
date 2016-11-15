package com.tuotiansudai.service.impl;


import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ReplaceBankCardDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.BandCardManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

@Service
public class BandCardManagerServiceImpl implements BandCardManagerService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static String BANK_CARD_REMARK_TEMPLATE = "bank_card_remark_id:{0}";

    private static int REMARK_LIFE_TIME = 60 * 60 * 24 * 30 * 6;

    @Override
    public int queryCountReplaceBankCardRecord(String mobile) {
        UserModel userModel = userMapper.findByMobile(mobile);
        return bankCardMapper.findCountReplaceBankCardByLoginName(userModel == null ? null : userModel.getLoginName());
    }

    @Override
    public List<ReplaceBankCardDto> queryReplaceBankCardRecord(String mobile, int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        List<BankCardModel> replaceBankCards = bankCardMapper.findReplaceBankCardByLoginName(userModel == null ? null : userModel.getLoginName(), index, pageSize);

        Iterator<ReplaceBankCardDto> replaceBankCardDtoIterator = Iterators.transform(replaceBankCards.iterator(), input -> {
            UserModel userModelByLoginName = userMapper.findByLoginName(input.getLoginName());
            BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(input.getLoginName());
            return new ReplaceBankCardDto(input.getId(), input.getLoginName(), userModelByLoginName.getUserName(), userModelByLoginName.getMobile(), bankCardModel != null ? bankCardModel.getBankCode() : "",
                    bankCardModel != null ? bankCardModel.getCardNumber() : "", input.getBankCode(), input.getCardNumber(), input.getCreatedTime(), input.getStatus(), redisWrapperClient.get(MessageFormat.format(this.BANK_CARD_REMARK_TEMPLATE, input.getId())));
        });

        return Lists.newArrayList(replaceBankCardDtoIterator);
    }

    @Override
    public void updateRemark(long bankCardId,String remark) {
        String key = MessageFormat.format(this.BANK_CARD_REMARK_TEMPLATE, bankCardId);
        if (redisWrapperClient.exists(key)) {
            redisWrapperClient.setex(key, REMARK_LIFE_TIME, remark);
        }else{
            String value = redisWrapperClient.get(key);
            redisWrapperClient.setex(key, REMARK_LIFE_TIME, value + ";" + remark);
        }
    }

    @Override
    public String findRemarkByBankCardId(long bankCardId) {
        String key = MessageFormat.format(this.BANK_CARD_REMARK_TEMPLATE, bankCardId);

        return redisWrapperClient.get(key);
    }
}

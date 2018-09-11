package com.tuotiansudai.service.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.ReplaceBankCardDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BandCardManagerService;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class BandCardManagerServiceImpl implements BandCardManagerService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserMapper userMapper;

    private static String BANK_CARD_REMARK_TEMPLATE = "bank_card_remark_id:{0}";

    public final String BAND_CARD_ACTIVE_STATUS_TEMPLATE = "bank_card_active_status";

    private final String BAND_CARD_IN_VERIFY = "inRecheck";

    private final String BAND_CARD_ON_VERIFY = "noRecheck";

    private static int REMARK_LIFE_TIME = 60 * 60 * 24 * 30 * 6;

    @Override
    public int queryCountReplaceBankCard(String loginName, String mobile) {
        UserModel userModel = userMapper.findByMobile(mobile);
        if(!Strings.isNullOrEmpty(mobile) && userModel == null)
            return 0;

        return bankCardMapper.findCountReplaceBankCardByLoginName(userModel == null ? null : userModel.getLoginName());
    }

    @Override
    public List<ReplaceBankCardDto> queryReplaceBankCard(String loginName, String mobile, int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        if(!Strings.isNullOrEmpty(mobile) && userModel == null)
            return Lists.newArrayList();

        List<BankCardModel> replaceBankCards = bankCardMapper.findReplaceBankCardByLoginName(userModel == null ? null : userModel.getLoginName(), index, pageSize);
        Map<String, String> bandCardIdMap = redisWrapperClient.exists(BAND_CARD_ACTIVE_STATUS_TEMPLATE) ? redisWrapperClient.hgetAll(BAND_CARD_ACTIVE_STATUS_TEMPLATE) : Maps.newConcurrentMap();

        Iterator<ReplaceBankCardDto> replaceBankCardDtoIterator = Iterators.transform(replaceBankCards.iterator(), input -> {
            UserModel userModelByLoginName = userMapper.findByLoginName(input.getLoginName());
            BankCardModel bankCardModel = null;
            if(input.getStatus().equals(BankCardStatus.PASSED)){
                bankCardModel = bankCardMapper.findReplaceBeforeCardByLoginName(input.getLoginName());
            }else{
                bankCardModel = bankCardMapper.findPassedBankCardByLoginName(input.getLoginName());
            }

            return new ReplaceBankCardDto(input.getId(), input.getLoginName(), userModelByLoginName.getUserName(), userModelByLoginName.getMobile(), bankCardModel != null ? bankCardModel.getBankCode() : "",
                    bankCardModel != null ? bankCardModel.getCardNumber() : "", input.getBankCode(), input.getCardNumber(), input.getCreatedTime(), input.getStatus(),
                    redisWrapperClient.get(MessageFormat.format(this.BANK_CARD_REMARK_TEMPLATE, String.valueOf(input.getId()))),
                    bandCardIdMap.get(String.valueOf(input.getId())) != null ? BAND_CARD_IN_VERIFY : BAND_CARD_ON_VERIFY);
        });

        return Lists.newArrayList(replaceBankCardDtoIterator);
    }

    @Override
    public void updateRemark(long bankCardId, String remark) {
        String key = MessageFormat.format(this.BANK_CARD_REMARK_TEMPLATE, String.valueOf(bankCardId));
        if (!redisWrapperClient.exists(key)) {
            redisWrapperClient.setex(key, REMARK_LIFE_TIME, remark);
        } else {
            String value = redisWrapperClient.get(key);
            redisWrapperClient.setex(key, REMARK_LIFE_TIME, value + "|" + remark);
        }
    }


    @Override
    public String updateBankCard(String loginName, long bankCardId, String ip) {
        BankCardModel bankCardModel = bankCardMapper.findById(bankCardId);
        bankCardMapper.updateStatusAndBankCode(bankCardId, BankCardStatus.REJECT, bankCardModel.getBankCode());
        return "审核成功!";
    }

}

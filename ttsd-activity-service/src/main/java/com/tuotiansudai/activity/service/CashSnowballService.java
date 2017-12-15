package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.repository.model.ActivityAmountGrade;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.MobileEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CashSnowballService {

    @Autowired
    private CashSnowballActivityMapper cashSnowballActivityMapper;

    public List<CashSnowballActivityModel> findAll() {
        List<CashSnowballActivityModel> cashSnowballActivityModelList = cashSnowballActivityMapper.findAll(null, null, null);
        cashSnowballActivityModelList.stream().forEach(cashSnowballActivityModel -> cashSnowballActivityModel.setMobile(MobileEncryptor.encryptMiddleMobile(cashSnowballActivityModel.getMobile())));
        return cashSnowballActivityModelList.size() > 20 ? cashSnowballActivityModelList.subList(0, 20) : cashSnowballActivityModelList;
    }

    public Map<String, String> userInvestAmount(String loginName) {
        CashSnowballActivityModel cashSnowballActivityModel = loginName == null ? null : cashSnowballActivityMapper.findByLoginName(loginName);
        long annualizedAmount = cashSnowballActivityModel == null ? 0l : cashSnowballActivityModel.getAnnualizedAmount();
        long cashAmount = cashSnowballActivityModel == null ? 0l : cashSnowballActivityModel.getCashAmount();
        long nextAmount = ((annualizedAmount / 1000000l + 1) * 1000000l) - annualizedAmount;

        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("annualizedAmount", AmountConverter.convertCentToString(annualizedAmount))
                .put("cashAmount", String.valueOf(cashAmount / 100))
                .put("nextAmount", AmountConverter.convertCentToString(nextAmount))
                .put("againObtainCash", String.valueOf(ActivityAmountGrade.getAwardAmount("CASH_SNOWBALL", annualizedAmount)))
                .build());
    }
}

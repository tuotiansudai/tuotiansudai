package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.util.MobileEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CashSnowballService {

    @Autowired
    private CashSnowballActivityMapper cashSnowballActivityMapper;

    public List<CashSnowballActivityModel> findAll() {
        List<CashSnowballActivityModel> cashSnowballActivityModelList = cashSnowballActivityMapper.findAll();
        cashSnowballActivityModelList.stream().forEach(cashSnowballActivityModel -> cashSnowballActivityModel.setLoginName(MobileEncryptor.encryptMiddleMobile(cashSnowballActivityModel.getLoginName())));
        return cashSnowballActivityModelList.size() > 20 ? cashSnowballActivityModelList.subList(0, 20) : cashSnowballActivityModelList;
    }

//    public Map<String, String> userInvestAmount(String loginName) {
//        Map<String, String> map = new HashMap<>();
//        CashSnowballActivityModel cashSnowballActivityModel = loginName == null ? null : cashSnowballActivityMapper.findByLoginName(loginName);
//        long annualizedAmount = cashSnowballActivityModel == null ? 0l : cashSnowballActivityModel.getAnnualizedAmount();
//    }
}

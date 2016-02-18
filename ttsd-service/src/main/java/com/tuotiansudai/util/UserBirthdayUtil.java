package com.tuotiansudai.util;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBirthdayUtil {

    @Autowired
    private AccountMapper accountMapper;

    public boolean isBirthMonth(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            return false;
        }

        String identityNumber = accountModel.getIdentityNumber();

        int birthMonth = Integer.parseInt(identityNumber.substring(10, 12));

        int monthOfYear = new DateTime().getMonthOfYear();

        return monthOfYear == birthMonth;
    }

}

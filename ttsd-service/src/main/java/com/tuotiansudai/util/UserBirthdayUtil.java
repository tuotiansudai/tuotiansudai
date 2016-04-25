package com.tuotiansudai.util;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserBirthdayUtil {

    @Autowired
    private AccountMapper accountMapper;

    public boolean isBirthMonth(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null || Strings.isNullOrEmpty(accountModel.getIdentityNumber())) {
            return false;
        }

        String identityNumber = accountModel.getIdentityNumber();

        int birthMonth = Integer.parseInt(identityNumber.length() == 18 ? identityNumber.substring(10, 12) : identityNumber.substring(8, 10));

        int monthOfYear = new DateTime().getMonthOfYear();

        return monthOfYear == birthMonth;
    }

    public DateTime getUserBirthday(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null || Strings.isNullOrEmpty(accountModel.getIdentityNumber())) {
            return null;
        }

        String identityNumber = accountModel.getIdentityNumber();

        return identityNumber.length() == 18 ?
                new DateTime().withDate(Integer.parseInt(identityNumber.substring(6, 10)),
                        Integer.parseInt(identityNumber.substring(10, 12)),
                        Integer.parseInt(identityNumber.substring(12, 14))).withTimeAtStartOfDay() :
                new DateTime().withDate(Integer.parseInt(MessageFormat.format("19{0}", identityNumber.substring(6, 8))),
                        Integer.parseInt(identityNumber.substring(8, 10)),
                        Integer.parseInt(identityNumber.substring(10, 12))).withTimeAtStartOfDay();
    }
}

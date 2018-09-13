package com.tuotiansudai.borrow.service;

import com.tuotiansudai.borrow.dto.response.AuthenticationResponseDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    public AuthenticationResponseDto isAuthentication(String mobile) {
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return new AuthenticationResponseDto(true, false, false, false, false);
        }
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        return new AuthenticationResponseDto(true,
                accountModel != null,
                bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName()) != null,
                accountModel != null && accountModel.isAutoRepay(),
                anxinSignPropertyMapper.findByLoginName(userModel.getLoginName()) != null);
    }
}

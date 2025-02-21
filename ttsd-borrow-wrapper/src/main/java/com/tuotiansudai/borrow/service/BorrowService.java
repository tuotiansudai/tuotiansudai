package com.tuotiansudai.borrow.service;

import com.tuotiansudai.borrow.dto.response.AuthenticationResponseDto;
import com.tuotiansudai.borrow.dto.response.BaseResponseDto;
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

    public BaseResponseDto<AuthenticationResponseDto> isAuthentication(String mobile) {
        BaseResponseDto<AuthenticationResponseDto> baseResponseDto = new BaseResponseDto<>(true);
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            baseResponseDto.setData(new AuthenticationResponseDto());
            return baseResponseDto;
        }
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        baseResponseDto.setData(new AuthenticationResponseDto(accountModel != null,
                bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName()) != null,
                accountModel != null && accountModel.isAutoRepay(),
                anxinSignPropertyMapper.findByLoginName(userModel.getLoginName()) != null));
        return baseResponseDto;
    }
}

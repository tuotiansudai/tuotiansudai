package com.tuotiansudai.borrow.service;

import com.tuotiansudai.borrow.dto.response.BaseResponseDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {

    @Autowired
    private AccountMapper accountMapper;

    public BaseResponseDto findAccountByLoginName(String loginName){
        return new BaseResponseDto(accountMapper.findByLoginName(loginName) == null);
    }
}

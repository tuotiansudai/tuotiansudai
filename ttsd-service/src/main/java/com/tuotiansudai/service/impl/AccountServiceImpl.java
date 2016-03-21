package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;


@Service
public class AccountServiceImpl implements AccountService {

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    public AccountModel findByLoginName(String loginName) {
        return accountMapper.findByLoginName(loginName);
    }

    public long getBalance(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getBalance() : 0;
    }

    @Override
    public long getFreeze(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getFreeze() : 0;
    }

    @Override
    public boolean isIdentityNumberExist(String identityNumber) {
        AccountModel accountModel = accountMapper.findByIdentityNumber(identityNumber);
        return accountModel != null;
    }

     @Override
    public List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, int currentPageNo, int pageSize){
        List<AccountModel> accountModels =  accountMapper.findUsersAccountPoint(loginName, userName, mobile, (currentPageNo - 1) * pageSize, pageSize);

        List<AccountItemDataDto> accountItemDataDtoList = new ArrayList<>();
        for(AccountModel accountModel : accountModels) {
            AccountItemDataDto accountItemDataDto = new AccountItemDataDto(accountModel);
            accountItemDataDto.setTotalPoint(accountMapper.findUsersAccountTotalPoint(accountModel.getLoginName()));
            accountItemDataDto.setMobile(userMapper.findUsersMobileByLoginName(accountModel.getLoginName()));
            accountItemDataDtoList.add(accountItemDataDto);
        }
        return accountItemDataDtoList;
    }

    @Override
    public int findUsersAccountPointCount(String loginName, String userName, String mobile){
        return accountMapper.findUsersAccountPointCount(loginName, userName, mobile);
    }

    @Override
    public String getRealName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel == null ? loginName : accountModel.getUserName();
    }

}

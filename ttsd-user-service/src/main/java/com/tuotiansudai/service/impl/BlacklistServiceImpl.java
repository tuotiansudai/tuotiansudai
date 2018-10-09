package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.BlacklistMapper;
import com.tuotiansudai.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Override
    public boolean userIsInBlacklist(String loginName) {
        return blacklistMapper.userIsInBlacklist(loginName);
    }
}

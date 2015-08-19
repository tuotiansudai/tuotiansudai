package com.tuotiansudai.security;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    static Logger logger = Logger.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userMapper.findByLoginNameOrMobile(username);
        if (userModel == null) {
            String errorMessage = MessageFormat.format("Login Error: {0} not found!", username);
            throw new UsernameNotFoundException(errorMessage);
        }

        String loginName = userModel.getLoginName();
        String mobile = userModel.getMobile();
        String password = userModel.getPassword();
        boolean enabled = userModel.isActive();
        String salt = userModel.getSalt();

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        String umpUserId = accountModel != null ? accountModel.getPayUserId() : null;

        List<GrantedAuthority> authorities = Lists.newArrayList();

        return new MyUser(loginName, password, enabled, true, true, true, authorities, mobile, umpUserId, salt);
    }
}

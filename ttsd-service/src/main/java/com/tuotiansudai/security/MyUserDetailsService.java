package com.tuotiansudai.security;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    static Logger logger = Logger.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value("${web.login.max.failed.times}")
    private int times;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {
        UserModel userModel = userMapper.findByLoginNameOrMobile(username);
        if (userModel == null) {
            String errorMessage = MessageFormat.format("Login Error: {0} not found!", username);
            throw new UsernameNotFoundException(errorMessage);
        }
        if (verifyLoginFailedMaxTimes(username)) {
            userModel.setStatus(UserStatus.ACTIVE);
            userMapper.updateUser(userModel);
        }
        String loginName = userModel.getLoginName();
        String mobile = userModel.getMobile();
        String password = userModel.getPassword();
        boolean enabled = userModel.isActive();

        String salt = userModel.getSalt();

        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);

        List<GrantedAuthority> grantedAuthorities = Lists.transform(userRoleModels, new Function<UserRoleModel, GrantedAuthority>() {
            @Override
            public GrantedAuthority apply(UserRoleModel userRoleModel) {
                return new SimpleGrantedAuthority(userRoleModel.getRole().name());
            }
        });

        return new MyUser(loginName, password, enabled, true, true, true, grantedAuthorities, mobile, salt);
    }

    private boolean verifyLoginFailedMaxTimes(String loginName) {
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        return !(redisWrapperClient.exists(redisKey) && Integer.parseInt(redisWrapperClient.get(redisKey)) == times);
    }
}

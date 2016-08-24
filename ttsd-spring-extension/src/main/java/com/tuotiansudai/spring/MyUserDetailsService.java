package com.tuotiansudai.spring;

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

import java.text.MessageFormat;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    static Logger logger = Logger.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${web.login.max.failed.times}")
    private int times;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException, DisabledException {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Login Error: {0} not found!", loginName));
        }

        String mobile = userModel.getMobile();
        String password = userModel.getPassword();
        String salt = userModel.getSalt();

        if (verifyLoginFailedMaxTimes(loginName)) {
            userModel.setStatus(UserStatus.ACTIVE);
            userMapper.updateUser(userModel);
        }

        boolean enabled = userModel.getStatus() == UserStatus.ACTIVE;

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

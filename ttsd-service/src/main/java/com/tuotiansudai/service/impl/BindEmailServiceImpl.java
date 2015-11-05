package com.tuotiansudai.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.BindEmailService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.SendCloudMailUtil;
import com.tuotiansudai.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class BindEmailServiceImpl implements BindEmailService {
    static Logger logger = Logger.getLogger(BindEmailServiceImpl.class);
    @Autowired
    private SendCloudMailUtil sendCloudMailUtil;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserMapper userMapper;


    @Value("${web.email.verify.host}")
    private String host;

    @Value("${web.email.verify.port}")
    private String port;

    private final static String ACTIVE_URL_TEMPLATE = "http://{host}:{port}/bind-email/verify/{uuid}";

    @Override
    public boolean sendActiveEmail(String email, String url) {
        if (StringUtils.isNotEmpty(email)) {
            String loginName = LoginUserInfo.getLoginName();
            String uuid = UUIDGenerator.generate();
            String bindEmailKey = "web:{loginName}:{uuid}";
            String bindEmailValue = "{loginName}:{email}";
            String activeUrl = ACTIVE_URL_TEMPLATE.replace("{host}",host).replace("{port}",port).replace("{uuid}",uuid);

            redisWrapperClient.setex(bindEmailKey.replace("{loginName}", loginName).replace("{uuid}", uuid),
                    86400,
                    bindEmailValue.replace("{loginName}", loginName).replace("{email}", email));

            Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                    .put("loginName", loginName)
                    .put("activeUrl", activeUrl)
                    .build());
            return sendCloudMailUtil.sendActiveEmail(email, emailParameters);
        }

        return false;
    }

    @Override
    @Transactional
    public String verifyEmail(String uuid) {
        if(StringUtils.isEmpty(uuid)){
            return "";
        }
        String bindEmailKeyTemplate = "web:{loginName}:{uuid}";
        String loginName = LoginUserInfo.getLoginName();
        String bindEmailKey = bindEmailKeyTemplate.replace("{loginName}", loginName).replace("{uuid}", uuid);
        String bindEmailValue = redisWrapperClient.get(bindEmailKey);
        if(StringUtils.isEmpty(bindEmailValue)){
            logger.debug(bindEmailKey + "绑定邮箱链接已经过期!");
            return "";
        }
        String[] loginNameAndEmail = bindEmailValue.split(":");
        if(!loginName.equals(loginNameAndEmail[0])){
            logger.debug("bindEmailKey=" + bindEmailKey+ ",bindEmailValue="+ bindEmailValue + "!");
            return "test|" + loginName;
        }
        UserModel userModelEmail = userMapper.findByEmail(loginNameAndEmail[1]);
        if(userModelEmail != null){
            return "";
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
        userModel.setEmail(loginNameAndEmail[1]);
        userMapper.updateUser(userModel);
        redisWrapperClient.del(bindEmailKey);

        return loginNameAndEmail[1];
    }

}

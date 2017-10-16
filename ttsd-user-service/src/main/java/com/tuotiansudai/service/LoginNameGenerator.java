package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoginNameGenerator {

    private final static char[] ALPHABET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final static Random random = new Random();

    private final WeChatUserMapper weChatUserMapper;

    @Autowired
    public LoginNameGenerator(WeChatUserMapper weChatUserMapper) {
        this.weChatUserMapper = weChatUserMapper;
    }

    public String generateWxLoginName() {
        String loginName = generateLoginName();
        while (CollectionUtils.isNotEmpty(weChatUserMapper.findByLoginName(loginName))) {
            loginName = generateLoginName();
        }
        return loginName;
    }

    private String generateLoginName() {
        StringBuilder loginNameBuilder = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            loginNameBuilder.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }

        return loginNameBuilder.toString();
    }
}

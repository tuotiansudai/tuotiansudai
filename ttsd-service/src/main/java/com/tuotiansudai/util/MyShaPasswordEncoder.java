package com.tuotiansudai.util;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyShaPasswordEncoder extends ShaPasswordEncoder {

    @Override
    public String encodePassword(String rawPass, Object salt) {
        String encodePassword = super.encodePassword(rawPass, null);
        return super.encodePassword(encodePassword, salt);
    }

    public String generateSalt() {
        return UUIDGenerator.generate();
    }
}

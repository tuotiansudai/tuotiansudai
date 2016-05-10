package com.tuotiansudai.web.utils;


import com.tuotiansudai.web.util.IdentityNumberValidator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IdcardUtilsTest {

    @Test
    public void validateIdCard(){
        assertTrue(IdentityNumberValidator.validateIdentity("370405199112286014"));
        assertFalse(IdentityNumberValidator.validateIdentity("370405199112286015"));
        assertFalse(IdentityNumberValidator.validateIdentity("370405189112286015"));
        assertFalse(IdentityNumberValidator.validateIdentity("370405199212286015"));
    }
}

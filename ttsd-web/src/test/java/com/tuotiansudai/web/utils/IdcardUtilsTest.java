package com.tuotiansudai.web.utils;


import com.tuotiansudai.web.util.IdcardUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IdcardUtilsTest {

    @Test
    public void validateIdCard(){
        assertTrue(IdcardUtils.validateIdentity("370405199112286014"));
        assertFalse(IdcardUtils.validateIdentity("370405199112286015"));
        assertFalse(IdcardUtils.validateIdentity("370405189112286015"));
        assertFalse(IdcardUtils.validateIdentity("370405199212286015"));
    }
}

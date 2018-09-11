package com.tuotiansudai.util;

import org.joda.time.DateTime;
import org.junit.Test;

public class IdGeneratorTest {

    @Test
    public void test() {
        System.out.println(IdGenerator.generate());

        System.out.println(Long.MAX_VALUE);

        System.out.println(new DateTime(2035, 12, 31, 0, 0, 0).getMillis());
//            101987471593360
//
//        9223372036854775807
    }
}

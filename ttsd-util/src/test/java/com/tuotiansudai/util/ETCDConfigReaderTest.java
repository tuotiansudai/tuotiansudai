package com.tuotiansudai.util;

import org.junit.Test;

public class ETCDConfigReaderTest {

    @Test
    public void name() throws Exception {
        System.out.println(ETCDConfigReader.getValue("common.environment"));
    }
}

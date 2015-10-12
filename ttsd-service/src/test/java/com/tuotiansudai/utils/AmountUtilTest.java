package com.tuotiansudai.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AmountUtilTest {

    @Test
    public void shouldConvertStringToCent() throws Exception {
        assertThat(AmountUtil.convertStringToCent("NaN"), is(0L));
        assertThat(AmountUtil.convertStringToCent("0"), is(0L));
        assertThat(AmountUtil.convertStringToCent("1"), is(100L));
        assertThat(AmountUtil.convertStringToCent("0.1"), is(10L));
        assertThat(AmountUtil.convertStringToCent("0.10"), is(10L));
        assertThat(AmountUtil.convertStringToCent("0.01"), is(1L));
        assertThat(AmountUtil.convertStringToCent("1.11"), is(111L));
    }

    @Test
    public void shouldConvertCentToString() throws Exception {
        assertThat(AmountUtil.convertCentToString(0), is("0.00"));
        assertThat(AmountUtil.convertCentToString(1), is("0.01"));
        assertThat(AmountUtil.convertCentToString(10), is("0.10"));
        assertThat(AmountUtil.convertCentToString(100), is("1.00"));
        assertThat(AmountUtil.convertCentToString(111), is("1.11"));
    }
}

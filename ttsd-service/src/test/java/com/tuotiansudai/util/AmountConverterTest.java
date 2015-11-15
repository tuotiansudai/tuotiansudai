package com.tuotiansudai.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AmountConverterTest {

    @Test
    public void shouldConvertStringToCent() throws Exception {
        assertThat(AmountConverter.convertStringToCent("NaN"), is(0L));
        assertThat(AmountConverter.convertStringToCent("0"), is(0L));
        assertThat(AmountConverter.convertStringToCent("1"), is(100L));
        assertThat(AmountConverter.convertStringToCent("0.1"), is(10L));
        assertThat(AmountConverter.convertStringToCent("0.10"), is(10L));
        assertThat(AmountConverter.convertStringToCent("0.01"), is(1L));
        assertThat(AmountConverter.convertStringToCent("1.11"), is(111L));
    }

    @Test
    public void shouldConvertCentToString() throws Exception {
        assertThat(AmountConverter.convertCentToString(0), is("0.00"));
        assertThat(AmountConverter.convertCentToString(1), is("0.01"));
        assertThat(AmountConverter.convertCentToString(10), is("0.10"));
        assertThat(AmountConverter.convertCentToString(100), is("1.00"));
        assertThat(AmountConverter.convertCentToString(111), is("1.11"));
    }
}

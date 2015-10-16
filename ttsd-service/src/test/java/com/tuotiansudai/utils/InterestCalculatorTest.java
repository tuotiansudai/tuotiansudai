package com.tuotiansudai.utils;

import com.tuotiansudai.repository.model.LoanModel;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InterestCalculatorTest {

    @Test
    public void shouldCalculateInterest() throws Exception {
        LoanModel loanModel = new LoanModel();
        loanModel.setBaseRate(0.09);
        loanModel.setActivityRate(0.01);
        loanModel.setRecheckTime(new DateTime().withDate(2015, 1, 1).withTimeAtStartOfDay().toDate());

        assertThat(InterestCalculator.calculateInterest(loanModel, 2000L), is(0L));
        assertThat(InterestCalculator.calculateInterest(loanModel, 4000L), is(1L));
    }
}

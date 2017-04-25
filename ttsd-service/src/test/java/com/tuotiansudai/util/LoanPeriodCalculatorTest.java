package com.tuotiansudai.util;

import com.tuotiansudai.repository.model.LoanType;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LoanPeriodCalculatorTest {

    @Test
    public void shouldReturnZeroWhenRecheckTimeIsNullOrDeadlineIsNullOrRecheckTimeIsAfterDeadline() throws Exception {
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(null, new Date(), LoanType.INVEST_INTEREST_LUMP_SUM_REPAY), is(0));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(new Date(), null, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY), is(0));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(new Date(),
                Date.from(LocalDateTime.now().minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_LUMP_SUM_REPAY), is(0));
    }

    @Test
    public void shouldReturnOneWhenPeriodTypeIsDay() throws Exception {
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(100).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_LUMP_SUM_REPAY),
                is(1));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(100).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_LUMP_SUM_REPAY),
                is(1));
    }

    @Test
    public void shouldReturnOneWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIsLessThan30Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY),
                is(1));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY),
                is(1));
    }

    @Test
    public void shouldReturnOneWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIs30Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY),
                is(1));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY),
                is(1));
    }

    @Test
    public void shouldReturnTwoWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIs31Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY),
                is(2));
        assertThat(LoanPeriodCalculator.calculateLoanPeriods(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY),
                is(2));
    }

    @Test
    public void shouldReturnEmptyWhenRecheckTimeIsNullOrDeadlineIsNullOrRecheckTimeIsAfterDeadline() throws Exception {
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(null, new Date(), LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).size(), is(0));
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(new Date(), null, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).size(), is(0));
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(new Date(),
                Date.from(LocalDateTime.now().minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).size(), is(0));
    }

    @Test
    public void shouldReturnOnePeriodWhenPeriodTypeIsDay() throws Exception {
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(99).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_LUMP_SUM_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(99).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_LUMP_SUM_REPAY).get(0),
                is(100));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(99).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(99).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).get(0),
                is(100));
    }

    @Test
    public void shouldReturnOnePeriodWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIsLessThan30Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).get(0),
                is(29));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(28).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).get(0),
                is(29));
    }

    @Test
    public void shouldReturnOnePeriodWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIs30Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).get(0),
                is(30));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).size(),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).get(0),
                is(30));
    }

    @Test
    public void shouldReturnOnePeriodWhenPeriodTypeIsMonthAndDurationBetweenRecheckTimeAndDeadlineIs31Days() throws Exception {
        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).size(),
                is(2));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).get(0),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.LOAN_INTEREST_MONTHLY_REPAY).get(1),
                is(30));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).size(),
                is(2));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).get(0),
                is(1));

        assertThat(LoanPeriodCalculator.calculateDaysOfPerPeriod(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()),
                LoanType.INVEST_INTEREST_MONTHLY_REPAY).get(1),
                is(30));
    }


}

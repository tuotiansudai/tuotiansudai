package com.tuotiansudai.service;

import java.util.Date;

public interface ExperienceRepayService {
    /**
     *
     * @param compareDate 还款时间比较点，应保证是new Date()
     * @param repayDate 实际还款时间，一般是new Date()
     */
    void repay(Date compareDate, Date repayDate);
}

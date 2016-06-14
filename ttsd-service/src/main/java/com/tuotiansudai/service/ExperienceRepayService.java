package com.tuotiansudai.service;

import java.util.Date;

public interface ExperienceRepayService {
    /**
     *
     * @param repayDate 实际还款时间，一般是new Date()
     */
    void repay(Date repayDate);
}

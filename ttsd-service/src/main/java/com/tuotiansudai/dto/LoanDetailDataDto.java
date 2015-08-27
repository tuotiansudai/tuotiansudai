package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;

import java.util.Date;

public class LoanDetailDataDto extends BaseDataDto{
    private long id;

    private String name;

    private String agentLoginName;

    private String loanerLoginName;

    private LoanType type;

    private String periods;

    private String descriptionText;

    private String descriptionHtml;

    private long loanAmount;

    private long minInvestAmount;

    private long investIncreasingAmount;

    private long maxInvestAmount;

    private ActivityType activityType;

    private double activityRate;

    private double basicRate;

    private Date createdTime;

    private LoanStatus status;

    private long needRaisedAmount;

    private long balance;

    private long expectedIncome;


}

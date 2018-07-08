package com.tuotiansudai.dto.query;

import com.tuotiansudai.repository.model.FundPlatform;
import com.tuotiansudai.repository.model.LoanStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qduljs2011 on 2018/7/6.
 */
public class LoanQueryDto extends PageUtilDto implements Serializable {

   private LoanStatus status;

   private FundPlatform fundPlatform;

   private Long loanId;

   private Date startTime;

   private Date endTime;

   private String loanName;


   public LoanStatus getStatus() {
      return status;
   }

   public void setStatus(LoanStatus status) {
      this.status = status;
   }

   public FundPlatform getFundPlatform() {
      return fundPlatform;
   }

   public void setFundPlatform(FundPlatform fundPlatform) {
      this.fundPlatform = fundPlatform;
   }

   public Long getLoanId() {
      return loanId;
   }

   public void setLoanId(Long loanId) {
      this.loanId = loanId;
   }

   public Date getStartTime() {
      return startTime;
   }

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
   }

   public Date getEndTime() {
      return endTime;
   }

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   public String getLoanName() {
      return loanName;
   }

   public void setLoanName(String loanName) {
      this.loanName = loanName;
   }
}

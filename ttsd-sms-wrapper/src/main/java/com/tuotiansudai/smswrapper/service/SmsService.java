package com.tuotiansudai.smswrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.dto.sms.LoanRaisingCompleteNotifyDto;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;

import java.util.List;

public interface SmsService {

    BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto);

    BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, String ip);

    BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile);

    BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify);

    BaseDto<SmsDataDto> loanRepayNotify(String mobile, String repayAmount);

    BaseDto<SmsDataDto> couponNotify(SmsCouponNotifyDto notifyDto);

    BaseDto<SmsDataDto> birthdayNotify(SmsCouponNotifyDto notifyDto);

    BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String captcha, String ip) ;

    BaseDto<SmsDataDto> experienceRepayNotify(List<String> mobiles, String repayAmount);

    BaseDto<SmsDataDto> cancelTransferLoan(String mobile, String TransferLoanName);

    BaseDto<SmsDataDto> importUserGetGiveMembership(String mobile, int level);

    BaseDto<SmsDataDto> newUserGetGiveMembership(String mobile, int level);

    BaseDto<SmsDataDto> couponNotifyByMd(SmsCouponNotifyDto notifyDto);

    BaseDto<SmsDataDto> sendRegisterCaptchaByMd(String mobile, String captcha, String ip);
    
    BaseDto<SmsDataDto> platformBalanceLowNotify(List<String> mobiles, String warningValue);

    BaseDto<SmsDataDto> generateContractNotify(List<String> mobiles, long businessId);

    BaseDto<SmsDataDto> loanRaisingCompleteNotify(LoanRaisingCompleteNotifyDto dto);
}

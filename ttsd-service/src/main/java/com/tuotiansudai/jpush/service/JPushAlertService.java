package com.tuotiansudai.jpush.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.dto.JpushReportDto;
import com.tuotiansudai.jpush.repository.model.*;

import java.util.Date;
import java.util.List;

public interface JPushAlertService {
    void buildJPushAlert(String loginName, JPushAlertDto jPushAlertDto);

    int findMaxSerialNumByType(PushType pushType);

    int findPushAlertCount(PushType pushType,
                           PushSource pushSource, PushUserType pushUserType,PushStatus pushStatus,
                           Date startTime, Date endTime,boolean isAutomatic);

    List<JPushAlertModel> findPushAlerts(int index, int pageSize, PushType pushType,
                                         PushSource pushSource, PushUserType pushUserType,PushStatus pushStatus,
                                         Date startTime, Date endTime, boolean isAutomatic);

    JPushAlertModel findJPushAlertModelById(long id);

    BaseDto<JpushReportDto> refreshPushReport(long jpushId);

    void changeJPushAlertStatus(long id, PushStatus status, String loginName);

    void changeJPushAlertContent(long id, String content, String loginName);

    void manualJPushAlert(long id);

    void autoJPushAlertBirthMonth();

    void autoJPushAlertBirthDay();

    void autoJPushNoInvestAlert();

    void autoJPushLoanAlert(long loanId);

    void autoJPushRepayAlert(long loanId);

    void autoJPushRechargeAlert(RechargeDto rechargeDto);

    BaseDto<BaseDataDto> pass(String loginName, long id, String ip);

    void reject(String loginName, long id, String ip);

    void delete(String loginName, long id);

}

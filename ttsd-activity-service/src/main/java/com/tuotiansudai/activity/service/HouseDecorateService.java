package com.tuotiansudai.activity.service;

import com.tuotiansudai.repository.mapper.InvestMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HouseDecorateService {

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.family.finance.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.family.finance.endTime}\")}")
    private Date endTime;

    public long toDayInvestAmount(String loginName){
        Date nowDate= DateTime.now().toDate();
        if (nowDate.before(startTime) || nowDate.after(endTime)) {
            return 0;
        }
        return investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().minusMillis(1).toDate()).stream().mapToLong(i->i.getAmount()).sum();
    }

    public long investAmount(String loginName){
        return investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,startTime,endTime).stream().mapToLong(i->i.getAmount()).sum();
    }
}

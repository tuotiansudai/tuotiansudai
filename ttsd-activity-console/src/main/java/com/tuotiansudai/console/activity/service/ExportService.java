package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.AutumnService;
import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    @Autowired
    private AutumnService autumnService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public List<AutumnExportDto> getAutumnExport(){
        List<AutumnExportDto> autumnExportDtoList = Lists.newArrayList();
        int activityDays = (int)DateUtil.differenceDay(activityAutumnStartTime, activityAutumnEndTime) + 1;
        for(int i = 0; i < activityDays;i ++){
            Date startTime = new DateTime(activityAutumnStartTime).plusDays(i).withTimeAtStartOfDay().toDate();
            Date endTime =  new DateTime(activityAutumnStartTime).plusDays(i).withTime(23,59,59,0).toDate();

            Map<String, List<String>> allFamilyAndNum = autumnService.getAllFamilyMap(activityAutumnStartTime, endTime);

            for(Map.Entry<String, List<String>> entry1:allFamilyAndNum.entrySet()){
                long totalAmount = 0;
                List<InvestModel> currentHomeInvestModelList = Lists.newArrayList();
                for(String loginName: entry1.getValue()){
                    totalAmount += investMapper.sumInvestAmount(null, loginName, null, null, null, startTime, endTime, InvestStatus.SUCCESS, null);
                    List<InvestModel> investModelList = investMapper.findSuccessInvestByInvestTime(loginName, startTime, endTime);
                    currentHomeInvestModelList.addAll(investModelList);
                }
                for(InvestModel investModel:currentHomeInvestModelList){
                    AutumnExportDto autumnExportDto = new AutumnExportDto();
                    autumnExportDto.setName(entry1.getKey());
                    autumnExportDto.setTotalAmount(totalAmount);
                    autumnExportDto.setInvestTime(startTime);
                    if(totalAmount >= 5000000){
                        autumnExportDto.setPrize("50元红包");
                    }
                    else if(totalAmount >= 2000000 && totalAmount < 5000000){
                        autumnExportDto.setPrize("15元红包");
                    }
                    else if(totalAmount >= 1000000 && totalAmount < 2000000){
                        autumnExportDto.setPrize("5元红包");
                    }
                    else{
                        autumnExportDto.setPrize("");
                    }
                    UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());

                    autumnExportDto.setLoginName(investModel.getLoginName());
                    autumnExportDto.setInvestAmount(investModel.getAmount());
                    autumnExportDto.setJoinTime(userModel.getRegisterTime());
                    autumnExportDto.setMobile(userModel.getMobile());
                    autumnExportDtoList.add(autumnExportDto);
                }
            }
        }
        return autumnExportDtoList;
    }

    public List<List<String>> buildAutumnList(List<AutumnExportDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (AutumnExportDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getName());
            row.add(AmountConverter.convertCentToString(record.getTotalAmount()));
            row.add(new DateTime(record.getInvestTime()).toString("yyyy-MM-dd"));
            row.add(record.getPrize());
            row.add("明细-->");
            row.add(record.getLoginName());
            row.add(AmountConverter.convertCentToString(record.getInvestAmount()));
            row.add(new DateTime(record.getJoinTime()).toString("HH:mm:ss"));
            row.add(record.getMobile());
            rows.add(row);
        }
        return rows;
    }

}

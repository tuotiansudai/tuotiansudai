package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.AutumnService;
import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    @Autowired
    private AutumnService autumnService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    /*
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Map<String, Map<String, List<String>>> getHomeMap(){
        Map<String, Map<String, List<String>>>  homeMap = new HashMap<String, Map<String, List<String>>>();
        int homeSeq = 1;
        List<UserModel> userModelList =  userMapper.findUsersByRegisterTime(activityAutumnStartTime, activityAutumnEndTime);
        for(UserModel userModel:userModelList){
            List<ReferrerRelationModel> referrerRelationModelList = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);

            if(referrerRelationModelList.size() > 0 && !homeMap.containsKey(referrerRelationModelList.get(0).getReferrerLoginName())){
                //找出当前团长的所有团员
                List<ReferrerRelationModel> referrerRelationModelListAll = referrerRelationMapper.findByReferrerLoginNameAndLevelAndRegisterTime(referrerRelationModelList.get(0).getReferrerLoginName(), activityAutumnStartTime, activityAutumnEndTime);
                Map<String, List<String>> userMap = new HashMap<String, List<String>>();
                List<String> alluser = Lists.newArrayList();
                for(ReferrerRelationModel referrerRelationModel: referrerRelationModelListAll){
                    //放入所有团员
                    alluser.add(referrerRelationModel.getLoginName());
                }
                //最后放入团长本人
                alluser.add(referrerRelationModelList.get(0).getReferrerLoginName());

                //把活动期间的所有天数存起来
                int activityDays = (int)DateUtil.differenceDay(activityAutumnStartTime, activityAutumnEndTime) + 1;
                for(int i = 0; i < activityDays;i ++){
                    userMap.put("团员" + homeSeq + "号家庭|"+ new DateTime(activityAutumnStartTime).plusDays(i), alluser);
                }
                homeMap.put(referrerRelationModelList.get(0).getReferrerLoginName(), userMap);
                homeSeq ++;
            }
        }
        return homeMap;
    }
    */

    public List<AutumnExportDto> getAutumnExport(){
        List<AutumnExportDto> autumnExportDtoList = Lists.newArrayList();
        Map<String, List<String>>  homeMap = autumnService.getHomeMap();
       /* for(Map.Entry<String,  Map<String, List<String>>> entry:homeMap.entrySet()){
            Map<String, List<String>> allUser = entry.getValue();
            for(Map.Entry<String, List<String>> entry1:allUser.entrySet()){
                long totalAmount = 0;
                String nameAndRegisterTime = entry1.getKey().toString();
                String nameAndRegisterTimeArr[] = nameAndRegisterTime.split("\\|");
                List<InvestModel> currentHomeInvestModelList = Lists.newArrayList();
                for(String loginName: entry1.getValue()){
                    totalAmount += investMapper.sumInvestAmount(null, loginName, null, null, null, new DateTime(nameAndRegisterTimeArr[1]).withTimeAtStartOfDay().toDate(), new DateTime(nameAndRegisterTimeArr[1]).withTime(23,59,59,0).toDate(), InvestStatus.SUCCESS, null);
                    List<InvestModel> investModelList = investMapper.findSuccessInvestByInvestTime(loginName, new DateTime(nameAndRegisterTimeArr[1]).withTimeAtStartOfDay().toDate(), new DateTime(nameAndRegisterTimeArr[1]).withTime(23,59,59,0).toDate());
                    currentHomeInvestModelList.addAll(investModelList);
                }
                for(InvestModel investModel:currentHomeInvestModelList){
                    AutumnExportDto autumnExportDto = new AutumnExportDto();
                    autumnExportDto.setName(nameAndRegisterTimeArr[0]);
                    autumnExportDto.setTotalAmount(totalAmount);
                    autumnExportDto.setInvestTime(new DateTime(nameAndRegisterTimeArr[1]).toDate());
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
        }*/
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

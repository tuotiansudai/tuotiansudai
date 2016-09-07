package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.console.activity.dto.UserItemExportDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExportService {

    @Autowired
    private UserService userService;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, List<String>> getHomeMap(){
        Map<String, List<String>>  homeMap = new HashMap<String, List<String>>();
       // List<UserModel> userModelList =  userService.findByRegisterTime(activityAutumnStartTime, activityAutumnEndTime);

        UserModel userModel1 = new UserModel();
        userModel1.setLoginName("zhanshan");
        userModel1.setRegisterTime(new DateTime().toDate());

        UserModel userModel12 = new UserModel();
        userModel12.setLoginName("lishi");
        userModel12.setRegisterTime(new DateTime().plusDays(1).toDate());

        List<UserModel> userModelList = Lists.newArrayList();
        userModelList.add(userModel1);
        userModelList.add(userModel12);



        int homeSeq = 1;
        for(UserModel userModel:userModelList){
            //List<ReferrerRelationModel> referrerRelationModelList = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);

            ReferrerRelationModel referrerRelationModel1 = new ReferrerRelationModel();
            referrerRelationModel1.setReferrerLoginName("aaaa");
            referrerRelationModel1.setLoginName(userModel1.getLoginName());
            referrerRelationModel1.setLevel(1);


            ReferrerRelationModel referrerRelationModel2 = new ReferrerRelationModel();
            referrerRelationModel2.setReferrerLoginName("bbbb");
            referrerRelationModel2.setLoginName(userModel12.getLoginName());
            referrerRelationModel2.setLevel(2);

            List<ReferrerRelationModel> referrerRelationModelList = Lists.newArrayList();
            referrerRelationModelList.add(referrerRelationModel1);
            //referrerRelationModelList.add(referrerRelationModel2);

            if(referrerRelationModelList.size() == 1){
                //找出当前团长的所有团员
               // List<ReferrerRelationModel> referrerRelationModelListAll = referrerRelationMapper.findByReferrerLoginName(referrerRelationModelList.get(0).getReferrerLoginName());

                ReferrerRelationModel referrerRelationModel3 = new ReferrerRelationModel();
                referrerRelationModel3.setReferrerLoginName("aaaa");
                referrerRelationModel3.setLoginName(userModel1.getLoginName());
                referrerRelationModel3.setLevel(1);

                ReferrerRelationModel referrerRelationModel4 = new ReferrerRelationModel();
                referrerRelationModel4.setReferrerLoginName("aaaa");
                referrerRelationModel4.setLoginName(userModel12.getLoginName());
                referrerRelationModel4.setLevel(2);


                List<ReferrerRelationModel> referrerRelationModelListAll = Lists.newArrayList();
                referrerRelationModelListAll.add(referrerRelationModel3);
                referrerRelationModelListAll.add(referrerRelationModel4);

                List<String> alluser = Lists.newArrayList();
                for(ReferrerRelationModel referrerRelationModel: referrerRelationModelListAll){
                    alluser.add(referrerRelationModel.getLoginName());
                }
                //团长本人
                alluser.add(referrerRelationModelList.get(0).getReferrerLoginName());
                homeMap.put("团员" + homeSeq + "号家庭_" + sdf.format(userModel.getRegisterTime()), alluser);
                homeSeq ++;
            }
        }
        return homeMap;
    }

    public List<AutumnExportDto> getAutumnExport(){
        List<AutumnExportDto> autumnExportDtoList = Lists.newArrayList();
        List<UserItemExportDto> userItemExportDtoList = Lists.newArrayList();
        Map<String, List<String>>  homeMap = getHomeMap();
        AutumnExportDto autumnExportDto = new AutumnExportDto();
        for(Map.Entry<String, List<String>> entry:homeMap.entrySet()){
            long totalAmount = 0;
            String nameAndRegisterTime[] = entry.getKey().split("_");
            String investTime =nameAndRegisterTime[1];
            for(String loginName: entry.getValue()){
                System.out.println(new DateTime(investTime).withTimeAtStartOfDay().toDate());
                System.out.println(new DateTime(investTime).withTime(23,59,59,0).toDate());
                totalAmount += investMapper.sumInvestAmount(null, loginName, null, null, null, new DateTime(investTime).withTimeAtStartOfDay().toDate(), new DateTime(investTime).withTime(23,59,59,0).toDate(), InvestStatus.SUCCESS, null);
            }
            List<InvestModel> investModelList = investMapper.findSuccessInvestByInvestTime(new DateTime(investTime).withTimeAtStartOfDay().toDate(), new DateTime(investTime).withTime(23,59,59,0).toDate());
            for(InvestModel investModel:investModelList){
                UserItemExportDto userItemExportDto = new UserItemExportDto();
                userItemExportDto.setLoginName(investModel.getLoginName());
                userItemExportDto.setInvestAmount(investModel.getAmount());
                userItemExportDto.setJoinTime(investModel.getCreatedTime());
                userItemExportDto.setMobile("222222");
                userItemExportDtoList.add(userItemExportDto);
            }

            autumnExportDto.setName(nameAndRegisterTime[0]);
            autumnExportDto.setTotalAmount(totalAmount);

            autumnExportDto.setInvestTime(new DateTime(investTime).toDate());
            if(totalAmount > 5000000){
                autumnExportDto.setPrize("50元红包");
            }
            if(totalAmount > 2000000){
                autumnExportDto.setPrize("15元红包");
            }
            if(totalAmount > 1000000){
                autumnExportDto.setPrize("5元红包");
            }
            autumnExportDto.setUserItemExportDtos(userItemExportDtoList);

            autumnExportDtoList.add(autumnExportDto);

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
            row.add(record.getUserItemExportDtos().toString());
            rows.add(row);
        }
        return rows;
    }


    public static void main(String args[]){
        ExportService exportService = new ExportService();

        exportService.getAutumnExport();
    }
}




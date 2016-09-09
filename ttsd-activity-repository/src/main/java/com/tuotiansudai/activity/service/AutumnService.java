package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.autumn.mapper.AutumnMapper;
import com.tuotiansudai.activity.repository.autumn.model.AutumnReferrerRelationView;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutumnService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AutumnMapper autumnMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public Map<String, List<String>> getHomeMap(){
        Map<String, List<String>>  homeMap = new LinkedHashMap<String, List<String>>();
        int homeSeq = 1;
        List<UserModel> userModelList =  userMapper.findUsersByRegisterTime(activityAutumnStartTime, activityAutumnEndTime);
        for(UserModel userModel:userModelList){
            List<AutumnReferrerRelationView> referrerRelationViewList = autumnMapper.findByLoginNameAndLevel(userModel.getLoginName(), 1);

            if(CollectionUtils.isNotEmpty(referrerRelationViewList)){

                if(!homeMap.containsKey(referrerRelationViewList.get(0).getReferrerLoginName() + "|团员" + homeSeq + "号家庭")){
                    //找出当前团长的所有团员
                    List<AutumnReferrerRelationView> autumnReferrerRelationViewListAll = autumnMapper.findByReferrerLoginNameAndLevelAndRegisterTime(referrerRelationViewList.get(0).getReferrerLoginName(), activityAutumnStartTime, activityAutumnEndTime);
                    List<String> alluser = Lists.newArrayList();
                    // 先放入团长本人
                    alluser.add(referrerRelationViewList.get(0).getReferrerLoginName() + "|");
                    for(AutumnReferrerRelationView referrerRelationModel: autumnReferrerRelationViewListAll){
                        //放入所有团员
                        alluser.add(referrerRelationModel.getLoginName());
                    }

                    //把活动期间的所有天数存起来
//                int activityDays = (int)DateUtil.differenceDay(activityAutumnStartTime, activityAutumnEndTime) + 1;
//                for(int i = 0; i < activityDays;i ++){
//                    userMap.put("团员" + homeSeq + "号家庭|"+ new DateTime(activityAutumnStartTime).plusDays(i), alluser);
//                }
                    homeMap.put(referrerRelationViewList.get(0).getReferrerLoginName() + "|团员" + homeSeq + "号家庭" , alluser);
                    homeSeq ++;
                }
            }
        }
        return homeMap;
    }
}

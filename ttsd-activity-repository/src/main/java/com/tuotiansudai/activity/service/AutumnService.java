package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutumnService {

    @Autowired
    private UserMapper userMapper;

    public Map getAllFamilyMap(Date activityMinAutumnStartTime, Date activityMinAutumnEndTime) {
        List<UserModel> userModels = userMapper.findUsersByRegisterTime(activityMinAutumnStartTime, activityMinAutumnEndTime);

        Map<String, List<String>> allFamily = new LinkedHashMap<>();

        if (userModels.size() == 0) {
            return Maps.newConcurrentMap();
        }

        for (UserModel userModel : userModels) {
            if (StringUtils.isEmpty(userModel.getReferrer())) {
                allFamily.put(userModel.getLoginName(),Lists.newArrayList(userModel.getLoginName()));
                continue;
            }
            if(CollectionUtils.isEmpty(allFamily.values())){
                allFamily.put(userModel.getReferrer(),Lists.newArrayList(userModel.getLoginName()));
                continue;
            }
            for (List<String> family : allFamily.values()) {
                if(family.contains(userModel.getReferrer())){
                    family.add(userModel.getLoginName());
                }else{
                    allFamily.put(userModel.getReferrer(),Lists.newArrayList(userModel.getLoginName()));
                }
            }

        }

        Map<String, List<String>> allFamilyAndNum = new LinkedHashMap<>();
        int num = 0;
        for(String key : allFamily.keySet()){
            List<String> family = allFamily.get(key);
            if(family.size() == 1){
                continue;
            }
            num ++;
            allFamilyAndNum.put(MessageFormat.format("团员{0}号家庭",String.valueOf(num)),allFamily.get(key));
        }

        return allFamilyAndNum;

    }

}

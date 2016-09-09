package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.autumn.mapper.AutumnMapper;
import com.tuotiansudai.activity.repository.autumn.model.AutumnReferrerRelationView;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class AutumnService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AutumnMapper autumnMapper;

    public Map getAllFamilyMap(Date activityMinAutumnStartTime,Date activityMinAutumnEndTime) {
        List<UserModel> userModels = userMapper.findUsersByRegisterTime(activityMinAutumnStartTime, activityMinAutumnEndTime);

        if (CollectionUtils.isEmpty(userModels)) {
            return Maps.newConcurrentMap();
        }

        Map<String, List<String>> allFamily = new LinkedHashMap<>();
        String referrerLoginName;
        String loginName;
        for (UserModel userModel : userModels) {
            List<AutumnReferrerRelationView> referrerRelationModels = autumnMapper.findByLoginNameAndLevel(userModel.getLoginName(), 1);
            //有一级推荐时查询是否是团长，否则查询是否是团长推荐（无视推荐层级）
            if (CollectionUtils.isNotEmpty(referrerRelationModels)) {
                referrerLoginName = referrerRelationModels.get(0).getReferrerLoginName();
                loginName = userModel.getLoginName();
                if (allFamily.get(referrerLoginName) != null) {
                    allFamily.get(referrerLoginName).add(loginName);
                    continue;
                }

                for (String name : allFamily.keySet()) {
                    if (autumnMapper.findByReferrerAndLoginName(name, loginName) != null) {
                        allFamily.get(name).add(loginName);
                        continue;
                    }
                }
            } else {
                //没有一级推荐就是团长
                allFamily.put(userModel.getLoginName(), Lists.newArrayList(userModel.getLoginName()));
            }
        }

        Map<String, List<String>> allFamilyAndNum = new LinkedHashMap<>();
        int num = 0;
        for(String key : allFamily.keySet()){
            num ++;
            allFamilyAndNum.put(MessageFormat.format("团员{0}号家庭",String.valueOf(num)),allFamily.get(key));
        }

        return allFamilyAndNum;
    }
}

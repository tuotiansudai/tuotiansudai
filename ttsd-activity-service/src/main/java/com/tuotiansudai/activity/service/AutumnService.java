package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
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
        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(activityMinAutumnStartTime, activityMinAutumnEndTime, null);

        Map<String, List<String>> allFamily = new LinkedHashMap<>();

        if (userModels.size() == 0) {
            return Maps.newConcurrentMap();
        }

        for (UserModel userModel : userModels) {
            if (Strings.isNullOrEmpty(userModel.getReferrer())) {
                allFamily.put(userModel.getLoginName(), Lists.newArrayList(userModel.getLoginName()));
                continue;
            }
            if (allFamily.values() == null || allFamily.values().size() == 0) {
                allFamily.put(userModel.getReferrer(), Lists.newArrayList(userModel.getReferrer(), userModel.getLoginName()));
                continue;
            }
            boolean isFamily = false;
            for (List<String> family : allFamily.values()) {
                if (family.contains(userModel.getReferrer())) {
                    isFamily = true;
                    family.add(userModel.getLoginName());
                    break;
                }
            }

            if (!isFamily) {
                allFamily.put(userModel.getReferrer(), Lists.newArrayList(userModel.getReferrer(), userModel.getLoginName()));
            }

        }

        Map<String, List<String>> allFamilyAndNum = new LinkedHashMap<>();
        int num = 0;
        for (String key : allFamily.keySet()) {
            List<String> family = allFamily.get(key);
            if (family.size() == 1) {
                continue;
            }
            num++;
            allFamilyAndNum.put(MessageFormat.format("团员{0}号家庭", String.valueOf(num)), allFamily.get(key));
        }

        return allFamilyAndNum;

    }

}

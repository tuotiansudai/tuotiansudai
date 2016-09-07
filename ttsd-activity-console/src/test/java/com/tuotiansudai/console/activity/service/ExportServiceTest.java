package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.console.activity.service.ExportService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExportServiceTest {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private ExportService exportService;



    @Test
    public void ExportServiceIsOk(){
        List<UserModel> userModelList = Lists.newArrayList();

        userModelList.add(createUserModel("zhangshan", new DateTime().minusDays(1).toDate()));
        userModelList.add(createUserModel("lisi", new DateTime().toDate()));
        userModelList.add(createUserModel("wangwu", new DateTime().minusDays(1).toDate()));
        userModelList.add(createUserModel("maliu", new DateTime().plusDays(1).toDate()));

        createReferrerRelationModel("zhangshan", "test", 1);
        createReferrerRelationModel("lisi", "test", 1);
        createReferrerRelationModel("wangwu", "test", 1);
        createReferrerRelationModel("maliu", "test", 1);


        exportService.getAutumnExport();

    }

    private UserModel createUserModel(String loginName, Date registerTime){
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setRegisterTime(registerTime);
        userMapper.create(userModel);
        return  userModel;
    }

    private ReferrerRelationModel createReferrerRelationModel(String loginName, String referrer, int level){
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setReferrerLoginName(referrer);
        referrerRelationModel.setLevel(level);
        referrerRelationMapper.create(referrerRelationModel);
        return referrerRelationModel;
    }

}

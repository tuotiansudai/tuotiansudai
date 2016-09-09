package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ExportServiceTest extends TestBase{

    @Autowired
    private UserMapper userMapper;

    /*@Autowired
    private ExportService exportService;
    */
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Test
    public void ExportServiceIsOk(){
        List<UserModel> userModelList = Lists.newArrayList();

        List<ReferrerRelationModel> referrerRelationModelList = Lists.newArrayList();

        userModelList.add(createUserModel("olduser1", new DateTime().minusDays(1).toDate(), "13800000001"));
        userModelList.add(createUserModel("olduser2", new DateTime().minusDays(1).toDate(), "13800000002"));

        userModelList.add(createUserModel("zhangshan", new DateTime().minusDays(1).toDate(), "13900000001"));
        userModelList.add(createUserModel("lisi", new DateTime().toDate(), "13900000002"));
        userModelList.add(createUserModel("wangwu", new DateTime().minusDays(1).toDate(), "13900000003"));
        userModelList.add(createUserModel("maliu", new DateTime().plusDays(1).toDate(), "13900000004"));


        System.out.println(userModelList.size());
        referrerRelationModelList.add(createReferrerRelationModel("zhangshan", "olduser1", 1));
        referrerRelationModelList.add(createReferrerRelationModel("lisi", "olduser1", 1));
        referrerRelationModelList.add(createReferrerRelationModel("wangwu", "olduser2", 1));
        referrerRelationModelList.add(createReferrerRelationModel("maliu", "olduser2", 1));

        System.out.println(referrerRelationModelList.size());
        //exportService.getAutumnExport();

    }

    private UserModel createUserModel(String loginName, Date registerTime, String mobile){
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(registerTime);
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return  userModelTest;
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

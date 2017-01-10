package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.log.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoginLogMapperTest {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Test
    public void shouldCreateAndFind() {
        String loginName = "aaaa";
        LoginLogModel loginLogModel = fakeLoginLogModel(loginName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String table = "login_log_" + simpleDateFormat.format(new Date());
        loginLogMapper.create(table, loginLogModel);
    }

    private LoginLogModel fakeLoginLogModel(String loginName) {
        return new LoginLogModel(loginName, Source.WEB, "192.111.11.1", "asdf", true);
    }
}

package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.log.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class LoginLogMapperTest {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Test
    public void shouldCreateAndFindAndGetPaginationData() {
        String loginName = "aaaa";
        LoginLogModel loginLogModel = fakeLoginLogModel(loginName);
        String table = "login_log_201801";
        loginLogMapper.create(table, loginLogModel);

        long count = loginLogMapper.count(loginName, "1932", true, table, null);
        assertTrue (count > 0);

        List<LoginLogModel> data = loginLogMapper.getPaginationData(loginName, "1871", true, null, 0, 10, table);
        assertTrue (data.size() > 0);
    }

    private LoginLogModel fakeLoginLogModel(String loginName) {
        return new LoginLogModel(loginName, Source.WEB, "192.111.11.1", "asdf", true);
    }
}

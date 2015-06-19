package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by hourglasskoala on 15/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback=false)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindByEmail() throws  Exception{
        UserModel userModel = userMapper.findUserByEmail("123@abc.com");
        assertNotNull(userModel);
    }
    @Test
    public void testFindUserByMobileNumber() throws  Exception{
        UserModel userModel = userMapper.findUserByMobileNumber("18610361804");
        assertNotNull(userModel);
    }
    @Test
    public void testFindReferrerByLoginName() throws Exception{
        UserModel userModel = userMapper.findReferrerByLoginName("hourglass");
        assertNotNull(userModel);
    }
}

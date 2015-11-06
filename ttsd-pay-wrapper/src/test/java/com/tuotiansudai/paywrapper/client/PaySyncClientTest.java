package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectAccountSearchMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerQueryMapper;
import com.tuotiansudai.paywrapper.repository.mapper.UserSearchMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectAccountSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.PtpMerQueryRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.UserSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.PtpMerQueryResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.UserSearchResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PaySyncClientTest {

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    @Transactional
    public void test() throws Exception {
    }
}

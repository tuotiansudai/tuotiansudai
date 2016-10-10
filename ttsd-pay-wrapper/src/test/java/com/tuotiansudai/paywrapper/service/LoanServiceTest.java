package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.client.PayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.service.impl.LoanServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.umpay.api.exception.ReqDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
@Transactional
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayGateWrapper payGateWrapper;

    @Mock
    private PaySyncClient paySyncClient;


    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateLoanIsSuccess() throws ReqDataException, PayException {
        long id = idGenerator.generate();
        UserModel loanerModel = createFakeUser("loanerLoginName");
        LoanModel loanModel = fakeLoanModel();
        AccountModel accountModel = this.getFakeAccount(loanerModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        MerBindProjectResponseModel merBindProjectResponseModel = new MerBindProjectResponseModel();
        merBindProjectResponseModel.setMerCheckDate(new Date().toString());
        merBindProjectResponseModel.setProjectAccountId("ProjectAccountId");
        merBindProjectResponseModel.setRetCode("0000");
        merBindProjectResponseModel.setRetMsg("成功");
        when(paySyncClient.send(eq(MerBindProjectMapper.class), any(MerBindProjectRequestModel.class), eq(MerBindProjectResponseModel.class))).thenReturn(merBindProjectResponseModel);
        loanService.createLoan(id);

        ArgumentCaptor<MerBindProjectRequestModel> merBindProjectRequestModelArgumentCaptor = ArgumentCaptor.forClass(MerBindProjectRequestModel.class);
        verify(paySyncClient, times(1)).send(eq(MerBindProjectMapper.class), merBindProjectRequestModelArgumentCaptor.capture(), eq(MerBindProjectResponseModel.class));

        assertEquals(merBindProjectRequestModelArgumentCaptor.getValue().getProjectName(),merBindProjectRequestModelArgumentCaptor.getValue().getProjectId());
    }
    protected AccountModel getFakeAccount(UserModel userModel) {
        AccountModel fakeAccount = new AccountModel(userModel.getLoginName(), userModel.getLoginName(), "ID", "payUserId", "payAccountId", new Date());
        fakeAccount.setBalance(1000000);
        return fakeAccount;
    }

    private UserModel createFakeUser(String loginName) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("12900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return model;
    }

    private LoanModel fakeLoanModel(){
        LoanModel loanModel = new LoanModel();

        return loanModel;
    }
}
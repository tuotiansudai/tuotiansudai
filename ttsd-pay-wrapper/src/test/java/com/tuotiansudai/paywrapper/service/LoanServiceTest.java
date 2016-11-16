package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.client.PayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayGateWrapper payGateWrapper;

    @Mock
    private PaySyncClient paySyncClient;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateLoanIsSuccess() throws ReqDataException, PayException {
        LoanModel loanModel = this.fakeLoanModel();
        AccountModel accountModel = this.getFakeAccount();

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);

        MerBindProjectResponseModel merBindProjectResponseModel = new MerBindProjectResponseModel();
        merBindProjectResponseModel.setRetCode("0000");
        MerUpdateProjectResponseModel merUpdateProjectResponseModel = new MerUpdateProjectResponseModel();
        merUpdateProjectResponseModel.setRetCode("0000");

        when(paySyncClient.send(eq(MerBindProjectMapper.class), any(MerBindProjectRequestModel.class), eq(MerBindProjectResponseModel.class))).thenReturn(merBindProjectResponseModel);
        when(paySyncClient.send(eq(MerUpdateProjectMapper.class), any(MerUpdateProjectRequestModel.class), eq(MerUpdateProjectResponseModel.class))).thenReturn(merUpdateProjectResponseModel);

        loanService.createLoan(fakeLoanModel().getId());

        ArgumentCaptor<MerBindProjectRequestModel> merBindProjectRequestModelArgumentCaptor = ArgumentCaptor.forClass(MerBindProjectRequestModel.class);
        ArgumentCaptor<MerUpdateProjectRequestModel> merUpdateProjectRequestModelArgumentCaptor = ArgumentCaptor.forClass(MerUpdateProjectRequestModel.class);
        verify(paySyncClient, times(1)).send(eq(MerBindProjectMapper.class), merBindProjectRequestModelArgumentCaptor.capture(), eq(MerBindProjectResponseModel.class));
        verify(paySyncClient, times(2)).send(eq(MerUpdateProjectMapper.class), merUpdateProjectRequestModelArgumentCaptor.capture(), eq(MerUpdateProjectResponseModel.class));

        MerBindProjectRequestModel merBindProjectRequestModelArgumentCaptorValue = merBindProjectRequestModelArgumentCaptor.getValue();
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getLoanUserId(), is(accountModel.getPayUserId()));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectAmount(), is(String.valueOf(loanModel.getLoanAmount())));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectName(), is(String.valueOf(loanModel.getId())));

        List<MerUpdateProjectRequestModel> merUpdateProjectRequestModelArgumentCaptorAllValues = merUpdateProjectRequestModelArgumentCaptor.getAllValues();
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(0).getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(0).getProjectState(), is("0"));

        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(1).getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(1).getProjectState(), is("1"));
    }


    protected AccountModel getFakeAccount() {
        return new AccountModel("loginName", "payUserId", "payAccountId", new Date());
    }

    private LoanModel fakeLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(0);
        loanModel.setAgentLoginName("agent");
        loanModel.setLoanAmount(1);
        return loanModel;
    }
}
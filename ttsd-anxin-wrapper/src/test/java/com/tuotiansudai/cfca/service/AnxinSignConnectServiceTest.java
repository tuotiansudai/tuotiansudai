package com.tuotiansudai.cfca.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3101ReqVO;
import cfca.trustsign.common.vo.request.tx3.Tx3102ReqVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import com.tuotiansudai.cfca.connector.AnxinClient;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.cfca.constant.TxCode;
import com.tuotiansudai.cfca.service.impl.AnxinSignConnectServiceImpl;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinSignConnectServiceTest {

    @InjectMocks
    private AnxinSignConnectServiceImpl anxinSignConnectService;

    @Mock
    private AnxinClient anxinClient;

    @Mock
    private RequestResponseService requestResponseService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateAccount3001() throws PKIException {

        UserModel userModel = fakeUser();

        String createAccountSuccessResposne = "{\"head\":{\"txTime\":\"20161202120618\",\"locale\":\"zh_CN\",\"retCode\":\"60000000\",\"retMessage\":\"OK\"},\"person\":{\"userId\":\"4027A45BCB2629E9E05311016B0AA19C\",\"personName\":\"周宝鑫\",\"identTypeCode\":\"0\",\"identNo\":\"34112719840523021X\",\"email\":\"\",\"mobilePhone\":\"18611445119\",\"address\":\"北京\",\"authenticationMode\":\"公安部\",\"anXinSignMobilePhone\":\"186****5119\"},\"notSendPwd\":0}";

        when(anxinClient.send(eq(TxCode.CREATE_ACCOUNT), anyString())).thenReturn(createAccountSuccessResposne);

        doNothing().when(requestResponseService).insertCreateAccountRequest(any(Tx3001ReqVO.class));
        doNothing().when(requestResponseService).insertCreateAccountResponse(any(Tx3001ResVO.class));

        Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(userModel);

        assert (tx3001ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
    }

    @Test
    public void shouldCreateAccount3001Fail() throws PKIException {

        UserModel userModel = fakeUser();

        String createAccountFailResposne = "";

        when(anxinClient.send(eq(TxCode.CREATE_ACCOUNT), anyString())).thenReturn(createAccountFailResposne);

        doNothing().when(requestResponseService).insertCreateAccountRequest(any(Tx3001ReqVO.class));
        doNothing().when(requestResponseService).insertCreateAccountResponse(any(Tx3001ResVO.class));

        Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(userModel);

        assertNull(tx3001ResVO);
    }

    @Test
    public void shouldSendCaptcha3101() throws PKIException {

        String sendCaptchaSuccessResposne = "{\"head\":{\"txTime\":\"20161202182037\",\"locale\":\"zh_CN\",\"retCode\":\"60000000\",\"retMessage\":\"OK\"},\"proxySign\":{\"userId\":\"4027A45BC12E29E9E05311016B0AA19C\",\"projectCode\":\"004\",\"isSendVoice\":0}}";

        doNothing().when(requestResponseService).insertSendCaptchaRequest(any(Tx3101ReqVO.class));
        doNothing().when(requestResponseService).insertSendCaptchaResponse(any(Tx3101ResVO.class));

        when(anxinClient.send(eq(TxCode.SEND_CAPTCHA), anyString())).thenReturn(sendCaptchaSuccessResposne);

        Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101("4027A45BCB2629E9E05311016B0AA19C", "111", false);

        assert (tx3101ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
    }

    @Test
    public void shouldSendCaptcha3101Fail() throws PKIException {

        String sendCaptchaFailResposne = "{\"errorCode\":\"60030401\",\"errorMessage\":\"已经开通代签权限\"}";

        doNothing().when(requestResponseService).insertSendCaptchaRequest(any(Tx3101ReqVO.class));
        doNothing().when(requestResponseService).insertSendCaptchaResponse(any(Tx3101ResVO.class));

        when(anxinClient.send(eq(TxCode.SEND_CAPTCHA), anyString())).thenReturn(sendCaptchaFailResposne);

        Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101("4027A45BCB2629E9E05311016B0AA19C", "111", false);

        assertEquals(tx3101ResVO.getHead().getRetCode(), "60030401");
    }

    @Test
    public void shouldVerifyCaptcha3102() throws PKIException {

        String sendCaptchaSuccessResposne = "{\"head\":{\"txTime\":\"20161202101304\",\"locale\":\"zh_CN\",\"retCode\":\"60000000\",\"retMessage\":\"OK\"},\"proxySign\":{\"userId\":\"4266F7DCE4394A4DE0538D02030A464A\",\"projectCode\":\"e7471731b9ea4e19a426f231a4b9867c\",\"checkCode\":\"997492\"}}";

        doNothing().when(requestResponseService).insertSendCaptchaRequest(any(Tx3101ReqVO.class));
        doNothing().when(requestResponseService).insertSendCaptchaResponse(any(Tx3101ResVO.class));

        when(anxinClient.send(eq(TxCode.VERIFY_CAPTCHA), anyString())).thenReturn(sendCaptchaSuccessResposne);

        Tx3102ResVO tx3102ResVO = anxinSignConnectService.verifyCaptcha3102("4027A45BCB2629E9E05311016B0AA19C", "111", "111111");

        assert (tx3102ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
    }

    @Test
    public void shouldVerifyCaptcha3102Fail() throws PKIException {

        String verifyCaptchaFailResposne = "{\"errorCode\":\"60030202\",\"errorMessage\":\"ID为4027A45BC12E29E9E05311016B0AA191的用户信息不存在或不是审核通过状态\"}";

        doNothing().when(requestResponseService).insertVerifyCaptchaRequest(any(Tx3102ReqVO.class));
        doNothing().when(requestResponseService).insertVerifyCaptchaResponse(any(Tx3102ResVO.class));

        when(anxinClient.send(eq(TxCode.VERIFY_CAPTCHA), anyString())).thenReturn(verifyCaptchaFailResposne);

        Tx3102ResVO tx3102ResVO = anxinSignConnectService.verifyCaptcha3102("4027A45BCB2629E9E05311016B0AA19C", "111", "111111");

        assertEquals(tx3102ResVO.getHead().getRetCode(), "60030202");
    }


    private UserModel fakeUser() {
        UserModel userModel = new UserModel();
        userModel.setMobile("18611445119");
        userModel.setUserName("周宝鑫");
        userModel.setIdentityNumber("34112719840523021X");
        userModel.setCity("北京");
        return userModel;
    }

}

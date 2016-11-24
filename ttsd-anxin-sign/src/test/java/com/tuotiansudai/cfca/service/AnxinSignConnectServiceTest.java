package com.tuotiansudai.cfca.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinSignConnectServiceTest {

    @Autowired
    private AnxinSignConnectService anxinSignConnectService;

    @Ignore
    @Test
    public void shouldCreateAccount3001() throws PKIException {

        UserModel userModel = fakeUser();

        Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(userModel);

        assert (tx3001ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
    }

    @Ignore
    @Test
    public void shouldSendCaptcha3101() throws PKIException {

        Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101("4027A45BCB2629E9E05311016B0AA19C", "111", false);

        assert (tx3101ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
    }

    @Ignore
    @Test
    public void shouldVerifyCaptcha3102() throws PKIException {

        Tx3102ResVO tx3102ResVO = anxinSignConnectService.verifyCaptcha3102("4027A45BCB2629E9E05311016B0AA19C", "111", "111111");

        assert (tx3102ResVO.getHead().getRetCode().equals(AnxinRetCode.SUCCESS));
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

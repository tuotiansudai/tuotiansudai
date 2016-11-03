package com.tuotiansudai.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnxinSignServiceImpl implements AnxinSignService {

    static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnxinSignConnectService anxinSignConnectService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String TEMP_PROJECT_CODE_KEY = "temp_project_code:";

    private static final int TEMP_PROJECT_CODE_EXPIRE_TIME = 1800; // 半小时

    @Override
    public BaseDto createAccount3001(String loginName) {

        try {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            UserModel userModel = userMapper.findByLoginName(loginName);

            Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(accountModel, userModel);
            String retMessage = tx3001ResVO.getHead().getRetMessage();

            if (isSuccess(tx3001ResVO)) {
                accountModel.setAnxinUserId(tx3001ResVO.getPerson().getUserId());
                accountMapper.update(accountModel);
                return new BaseDto();
            } else {
                logger.error("create anxin sign account failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("create anxin sign account failed. ", e);
            return failBaseDto();
        }
    }


    @Override
    public BaseDto sendCaptcha3101(String loginName) {

        try {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);

            // 如果用户没有开通安心签账户，则先开通账户，再进行授权（发送验证码）
            if (accountModel.getAnxinUserId() == null) {
                BaseDto createAccountRet = this.createAccount3001(loginName);
                if (!createAccountRet.isSuccess()) {
                    return failBaseDto();
                }
            }

            String anxinUserId = accountModel.getAnxinUserId();

            String projectCode = UUIDGenerator.generate();

            Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101(anxinUserId, projectCode);

            String retMessage = tx3101ResVO.getHead().getRetMessage();

            if (isSuccess(tx3101ResVO)) {
                redisWrapperClient.setex(TEMP_PROJECT_CODE_KEY + loginName, TEMP_PROJECT_CODE_EXPIRE_TIME, projectCode);
                return new BaseDto();
            } else {
                logger.error("send anxin captcha code failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("send anxin captcha code failed. ", e);
            return failBaseDto();
        }
    }

    @Override
    public BaseDto verifyCaptcha3102(String loginName, String captcha, boolean skipAuth) {

        try {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);

            String anxinUserId = accountModel.getAnxinUserId();

            String projectCode = redisWrapperClient.get(TEMP_PROJECT_CODE_KEY + loginName);

            if (StringUtils.isEmpty(projectCode)) {
                logger.error("project code is expired. loginName:" + loginName + ", anxinUserId:" + anxinUserId);
                return failBaseDto();
            }

            Tx3102ResVO tx3101ResVO = anxinSignConnectService.verifyCaptcha3102(anxinUserId, projectCode, captcha);

            String retMessage = tx3101ResVO.getHead().getRetMessage();

            if (isSuccess(tx3101ResVO)) {
                accountModel.setProjectCode(projectCode);
                accountModel.setSkipAuth(skipAuth);
                accountMapper.update(accountModel);
                return new BaseDto();
            } else {
                logger.error("verify anxin captcha code failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("verify anxin captcha code failed. ", e);
            return failBaseDto();
        }
    }

    private BaseDto failBaseDto() {
        BaseDto baseDto = new BaseDto<>();
        baseDto.setSuccess(false);
        return baseDto;
    }

    private boolean isSuccess(Tx3ResVO tx3ResVO) {
        return tx3ResVO.getHead().getRetCode().equals("60000000");
    }

}

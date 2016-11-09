package com.tuotiansudai.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    private static final String TEMP_PROJECT_CODE_KEY = "temp_project_code:";

    private static final int TEMP_PROJECT_CODE_EXPIRE_TIME = 1800; // 半小时

    @Value(value = "${anxin.contract.batch.num}")
    private int batchNum;

    @Value(value = "${anxin.loan.contract.template}")
    private String loanTemplate;

    @Value(value = "${anxin.transfer.contract.template}")
    private String transferTemplate;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    /**
     * 以前是否授权过
     */
    @Override
    public boolean hasAuthed(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && anxinProp.getAnxinUserId() != null && anxinProp.getProjectCode() != null;
    }


    /**
     * 获取用户的安心签相关属性
     */
    @Override
    public AnxinSignPropertyModel getAnxinSignProp(String loginName) {
        return anxinSignPropertyMapper.findByLoginName(loginName);
    }

    /**
     * 创建安心签账户
     */
    @Override
    public BaseDto createAccount3001(String loginName) {

        try {
            if (hasAnxinAccount(loginName)) {
                logger.error(loginName + " already have anxin-sign account. can't create anymore.");
                return failBaseDto("用户已有安心签账户，不能重复开户");
            }

            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            UserModel userModel = userMapper.findByLoginName(loginName);

            Tx3ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(accountModel, userModel);
            String retMessage = tx3001ResVO.getHead().getRetMessage();

            if (isSuccess(tx3001ResVO)) {

                AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
                anxinProp.setLoginName(loginName);
                Date now = new Date();
                anxinProp.setCreatedTime(now);
                anxinProp.setAnxinUserId(((Tx3001ResVO) tx3001ResVO).getPerson().getUserId());
                anxinSignPropertyMapper.create(anxinProp);

                accountMapper.update(accountModel);
                return new BaseDto();
            } else {
                logger.error("create anxin sign account failed. " + retMessage);
                return new BaseDto(false);
            }

        } catch (PKIException e) {
            logger.error("create anxin sign account failed. ", e);
            return new BaseDto(false);
        }
    }


    /**
     * 发送验证码
     */
    @Override
    public BaseDto sendCaptcha3101(String loginName, boolean isVoice) {
        try {
            // 如果用户没有开通安心签账户，则先开通账户，再进行授权（发送验证码）
            if (!hasAnxinAccount(loginName)) {
                BaseDto createAccountRet = this.createAccount3001(loginName);
                if (!createAccountRet.isSuccess()) {
                    return createAccountRet;
                }
            }

            String anxinUserId = anxinSignPropertyMapper.findByLoginName(loginName).getAnxinUserId();

            String projectCode = UUIDGenerator.generate();

            Tx3ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101(anxinUserId, projectCode, isVoice);

            if (isSuccess(tx3101ResVO)) {
                redisWrapperClient.setex(TEMP_PROJECT_CODE_KEY + loginName, TEMP_PROJECT_CODE_EXPIRE_TIME, projectCode);
                return new BaseDto();
            } else {
                logger.error("send anxin captcha code failed. " + tx3101ResVO.getHead().getRetMessage());
                return new BaseDto(false);
            }

        } catch (PKIException e) {
            logger.error("send anxin captcha code failed. ", e);
            return new BaseDto(false);
        }
    }

    /**
     * 确认验证码 （授权）
     */
    @Override
    public BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean skipAuth, String ip) {

        try {
            // 如果用户没有开通安心签账户，则返回失败
            if (!hasAnxinAccount(loginName)) {
                logger.error("user has not create anxin account yet. loginName: " + loginName);
                return failBaseDto("用户还未开通安心签账户");
            }

            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);

            String anxinUserId = anxinProp.getAnxinUserId();

            String projectCode = redisWrapperClient.get(TEMP_PROJECT_CODE_KEY + loginName);

            if (StringUtils.isEmpty(projectCode)) {
                logger.error("project code is expired. loginName:" + loginName + ", anxinUserId:" + anxinUserId);
                return failBaseDto("验证码已过期，请重新获取");
            }

            Tx3ResVO tx3101ResVO = anxinSignConnectService.verifyCaptcha3102(anxinUserId, projectCode, captcha);

            if (isSuccess(tx3101ResVO)) {
                // 更新projectCode 和 skipAuth
                anxinProp.setProjectCode(projectCode);
                anxinProp.setSkipAuth(skipAuth);
                anxinProp.setAuthTime(new Date());
                anxinProp.setAuthIp(ip);
                anxinSignPropertyMapper.update(anxinProp);
                BaseDto baseDto = new BaseDto();
                baseDto.setData(new BaseDataDto(true, skipAuth ? "skipAuth" : ""));
                return baseDto;
            } else {
                String retMessage = tx3101ResVO.getHead().getRetMessage();
                logger.error("verify anxin captcha code failed. " + retMessage);
                return failBaseDto(retMessage);
            }

        } catch (PKIException e) {
            logger.error("verify anxin captcha code failed. ", e);
            return new BaseDto<>(false);
        }
    }

    /**
     * 打开/关闭 免验开关
     */
    @Override
    public BaseDto switchSkipAuth(String loginName, boolean open) {
        try {
            logger.info(loginName + " is switching anxin-sign skip-auth " + (open ? "on." : "off."));
            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
            anxinProp.setSkipAuth(open);
            anxinSignPropertyMapper.update(anxinProp);
        } catch (Exception e) {
            logger.error("switch anxin-sign skip-auth " + (open ? "on " : "off ") + "failed.", e);
            return new BaseDto(false);
        }
        return new BaseDto();
    }

    private BaseDto<BaseDataDto> failBaseDto(String errorMessage) {
        BaseDataDto dataDto = new BaseDataDto(false, errorMessage);
        return new BaseDto<>(false, dataDto);
    }

    private boolean isSuccess(Tx3ResVO tx3ResVO) {
        return tx3ResVO != null && tx3ResVO.getHead() != null && "60000000".equals(tx3ResVO.getHead().getRetCode());
    }

    private boolean hasAnxinAccount(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && StringUtils.isNotEmpty(anxinProp.getAnxinUserId());
    }

    @Override
    public byte[] downContractByContractNo(String contractNo) {
        byte[] contract = null;
        try {
            contract = anxinSignConnectService.downLoanContractByBatchNo(contractNo);
        } catch (PKIException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contract;
    }

}

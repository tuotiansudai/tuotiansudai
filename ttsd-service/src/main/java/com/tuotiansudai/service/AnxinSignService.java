package com.tuotiansudai.service;

import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;

public interface AnxinSignService {

    boolean hasAuthed(String loginName);

    AnxinSignPropertyModel getAnxinSignProp(String loginName);

    BaseDto createAccount3001(String loginName);

    BaseDto sendCaptcha3101(String loginName, boolean isVoice);

    BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean isSkipAuth, String ip);

    BaseDto switchSkipAuth(String loginName, boolean open);

    byte[] downContractByContractNo(String contractNo);

    BaseDto updateContractResponse(long loanId,AnxinContractType anxinContractType);

    BaseDto createContracts(long loanId);

    BaseDto createTransferContracts(long transferApplicationId);

}

package com.tuotiansudai.anxin.service;

import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;

import java.util.List;

public interface AnxinSignService {

    boolean hasAuthed(String loginName);

    AnxinSignPropertyModel getAnxinSignProp(String loginName);

    BaseDto createAccount3001(String loginName);

    BaseDto sendCaptcha3101(String loginName, boolean isVoice);

    BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean isSkipAuth, String ip);

    BaseDto switchSkipAuth(String loginName, boolean open);

    byte[] downContractByContractNo(String contractNo);

    BaseDto createLoanContracts(long loanId);

    BaseDto createTransferContracts(long transferApplicationId);

    List<String> queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType);

    BaseDto updateLoanInvestContractNo(long loanId);

    BaseDto updateTransferInvestContractNo(long transferInvestId);

}

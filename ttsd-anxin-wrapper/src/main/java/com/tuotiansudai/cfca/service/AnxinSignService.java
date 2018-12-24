package com.tuotiansudai.cfca.service;


import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.AnxinQueryContractDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;

import java.util.List;

public interface AnxinSignService {

    boolean hasAuthed(String loginName);

    boolean isAuthenticationRequired(String loginName);

    AnxinSignPropertyModel getAnxinSignProp(String loginName);

    BaseDto createAccount3001(String loginName);

    BaseDto sendCaptcha3101(String loginName, boolean isVoice);

    BaseDto verifyCaptcha3102(String loginName, String captcha, boolean isSkipAuth, String ip);

    BaseDto switchSkipAuth(String loginName, boolean open);

    byte[] downContractByContractNo(String contractNo);

    BaseDto<AnxinDataDto> createLoanContracts(long loanId);

    BaseDto<AnxinDataDto> createLoanServiceAgreement(long loanId);

    BaseDto<AnxinDataDto> createTransferContracts(long transferApplicationId);

    boolean queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType);

    BaseDto<AnxinDataDto> queryContract(AnxinQueryContractDto anxinQueryContractDto);

}

package com.tuotiansudai.cfca.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnxinSignConnectService {

    Tx3001ResVO createAccount3001(AccountModel accountModel, UserModel userModel) throws PKIException;

    Tx3101ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException;

    Tx3102ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException;

    Tx3202ResVO generateContractBatch3202(long loanId,String batchNo, List<CreateContractVO> createContractlist) throws PKIException;

}

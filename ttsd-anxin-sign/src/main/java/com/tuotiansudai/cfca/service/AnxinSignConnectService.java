package com.tuotiansudai.cfca.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.response.tx3.*;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;

import java.io.FileNotFoundException;
import java.util.List;

public interface AnxinSignConnectService {

    Tx3ResVO createAccount3001(UserModel userModel) throws PKIException;

    Tx3ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException;

    Tx3ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException;

    Tx3202ResVO generateContractBatch3202(long loanId,String batchNo,AnxinContractType anxinContractType, List<CreateContractVO> createContractList) throws PKIException;

    Tx3202ResVO queryContractByBatchNo(String batchNo) throws PKIException;

    byte[] downLoanContractByBatchNo(String contractNo) throws PKIException, FileNotFoundException;

    List<ContractResponseView> queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType) throws PKIException;

}

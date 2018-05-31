package com.tuotiansudai.cfca.service;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.response.tx3.*;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.UserModel;

import java.io.FileNotFoundException;
import java.util.List;

public interface AnxinSignConnectService {

    Tx3001ResVO createAccount3001(UserModel userModel) throws PKIException;

    Tx3101ResVO sendCaptcha3101(String userId, String projectCode, boolean isVoice) throws PKIException;

    Tx3102ResVO verifyCaptcha3102(String userId, String projectCode, String checkCode) throws PKIException;

    Tx3202ResVO createContractBatch3202(long loanId, String batchNo, AnxinContractType anxinContractType, List<CreateContractVO> createContractList) throws PKIException;

    Tx3211ResVO queryContractBatch3211(long businessId, String batchNo) throws PKIException;

    byte[] downLoanContractByBatchNo(String contractNo) throws PKIException, FileNotFoundException;

    byte[] batchDownLoanContracts(String contractNos) throws PKIException, FileNotFoundException;

    List<ContractResponseView> queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType);

}

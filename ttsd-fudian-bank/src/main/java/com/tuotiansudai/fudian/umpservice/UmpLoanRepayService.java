package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.ProjectTransferRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpLoanRepayDto;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class UmpLoanRepayService {

    private static Logger logger = LoggerFactory.getLogger(UmpLoanRepayService.class);

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    @Autowired
    private UmpLoanRepayService(InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public ProjectTransferRequestModel loanRepay(UmpLoanRepayDto dto) {
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(String.valueOf(dto.getLoanId()),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(dto.getLoanRepayId()), String.valueOf(new Date().getTime())),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()),
                dto.isAdvanceRepay());

        umpUtils.sign(requestModel);

        insertRequestMapper.insertProjectTransfer(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP LOAN REPAY] failed to sign, data: {}", requestModel);
            return null;
        }
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString) {
        ProjectTransferNotifyRequestModel projectTransferNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, new ProjectTransferNotifyRequestModel());
        if (Strings.isNullOrEmpty(projectTransferNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyProjectTransfer(projectTransferNotifyModel);
        return projectTransferNotifyModel.getResponseData();
    }
}
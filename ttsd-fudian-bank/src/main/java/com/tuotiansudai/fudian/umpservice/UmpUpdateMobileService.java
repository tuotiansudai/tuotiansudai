package com.tuotiansudai.fudian.umpservice;

import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.fudian.umpdto.UmpUpdateMobileDto;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class UmpUpdateMobileService {

    private final static Logger logger = LoggerFactory.getLogger(UmpUpdateMobileService.class);

    private final UmpUtils umpUtils;

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateRequestMapper updateRequestMapper;

    private final InsertResponseMapper insertResponseMapper;

    @Autowired
    public UmpUpdateMobileService(UmpUtils umpUtils, InsertRequestMapper insertRequestMapper, UpdateRequestMapper updateRequestMapper, InsertResponseMapper insertResponseMapper){
        this.umpUtils = umpUtils;
        this.insertRequestMapper = insertRequestMapper;
        this.updateRequestMapper = updateRequestMapper;
        this.insertResponseMapper = insertResponseMapper;
    }

    public BankBaseMessage updateMobile(UmpUpdateMobileDto dto){
        MerRegisterPersonRequestModel model = new MerRegisterPersonRequestModel(
                MessageFormat.format("{0}X{1}", String.valueOf(dto.getAccountId()), String.valueOf(new Date().getTime())),
                dto.getLoginName(),
                dto.getUserName(),
                dto.getIdentityNumber(),
                dto.getMobile());

        umpUtils.sign(model);

        insertRequestMapper.insertRegisterPerson(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP UPDATE MOBILE] failed to sign, data: {}", model);
            return new BankBaseMessage();
        }

        updateRequestMapper.updateRegisterPerson(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), model.getField());
        if (responseBody != null) {
            MerRegisterPersonResponseModel responseModel = new MerRegisterPersonResponseModel();
            umpUtils.generateResponse(model.getId(), responseBody, responseModel);
            updateRequestMapper.updateRegisterPerson(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE, model.getId());
            insertResponseMapper.insertResponseRegisterPerson(responseModel);
            return new BankBaseMessage(responseModel.isSuccess(), responseModel.getRetMsg());
        }
        return new BankBaseMessage();
    }
}

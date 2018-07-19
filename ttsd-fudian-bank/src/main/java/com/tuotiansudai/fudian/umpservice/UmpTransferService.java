package com.tuotiansudai.fudian.umpservice;

import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.request.TransferSearchRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UmpTransferService {

    private static Logger logger = LoggerFactory.getLogger(UmpTransferService.class);

    @Autowired
    private UmpUtils umpUtils;
    
    @Autowired
    private InsertRequestMapper insertRequestMapper;

    @Autowired
    private UpdateRequestMapper updateRequestMapper;

    @Autowired
    private InsertResponseMapper insertResponseMapper;

    @Autowired
    private SelectMapper selectMapper;

    public Map<String, String> getTransferStatus(String orderId, String merDate, String businessType) {

        TransferSearchRequestModel model = new TransferSearchRequestModel(orderId,merDate,businessType);

        umpUtils.sign(model);
        insertRequestMapper.insertUmpTransferRequest(model);
        if (model.getField().isEmpty()) {
            logger.error("[UMP TRANSFER_SEARCH] failed to sign, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpTransferSearch(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null) {
            updateRequestMapper.updateUmpTransferSearch(SyncRequestStatus.FAILURE, model.getId());
            logger.error("[UMP TRANSFER_SEARCH] response is empty, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpTransferSearch(SyncRequestStatus.SUCCESS, model.getId());
        TransferSearchResponseModel responseModel = new TransferSearchResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);

        insertResponseMapper.insertResponseUmpTransfer(responseModel);
        if(responseModel.isSuccess()){
            return responseModel.generateHumanReadableInfo();
        }
        return new HashMap<>();

    }

}

package com.tuotiansudai.fudian.umpservice;

import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.ump.sync.request.ProjectAccountSearchRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UmpLoanStatusService {

    private static Logger logger = LoggerFactory.getLogger(UmpLoanStatusService.class);

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

    public Map<String, String> getLoanStatus(long loanId) {

        ProjectAccountSearchRequestModel model = new ProjectAccountSearchRequestModel(String.valueOf(loanId));

        umpUtils.sign(model);
        insertRequestMapper.insertUmpProjectRequest(model);
        if (model.getField().isEmpty()) {
            logger.error("[UMP PROJECT_ACCOUNT_SEARCH] failed to sign, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpProjectAccountSearch(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null) {
            updateRequestMapper.updateUmpUserSearch(SyncRequestStatus.FAILURE, model.getId());
            logger.error("[UMP PROJECT_ACCOUNT_SEARCH] response is empty, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpProjectAccountSearch(SyncRequestStatus.SUCCESS, model.getId());
        ProjectAccountSearchResponseModel responseModel = new ProjectAccountSearchResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);

        insertResponseMapper.insertResponseUmpProjectAccount(responseModel);
        return responseModel.generateHumanReadableInfo();
    }

}

package com.tuotiansudai.fudian.umpservice;

import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.ump.sync.request.UserSearchRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.UserSearchResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UmpUserService {

    private static Logger logger = LoggerFactory.getLogger(UmpUserService.class);

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

    public Map<String, String> getUserStatus(String loginName) {
        String payUserId = selectMapper.selectPayUserId(loginName);
        if (payUserId == null || "".equals(payUserId)) {
            return new HashMap<>();
        }
        UserSearchRequestModel model = new UserSearchRequestModel(payUserId);
        umpUtils.sign(model);
        insertRequestMapper.insertUmpUserSearch(model);
        if (model.getField().isEmpty()) {
            logger.error("[UMP USER SEARCH] failed to sign, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpUserSearch(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null) {
            updateRequestMapper.updateUmpUserSearch(SyncRequestStatus.FAILURE, model.getId());
            logger.error("[UMP USER SEARCH] response is empty, data: {}", model);
            return new HashMap<>();
        }
        updateRequestMapper.updateUmpUserSearch(SyncRequestStatus.SUCCESS, model.getId());
        UserSearchResponseModel responseModel = new UserSearchResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);

        insertResponseMapper.insertResponseUmpUserSearch(responseModel);
        if(responseModel.isSuccess()){
            return responseModel.generateHumanReadableInfo();
        }
        return new HashMap<>();
    }

}

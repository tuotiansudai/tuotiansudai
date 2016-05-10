package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.OperationDataModel;

/**
 * Created by huoxuanbo on 16/5/9.
 */
public interface OperationDataService {
    OperationDataModel getOperationDataFromDatabase();

    OperationDataModel getOperationDataFromRedis();

    void updateRedis(OperationDataModel operationDataModel);
}

package com.tuotiansudai.service;

import com.tuotiansudai.service.impl.OperationDataServiceModel;

/**
 * Created by huoxuanbo on 16/5/9.
 */
public interface OperationDataService {
    OperationDataServiceModel getOperationDataFromDatabase();

    OperationDataServiceModel getOperationDataFromRedis();

    void updateRedis(OperationDataServiceModel operationDataServiceModel);
}

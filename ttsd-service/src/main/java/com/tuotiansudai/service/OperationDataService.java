package com.tuotiansudai.service;

import com.tuotiansudai.dto.OperationDataDto;

/**
 * Created by huoxuanbo on 16/5/9.
 */
public interface OperationDataService {
    OperationDataDto getOperationDataFromDatabase();

    OperationDataDto getOperationDataFromRedis();

    void updateRedis(OperationDataDto operationDataDto);
}

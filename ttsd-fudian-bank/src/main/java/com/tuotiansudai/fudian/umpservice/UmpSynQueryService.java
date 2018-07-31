package com.tuotiansudai.fudian.umpservice;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.TranseqSearchRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.TranseqSearchResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by qduljs2011 on 2018/7/20.
 */
@Service
public class UmpSynQueryService {
    private static Logger logger = LoggerFactory.getLogger(UmpSynQueryService.class);

    private final static int PAGE_SIZE = 10;

    @Autowired
    private UmpUtils umpUtils;

    public <R extends BaseSyncRequestModel, T extends BaseSyncResponseModel> T queryUmpInfo(R requestModel, Class<T> responseModelClass) {
        logger.info("[UmpSynQueryService] 请求开始,data{}",requestModel);
        umpUtils.sign(requestModel);
        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP_SEARCH] failed to sign, data: {}", requestModel);
            return null;
        }
        String responseBody = umpUtils.send(requestModel.getRequestUrl(), (Map<String, String>) requestModel.getField());
        if (responseBody == null) {
            logger.error("[UMP SEARCH] response is empty, data: {}", requestModel);
            return null;
        }
        T responseMode = null;
        try {
            responseMode = responseModelClass.newInstance();
        } catch (Exception e) {
            logger.error("[UMP SEARCH] responseMode instance error, data: {}", responseModelClass);
            e.printStackTrace();
            return null;
        }
        umpUtils.generateResponse(requestModel.getId(), responseBody, responseMode);
        return responseMode;
    }

    public List<List<String>> getUmpTransferBill(String payAccountId, Date startDate, Date endDate) {
        List<List<String>> data = Lists.newArrayList();
        if (StringUtils.isEmpty(payAccountId)) {
            return data;
        }
        try {
            int pageNum = 1;
            int totalNum = 0;
            do {
                TranseqSearchResponseModel responseModel = queryUmpInfo(new TranseqSearchRequestModel(payAccountId, pageNum, startDate, endDate), TranseqSearchResponseModel.class);
                if (responseModel != null && responseModel.isSuccess()) {
                    totalNum = Integer.parseInt(responseModel.getTotalNum());
                    List<List<String>> humanReadableData = responseModel.generateHumanReadableData();
                    data.addAll(humanReadableData);
                    pageNum += 1;
                }
            } while ((totalNum % PAGE_SIZE == 0 ? totalNum / PAGE_SIZE : totalNum / PAGE_SIZE + 1) >= pageNum);

            return data;

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return data;
    }
}

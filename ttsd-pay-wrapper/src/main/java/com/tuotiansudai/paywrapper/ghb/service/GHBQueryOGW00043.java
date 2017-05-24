package com.tuotiansudai.paywrapper.ghb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.tuotiansudai.paywrapper.ghb.client.GHBClient;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestOGW00043;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00043;
import com.tuotiansudai.paywrapper.ghb.repository.mapper.DynamicTableMapper;
import com.tuotiansudai.util.RedissonManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class GHBQueryOGW00043 extends GHBQueryBase {

    private static final Log logger = LogFactory.getLog(GHBQueryOGW00043.class);

    private static final String ASYNC_REQUEST_TABLE = "RequestOGW00042";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DynamicTableMapper dynamicTableMapper;

    private final GHBClient ghbClient;

    @Autowired
    public GHBQueryOGW00043(DynamicTableMapper dynamicTableMapper, GHBClient ghbClient) {
        this.dynamicTableMapper = dynamicTableMapper;
        this.ghbClient = ghbClient;
    }

    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void query() {
        if (!fetchLock()) {
            return;
        }

        List<Map<String, Object>> requestItems = dynamicTableMapper.findProcessingRequest(ASYNC_REQUEST_TABLE);
        logger.info(MessageFormat.format("[GHBQueryOGW00043] OGW00042 requests: {0}", requestItems));

        for (Map<String, Object> requestItem : requestItems) {
            logger.info(MessageFormat.format("[GHBQueryOGW00043] OGW00042 request: {0}", requestItem));
            try {
                DateTime createdTime = new DateTime(DATE_FORMAT.parse(requestItem.get("created_time").toString()));
                if (!requestItem.containsKey("LATEST_RETURN_STATUS_TIME") && createdTime.plusMinutes(10).isBeforeNow()) {
                    logger.info(MessageFormat.format("[GHBQueryOGW00043] OGW00042 request: {0}, must query after ten minutes", requestItem));
                    continue;
                }

                String channelFlow = requestItem.get("header_channelFlow").toString();
                if (Strings.isNullOrEmpty(channelFlow)) {
                    logger.error(MessageFormat.format("[GHBQueryOGW00043] OGW00042 request: {0}, channelFlow is empty", requestItem));
                    continue;
                }

                RequestMessageContent<RequestOGW00043> message = new RequestMessageContent<>(new RequestOGW00043(channelFlow));

                ResponseMessageContent<ResponseOGW00043> messageResponse = ghbClient.invoke(message);

                if (messageResponse == null) {
                    logger.error(MessageFormat.format("[GHBQueryOGW00043] OGW00042 request: {0}, query result is null", requestItem));
                    continue;
                }

                ResponseOGW00043 content = messageResponse.getBody().getContent();

                logger.info(MessageFormat.format("[GHBQueryOGW00043] OGW00042 request: {0}, query status {1}", requestItem, content.getReturn_status()));

                dynamicTableMapper.updateRequestStatus(ASYNC_REQUEST_TABLE, requestItem.get("id").toString(), content.getReturn_status());

                if ("R".equals(content.getReturn_status()) && createdTime.plusMinutes(25).isBeforeNow()) {
                    //R 处理中（客户仍停留在页面操作，25分钟后仍是此状态可置交易为失败）
                    dynamicTableMapper.updateRequestStatus(ASYNC_REQUEST_TABLE, requestItem.get("id").toString(), "F");
                } else {
                    dynamicTableMapper.updateRequestStatus(ASYNC_REQUEST_TABLE, requestItem.get("id").toString(), content.getReturn_status());
                }

                if ("S".equals(content.getReturn_status())) {
                    //TODO: 业务处理
                }
            } catch (ParseException e) {
                logger.error(MessageFormat.format("[GHBQueryOGW00043] request: {0}, created time is invalid", requestItem), e);
            } catch (JsonProcessingException e) {
                logger.error(MessageFormat.format("[GHBQueryOGW00043] request: {0}, create request failed", requestItem), e);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}

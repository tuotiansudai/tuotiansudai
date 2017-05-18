package com.tuotiansudai.paywrapper.ghb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.tuotiansudai.paywrapper.ghb.client.GHBClient;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestOGW00046;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseOGW00046;
import com.tuotiansudai.paywrapper.ghb.repository.mapper.DynamicTableMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class GHBQueryOGW00046 extends GHBQueryBase {

    private static final Log logger = LogFactory.getLog(GHBQueryOGW00046.class);

    private static final String ASYNC_REQUEST_TABLE = "RequestOGW00045";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DynamicTableMapper dynamicTableMapper;

    private final GHBClient ghbClient;

    @Autowired
    public GHBQueryOGW00046(DynamicTableMapper dynamicTableMapper, GHBClient ghbClient) {
        this.dynamicTableMapper = dynamicTableMapper;
        this.ghbClient = ghbClient;
    }

    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void query() {
        if (!fetchLock()) {
            return;
        }

        List<Map<String, Object>> requestItems = dynamicTableMapper.findProcessingRequest(ASYNC_REQUEST_TABLE);
        logger.info(MessageFormat.format("[GHBQueryOGW00046] OGW00045 requests: {0}", requestItems));

        for (Map<String, Object> requestItem : requestItems) {
            logger.info(MessageFormat.format("[GHBQueryOGW00046] OGW00045 request: {0}", requestItem));
            try {
                DateTime createdTime = new DateTime(DATE_FORMAT.parse(requestItem.get("created_time").toString()));
                if (!requestItem.containsKey("LATEST_RETURN_STATUS_TIME") && createdTime.plusMinutes(5).isBeforeNow()) {
                    logger.info(MessageFormat.format("[GHBQueryOGW00046] OGW00045 request: {0}, must query after five minutes", requestItem));
                    continue;
                }

                String channelFlow = requestItem.get("header_channelFlow").toString();
                if (Strings.isNullOrEmpty(channelFlow)) {
                    logger.error(MessageFormat.format("[GHBQueryOGW00046] OGW00045 request: {0}, channelFlow is empty", requestItem));
                    continue;
                }

                RequestMessageContent<RequestOGW00046> message = new RequestMessageContent<>(new RequestOGW00046(channelFlow));

                ResponseMessageContent<ResponseOGW00046> messageResponse = ghbClient.invoke(message);

                if (messageResponse == null) {
                    logger.error(MessageFormat.format("[GHBQueryOGW00046] OGW00045 request: {0}, query result is null", requestItem));
                    continue;
                }

                ResponseOGW00046 content = messageResponse.getBody().getContent();

                logger.info(MessageFormat.format("[GHBQueryOGW00046] OGW00045 request: {0}, query status {1}", requestItem, content.getReturn_status()));

                dynamicTableMapper.updateRequestStatus(ASYNC_REQUEST_TABLE, requestItem.get("id").toString(), content.getReturn_status());

                if ("R".equals(content.getReturn_status()) && createdTime.plusMinutes(25).isBeforeNow()) {
                    //R 处理中（客户仍停留在页面操作，25分钟后仍是此状态可置交易为失败）
                    dynamicTableMapper.updateRequestStatus(ASYNC_REQUEST_TABLE, requestItem.get("id").toString(), "F");
                }

                if ("P".equals(content.getReturn_status())) {
                    //P 预授权成功（目前未到账，下一工作日到账，当天无需再进行查询，下一工作日上午6点再进行查询，状态会变成S，如状态不变则也无需再查询，可在下一工作日在对账文件中确认交易状态。）
                    //TODO: 业务处理
                }

                if ("D".equals(content.getReturn_status())) {
                    //D 后台支付系统处理中（如果 ERRORMSG值为“ORDER_CREATED”，并超过25分钟未变，则可置交易是失败。其他情况商户需再次发查询接口。但2小时后状态仍未变的则可置为异常，无需再发起查询，后续在对账文件中确认交易状态或线下人工处理）

                }

                if ("S".equals(content.getReturn_status())) {
                    //S 成功
                    //TODO: 业务处理
                }
            } catch (ParseException e) {
                logger.error(MessageFormat.format("[GHBQueryOGW00046] request: {0}, created time is invalid", requestItem), e);
            } catch (JsonProcessingException e) {
                logger.error(MessageFormat.format("[GHBQueryOGW00046] request: {0}, create request failed", requestItem), e);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}

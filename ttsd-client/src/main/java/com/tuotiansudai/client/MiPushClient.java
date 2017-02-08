package com.tuotiansudai.client;

import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.PushMessage;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class MiPushClient {

    static Logger logger = Logger.getLogger(MiPushClient.class);

    private Sender androidSender;

    private Sender iosSender;

    public MiPushClient(@Value("${common.mipush.appSecretKey.android}") String appSecretKeyAndroid,
                        @Value("${common.mipush.appSecretKey.ios}") String appSecretKeyIOS) {
        Constants.useOfficial();
        this.androidSender = new Sender(appSecretKeyAndroid);
        this.iosSender = new Sender(appSecretKeyIOS);
    }

    public void sendPushMessage(PushMessage pushMessage) throws Exception {

        logger.info(MessageFormat.format("send push message start. user count:{0}, source:{1}",
                pushMessage.getLoginNames() == null ? "ALL" : pushMessage.getLoginNames().size(), pushMessage.getPushSource()));

        if (pushMessage.getPushSource() == PushSource.ANDROID || pushMessage.getPushSource() == PushSource.ALL) {
            Message message = new Message.Builder()
                    .title("拓天速贷")
                    .description(pushMessage.getContent())
                    .payload(pushMessage.getContent())
                    .notifyType(-1)
                    .build();
            sendPushMessage(pushMessage, message, PushSource.ANDROID);
        }

        if (pushMessage.getPushSource() == PushSource.IOS || pushMessage.getPushSource() == PushSource.ALL) {
            Message message = new Message.IOSBuilder()
                    .title("拓天速贷")
                    .body(pushMessage.getContent())
                    .soundURL("default")
                    .build();
            sendPushMessage(pushMessage, message, PushSource.IOS);
        }
        logger.info(MessageFormat.format("send push message end. user count:{0}, source:{1}",
                pushMessage.getLoginNames() == null ? "ALL" : pushMessage.getLoginNames().size(), pushMessage.getPushSource()));
    }

    private void sendPushMessage(PushMessage pushMessage, Message message, PushSource source) throws IOException, ParseException {

        Sender sender = source == PushSource.ANDROID ? androidSender : iosSender;

        if (CollectionUtils.isEmpty(pushMessage.getLoginNames())) {
            // 如果alias list 为空，则给所有用户发推送
            logger.info("send push message to all " + source + " users.");

            // 给所有的 IOS/Android 用户发推送
            Result result = sender.broadcastAll(message, 2);
            logger.info(MessageFormat.format("result: {0}, reason: {1}, messageId: {2}", result.getErrorCode().getDescription(), result.getReason(), result.getMessageId()));
        } else {
            logger.info("send push message to " + source + " users, user count:" + pushMessage.getLoginNames().size());
            sendToAliasInBatch(pushMessage, message, sender);
        }
    }

    private void sendToAliasInBatch(PushMessage pushMessage, Message message, Sender sender) throws IOException, ParseException {

        List<String> aliasList = pushMessage.getLoginNames();
        int count = aliasList.size();
        int batchSize = count / 1000 + (count % 1000 > 0 ? 1 : 0);

        for (int batch = 0; batch < batchSize; batch++) {
            logger.info("batch number: " + batch);

            Result result = sender.sendToAlias(message, aliasList.subList(batch * 1000,
                    (batch + 1) * 1000 > count ? count : (batch + 1) * 1000), 2);

            logger.info(MessageFormat.format("batch number: {0}, result: {1}, reason: {2}, messageId: {3}", batch,
                    result.getErrorCode().getDescription(), result.getReason(), result.getMessageId()));
        }
    }

}

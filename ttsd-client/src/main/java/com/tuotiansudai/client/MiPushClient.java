package com.tuotiansudai.client;

import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.PushMessage;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class MiPushClient {

    static Logger logger = Logger.getLogger(MiPushClient.class);

    private Sender androidSender;

    private Sender iosSender;

    private Environment environment;

    final static private String PUSH_KEY_JUMP_TO = "jump_to";

    public MiPushClient(@Value("${common.mipush.appSecretKey.android}") String appSecretKeyAndroid,
                        @Value("${common.mipush.appSecretKey.ios}") String appSecretKeyIOS,
                        @Value("${common.environment}") Environment environment) {
        this.environment = environment;
        this.androidSender = new Sender(appSecretKeyAndroid);
        this.iosSender = new Sender(appSecretKeyIOS);
    }

    public void sendPushMessage(PushMessage pushMessage) {

        StringBuilder userListStr = new StringBuilder();
        if (pushMessage.getLoginNames() != null)
            pushMessage.getLoginNames().forEach(userListStr::append);
        else
            userListStr.append("ALL");

        logger.info(MessageFormat.format("send push message start. user count:{0}, source:{1}, type:{2}, content:{3}, user-list:{4}",
                pushMessage.getLoginNames() == null ? "ALL" : pushMessage.getLoginNames().size(), pushMessage.getPushSource(), pushMessage.getPushType(), pushMessage.getContent(), userListStr));

        if (pushMessage.getPushSource() == PushSource.ANDROID || pushMessage.getPushSource() == PushSource.ALL) {
            Message message = new Message.Builder()
                    .title("拓天速贷")
                    .description(pushMessage.getContent())
                    .payload(pushMessage.getContent())
                    .notifyType(-1)
                    .extra(PUSH_KEY_JUMP_TO, pushMessage.getJumpTo().getPath())
                    .build();
            // 安卓推送，测试环境和正式环境，都使用official（配置了不同的小米平台账户）
            Constants.useOfficial();
            sendPushMessage(pushMessage, message, PushSource.ANDROID);
        }

        if (pushMessage.getPushSource() == PushSource.IOS || pushMessage.getPushSource() == PushSource.ALL) {
            Message message = new Message.IOSBuilder()
                    .title("拓天速贷")
                    .body(pushMessage.getContent())
                    .soundURL("default")
                    .extra(PUSH_KEY_JUMP_TO, pushMessage.getJumpTo().getPath())
                    .build();
            // 针对IOS推送，需要根据环境来设置是否使用sandbox，安卓则无需这样
            if (environment != Environment.PRODUCTION) {
                Constants.useSandbox();
            } else {
                Constants.useOfficial();
            }
            sendPushMessage(pushMessage, message, PushSource.IOS);
        }
        logger.info("send push message end.");
    }

    private void sendPushMessage(PushMessage pushMessage, Message message, PushSource source) {

        Sender sender = source == PushSource.ANDROID ? androidSender : iosSender;

        if (CollectionUtils.isEmpty(pushMessage.getLoginNames())) {
            // 如果alias list 为空，则给所有用户发推送
            logger.info("send push message to all " + source + " users.");

            try {
                // 给所有的 IOS/Android 用户发推送
                Result result = sender.broadcastAll(message, 2);
                logger.info(MessageFormat.format("result: {0}, reason: {1}, messageId: {2}", result.getErrorCode().getDescription(), result.getReason(), result.getMessageId()));
            } catch (Exception e) {
                logger.error("send push message to all " + source + " users fail.", e);
            }
        } else {
            logger.info("send push message to " + source + " users, user count:" + pushMessage.getLoginNames().size());
            sendToAliasInBatch(pushMessage, message, sender);
        }
    }

    private void sendToAliasInBatch(PushMessage pushMessage, Message message, Sender sender) {

        List<String> aliasList = pushMessage.getLoginNames();
        int count = aliasList.size();
        int batchSize = count / 1000 + (count % 1000 > 0 ? 1 : 0);

        try {
            for (int batch = 0; batch < batchSize; batch++) {
                logger.info("batch number: " + batch);
                Result result = sender.sendToAlias(message, aliasList.subList(batch * 1000,
                        (batch + 1) * 1000 > count ? count : (batch + 1) * 1000), 2);

                logger.info(MessageFormat.format("batch size:{0}, batch number: {1}, result: {2}, reason: {3}, messageId: {4}", count,
                        batch, result.getErrorCode().getDescription(), result.getReason(), result.getMessageId()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("send mi push in batch fail, batch size:{1}", count), e);
        }
    }
}

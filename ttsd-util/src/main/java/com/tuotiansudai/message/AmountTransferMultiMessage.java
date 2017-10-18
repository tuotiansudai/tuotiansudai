package com.tuotiansudai.message;

import java.util.Arrays;
import java.util.List;

public class AmountTransferMultiMessage {

    private List<AmountTransferMessage> messageList;

    public AmountTransferMultiMessage() {

    }

    public AmountTransferMultiMessage(AmountTransferMessage... atms) {
        messageList = Arrays.asList(atms);
    }

    public List<AmountTransferMessage> getMessageList() {
        return messageList;
    }
}

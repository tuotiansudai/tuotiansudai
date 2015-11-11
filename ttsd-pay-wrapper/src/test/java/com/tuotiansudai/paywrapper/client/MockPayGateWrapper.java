package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.paywrapper.client.impl.PayGateWrapperFakeImpl;

public class MockPayGateWrapper extends PayGateWrapperFakeImpl {

    private static final MockPayGateWrapper instance = new MockPayGateWrapper();

    public MockPayGateWrapper() {
        super("");
    }

    public static void injectInto(PaySyncClient paySyncClient) {
        paySyncClient.payGateWrapper = instance;
    }

    public static void injectInto(PayAsyncClient payAsyncClient) {
        payAsyncClient.payGateWrapper = instance;
    }

    public static void setUrl(String requestUrl) {
        instance.setFakeServiceUrl(requestUrl);
    }
}

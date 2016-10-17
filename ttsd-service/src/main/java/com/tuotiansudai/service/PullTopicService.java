package com.tuotiansudai.service;

/**
 * Created by hourglasskoala on 16/10/14.
 */
public interface PullTopicService {

    void broadcast(String registerSuccessBody);

    void processAsyncRedEnvelope();

    void processSynchRedEnvelope();

}

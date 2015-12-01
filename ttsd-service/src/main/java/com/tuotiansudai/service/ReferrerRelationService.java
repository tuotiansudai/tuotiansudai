package com.tuotiansudai.service;

import com.tuotiansudai.exception.ReferrerRelationException;

public interface ReferrerRelationService {

    void generateRelation(String newReferrerLoginName, String loginName) throws ReferrerRelationException;
}

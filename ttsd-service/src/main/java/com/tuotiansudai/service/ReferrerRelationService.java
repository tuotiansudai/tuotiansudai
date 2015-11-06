package com.tuotiansudai.service;

import com.tuotiansudai.exception.EditUserException;

public interface ReferrerRelationService {

    void generateRelation(String referrerLoginName, String loginName);

    void updateRelation(String newReferrerLoginName, String loginName) throws EditUserException;
}

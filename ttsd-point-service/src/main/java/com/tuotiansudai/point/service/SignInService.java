package com.tuotiansudai.point.service;


import com.tuotiansudai.point.repository.dto.SignInPointDto;

public interface SignInService {

    SignInPointDto signIn(String loginName);

    boolean signInIsSuccess(String loginName);

    SignInPointDto getLastSignIn(String loginName);

    int getNextSignInPoint(String loginName);

    int getSignInCount(String loginName);

}

package com.tuotiansudai.point.service;


import com.tuotiansudai.point.dto.SignInPointDto;

public interface SignInService {

    SignInPointDto signIn(String loginName);

    boolean signInIsSuccess(String loginName);

}

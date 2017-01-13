package com.tuotiansudai.rest.exceptions;

public interface ErrorCode {
    /**
     * 错误代码编号由不超过6位的数字组成: 1-99999 为系统错误或通用错误代码，100000-999999 为各应用自定义错误代码
     * 错误代码编号规则：应用标识(前两位)，模块标识(中两位)，错误代码(末两位)
     * 应用标识： 00-99
     * 模块标识： 00-99
     * 错误代码： 00-99
     *
     * 正确的代码如： 100001，113242， 999999
     * 错误的代码如： 12345678
     *
     * @return
     */
    int getCode();

    String getMessage();
}

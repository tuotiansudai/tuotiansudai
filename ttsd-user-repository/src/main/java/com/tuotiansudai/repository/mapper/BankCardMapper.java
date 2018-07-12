package com.tuotiansudai.repository.mapper;

import org.springframework.stereotype.Repository;

/**
 * Created by qduljs2011 on 2018/7/12.
 */
@Repository
public interface BankCardMapper {
    String findPassedBankCardNumberByLoginName(String loginName);
}

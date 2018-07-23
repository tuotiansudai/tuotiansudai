package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import org.springframework.stereotype.Repository;

/**
 * Created by qduljs2011 on 2018/7/12.
 */
@Repository
public interface BankCardMapper {

    BankCardModel findPassedBankCardByLoginName(String loginName);
}

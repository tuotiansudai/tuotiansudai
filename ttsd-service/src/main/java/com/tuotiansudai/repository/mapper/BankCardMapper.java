package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import org.apache.ibatis.annotations.Param;

public interface BankCardMapper {

    void create(BankCardModel model);

    BankCardModel findById(long id);

    BankCardModel findByLoginName(String loginName);

    void update(BankCardModel bankCardModel);
}

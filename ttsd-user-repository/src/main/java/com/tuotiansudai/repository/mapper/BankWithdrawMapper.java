package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankWithdrawModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BankWithdrawMapper {

    void create(BankWithdrawModel bankWithdrawModel);

    BankWithdrawModel findById(long id);

    void update(BankWithdrawModel bankWithdrawModel);
}

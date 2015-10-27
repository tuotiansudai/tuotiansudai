package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.WithdrawModel;

public interface WithdrawMapper {

    void create(WithdrawModel withdrawModel);

    WithdrawModel findById(long id);

    void update(WithdrawModel withdrawModel);

    long findSumWithdrawByLoginName(String loginName);

}

package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import org.apache.ibatis.annotations.Param;

public interface WithdrawMapper {

    void create(WithdrawModel withdrawModel);

    WithdrawModel findById(long id);

    void update(WithdrawModel withdrawModel);

}

package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SystemBillModel;

import java.util.List;

public interface SystemBillMapper {

    void create(SystemBillModel model);

    List<SystemBillModel> getLastestSystemBill();

}

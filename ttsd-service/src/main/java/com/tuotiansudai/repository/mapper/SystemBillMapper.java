package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SystemBillModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemBillMapper {

    void create(SystemBillModel model);

    List<SystemBillModel> getLastestSystemBill();

}

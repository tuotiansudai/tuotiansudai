package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SystemRechargeModel;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemRechargeMapper {

    void create(SystemRechargeModel model);

    SystemRechargeModel findById(long id);

    void updateSystemRecharge(SystemRechargeModel systemRechargeModel);

    List<SystemRechargeModel> findByLoginName(String loginName);

}

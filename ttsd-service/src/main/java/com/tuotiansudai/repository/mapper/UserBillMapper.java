package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBillModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBillMapper {

    void create(UserBillModel userBillModel);

    List<UserBillModel> findByLoginName(String loginName);
}

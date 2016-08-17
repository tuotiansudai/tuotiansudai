package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.UserAddressModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressMapper {

    void create(UserAddressModel userAddressModel);

    void update(UserAddressModel userAddressModel);

    List<UserAddressModel> findByLoginName(@Param("loginName") String loginName);
}

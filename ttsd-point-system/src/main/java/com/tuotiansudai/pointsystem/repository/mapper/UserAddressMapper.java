package com.tuotiansudai.pointsystem.repository.mapper;


import com.tuotiansudai.pointsystem.repository.model.UserAddressModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressMapper {

    void create(UserAddressModel userAddressModel);

    UserAddressModel findByLoginName(@Param("loginName") String loginName);
}

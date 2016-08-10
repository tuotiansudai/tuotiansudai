package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.UserAddressModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressMapper {

    void create(UserAddressModel userAddressModel);

    UserAddressModel findByLoginName(@Param("loginName") String loginName);
}

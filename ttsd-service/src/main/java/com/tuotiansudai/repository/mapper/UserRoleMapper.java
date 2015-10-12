package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {

    List<UserRoleModel> findByLoginName(String loginName);

    void delete(@Param("loginName") String loginName);

    void createUserRoles(@Param("userRoles") List<UserRoleModel> userRoleModels);
}

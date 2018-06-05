package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BankAccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountMapper {

    void create(BankAccountModel bankAccountModel);

    void update(BankAccountModel bankAccountModel);

    BankAccountModel findByLoginName(String loginName);

    void updateMembershipPoint(@Param("loginName") String loginName,
                               @Param("point") long point);

    void updateAutoInvest(@Param("loginName") String loginName,
                          @Param("autoInvest") boolean autoInvest);

    void updateBalance(@Param("loginName") String loginName,
                       @Param("balanceDelta") long balanceDelta);
}

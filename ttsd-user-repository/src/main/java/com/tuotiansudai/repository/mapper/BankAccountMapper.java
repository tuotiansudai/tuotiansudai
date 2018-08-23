package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.BankAccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountMapper {

    void createInvestor(BankAccountModel bankAccountModel);

    void createLoaner(BankAccountModel bankAccountModel);

    void update(BankAccountModel bankAccountModel);

    BankAccountModel findByLoginNameAndRole(@Param(value = "loginName") String loginName,
                                            @Param(value = "roleType") Role roleType);

    BankAccountModel lockInvestorByLoginName(String loginName);

    void updateMembershipPoint(@Param("loginName") String loginName,
                               @Param("point") long point);

    void updateAutoInvest(@Param("loginName") String loginName,
                          @Param("autoInvest") boolean autoInvest);

    void updateInvestorBalance(@Param("loginName") String loginName,
                               @Param("balanceDelta") long balanceDelta);

    void updateLoanerBalance(@Param("loginName") String loginName,
                             @Param("balanceDelta") long balanceDelta);

    void updateBankMobileByLoginNameAndAccountNo(@Param("loginName")String loginName, @Param("bankAccountNo")String bankAccountNo, @Param("bankMobile")String bankMobile);
}

package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BankAccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountMapper {

    void createInvestor(BankAccountModel bankAccountModel);

    void createLoaner(BankAccountModel bankAccountModel);

    void update(BankAccountModel bankAccountModel);

    BankAccountModel findInvestorByLoginName(String loginName);

    BankAccountModel findLoanerByLoginName(String loginName);

    BankAccountModel lockInvestorByLoginName(String loginName);

    BankAccountModel lockLoanerByLoginName(String loginName);

    void updateMembershipPoint(@Param("loginName") String loginName,
                               @Param("point") long point);

    void updateAutoInvest(@Param("loginName") String loginName,
                          @Param("autoInvest") boolean autoInvest);

    void updateInvestorBalance(@Param("loginName") String loginName,
                               @Param("balanceDelta") long balanceDelta);

    void updateLoanerBalance(@Param("loginName") String loginName,
                             @Param("balanceDelta") long balanceDelta);
}

package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardMapper {

    void create(BankCardModel model);

    BankCardModel findById(long id);

    BankCardModel findPassedBankCardByLoginName(String loginName);

    BankCardModel findByLoginNameAndIsFastPayOn(String loginName);

    void update(BankCardModel bankCardModel);

    BankCardModel findPassedBankCardByBankCode(String bankCode);

    List<BankCardModel> findApplyBankCardByLoginName(String loginName);

    int findCountReplaceBankCardByLoginName(@Param(value = "loginName") String loginName);

    List<BankCardModel> findReplaceBankCardByLoginName(@Param(value = "loginName") String loginName,
                                                       @Param(value = "index") Integer index,
                                                       @Param(value = "pageSize") Integer pageSize);
}

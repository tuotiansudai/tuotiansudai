package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardMapper {

    void create(BankCardModel model);

    BankCardModel findById(long id);

    BankCardModel findPassedById(long id);

    BankCardModel findPassedBankCardByLoginName(String loginName);

    BankCardModel findByLoginNameAndIsFastPayOn(String loginName);

    void update(BankCardModel bankCardModel);

    BankCardModel findPassedBankCardByBankCode(String bankCode);

    List<BankCardModel> findApplyBankCardByLoginName(String loginName);
}

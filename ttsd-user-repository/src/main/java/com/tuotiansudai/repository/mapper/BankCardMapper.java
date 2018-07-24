package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qduljs2011 on 2018/7/12.
 */
@Repository
public interface BankCardMapper {

    void create(BankCardModel model);

    void updateStatusAndBankCode(@Param(value = "id") long id,
                                 @Param(value = "status") BankCardStatus status,
                                 @Param(value = "bankCode") String bankCode);

    BankCardModel findById(long id);

    BankCardModel findPassedBankCardByLoginName(String loginName);

    List<BankCardModel> findApplyBankCardByLoginName(String loginName);

}

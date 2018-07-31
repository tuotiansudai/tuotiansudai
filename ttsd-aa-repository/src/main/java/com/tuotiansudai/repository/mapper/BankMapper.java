package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMapper {

    BankModel findById(long id);

    void update(BankModel model);

    List<BankModel> findBankList(@Param(value = "singleAmount") Long singleAmount,
                                 @Param(value = "singleDayAmount") Long singleDayAmount);

    List<BankModel> findUmpBankList(@Param(value = "singleAmount") Long singleAmount,
                                    @Param(value = "singleDayAmount") Long singleDayAmount);

    BankModel findByBankCode(@Param(value = "bankCode") String bankCode);
}

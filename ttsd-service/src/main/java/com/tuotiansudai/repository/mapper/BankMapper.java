package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankMapper {

    BankModel findById(long id);

    void update(BankModel model);

    List<BankModel> findBankList();
}

package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class LoanTitleMapperTest {

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private IdGenerator idGenerator;

    public void createLoanTitleTest(){
        List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
        for(int i=0;i<5;i++){
            LoanTitleModel loanTitleModel = new LoanTitleModel();
            loanTitleModel.setId(new BigInteger(String.valueOf(idGenerator.generate())));
            loanTitleModel.setLoanId(new BigInteger(""));
            loanTitleModel.setTitleId(new BigInteger("1234567890"));
            loanTitleModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleModelList.add(loanTitleModel);
        }
        loanTitleMapper.createLoanTitle(loanTitleModelList);
    }
}

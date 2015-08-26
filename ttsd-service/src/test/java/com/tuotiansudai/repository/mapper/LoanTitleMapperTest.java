package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelation;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanTitleMapperTest {
    @Autowired
    LoanTitleMapper loanTitleMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void createLoanTitleTest(){
        List<LoanTitleRelation> loanTitleRelationList = new ArrayList<LoanTitleRelation>();
        long loanId = 186598028689408l;
        for (int i = 0; i < 1; i++) {
            LoanTitleRelation loanTitleRelation = new LoanTitleRelation();
            loanTitleRelation.setId(idGenerator.generate());
            loanTitleRelation.setLoanId(loanId);
            loanTitleRelation.setTitleId(Long.parseLong("1234567890"));
            loanTitleRelation.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationList.add(loanTitleRelation);
        }
        loanTitleMapper.create(loanTitleRelationList);
        assertTrue(loanTitleMapper.findLoanTitleByLoanId(loanId).size() > 0);
    }

}

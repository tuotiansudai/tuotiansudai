package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelationModel;
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
public class LoanTitleRelationMapperTest {
    @Autowired
    LoanTitleRelationMapper loanTitleRelationMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void createLoanTitleTest(){
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        long loanId = 186598028689408l;
        for (int i = 0; i < 1; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanId);
            loanTitleRelationModel.setTitleId(Long.parseLong("1234567890"));
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanTitleRelationMapper.create(loanTitleRelationModelList);
        assertTrue(loanTitleRelationMapper.findByLoanId(loanId).size() > 0);
    }

}

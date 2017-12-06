package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanTitleType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})@Transactional
public class LoanTitleMapperTest {
    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Test
    public void createTitleTest(){
        LoanTitleModel loanTitleModel = new LoanTitleModel(IdGenerator.generate(), LoanTitleType.BASE_TITLE_TYPE, "房产证");
        assertNotNull(loanTitleModel.getId());
    }

    @Test
    public void findAllTitlesTest(){
        LoanTitleModel loanTitleModel = new LoanTitleModel(IdGenerator.generate(), LoanTitleType.BASE_TITLE_TYPE, "身份证");
        loanTitleMapper.create(loanTitleModel);
        List<LoanTitleModel> loanTitleModels = loanTitleMapper.findAll();
        assertTrue(loanTitleModels.size() >= 0);
    }

}

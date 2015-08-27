package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class LoanTitleMapperTest {
    @Autowired
    private LoanTitleMapper loanTitleMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void createTitleTest(){
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long id = idGenerator.generate();
        loanTitleModel.setId(id);
        loanTitleModel.setType("base");
        loanTitleModel.setTitle("房产证");
        loanTitleMapper.create(loanTitleModel);
        assertNotNull(loanTitleMapper.findById(id));
    }

    @Test
    public void findAllTitlesTest(){
        List<LoanTitleModel> loanTitleModels = loanTitleMapper.findAll();
        assertTrue(loanTitleModels.size() >= 0);
    }
}

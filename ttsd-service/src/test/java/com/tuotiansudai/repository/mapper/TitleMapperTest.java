package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class TitleMapperTest {
    @Autowired
    private TitleMapper titleMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void createTitleTest(){
        TitleModel titleModel = new TitleModel();
        long id = idGenerator.generate();
        titleModel.setId(new BigInteger(String.valueOf(id)));
        titleModel.setType("base");
        titleModel.setTitle("房产证");
        titleMapper.createTitle(titleModel);
        assertTrue(titleModel.getId().equals(id));
    }

    @Test
    public void findAllTitlesTest(){
        List<TitleModel> titleModels = titleMapper.findAllTitles();
        assertTrue(titleModels.size() >= 0);
    }
}

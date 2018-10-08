package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.HelpCategory;
import com.tuotiansudai.repository.model.HelpCenterModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class HelpCenterMapperTest {
    @Autowired
    HelpCenterMapper helpCenterMapper;

    private void prepareData() {
        HelpCenterModel helpCenterModel1  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题1", "回答问题1", HelpCategory.KNOW_TTSD, false);
        helpCenterMapper.create(helpCenterModel1);
        HelpCenterModel helpCenterModel2  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题2", "回答问题2", HelpCategory.ACCOUNT_BANK_CARD, false);
        helpCenterMapper.create(helpCenterModel2);
        HelpCenterModel helpCenterModel3  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题3", "回答问题3", HelpCategory.KNOW_TTSD, true);
        helpCenterMapper.create(helpCenterModel3);
        HelpCenterModel helpCenterModel4  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题4", "回答问题4", HelpCategory.INVEST_REPAY, false);
        helpCenterMapper.create(helpCenterModel4);
        HelpCenterModel helpCenterModel5  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题5", "回答问题5", HelpCategory.KNOW_TTSD, true);
        helpCenterMapper.create(helpCenterModel5);
        HelpCenterModel helpCenterModel6  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题6", "回答问题6", HelpCategory.POINT_MEMBERSHIP, false);
        helpCenterMapper.create(helpCenterModel6);
        HelpCenterModel helpCenterModel7  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题7", "回答问题7", HelpCategory.RELIEVED_SIGN, false);
        helpCenterMapper.create(helpCenterModel7);
        HelpCenterModel helpCenterModel8  = new HelpCenterModel(IdGenerator.generate(), "Test提问问题标题8", "回答问题8", HelpCategory.SECURITY, false);
        helpCenterMapper.create(helpCenterModel8);
    }

    @Test
    public void testFindAllHelpCenterIsHot() throws Exception {
        prepareData();
        List<HelpCenterModel> helpCenterModelList = helpCenterMapper.findAllHelpCenterByTitleOrCategoryOrHot("Test", null, "true");
        assertEquals(2, helpCenterModelList.size());

    }

    @Test
    public void testFindAllHelpCenterOneCategory() throws Exception {
        prepareData();
        List<HelpCenterModel> helpCenterModelList = helpCenterMapper.findAllHelpCenterByTitleOrCategoryOrHot("Test", "KNOW_TTSD", "false");
        assertEquals(3, helpCenterModelList.size());

    }

    @Test
    public void testFindAllHelpCenterHasKeywords() throws Exception {
        prepareData();
        List<HelpCenterModel> helpCenterModelList = helpCenterMapper.findAllHelpCenterByTitleOrCategoryOrHot("Test", "", "false");
        assertEquals(8, helpCenterModelList.size());

    }
}

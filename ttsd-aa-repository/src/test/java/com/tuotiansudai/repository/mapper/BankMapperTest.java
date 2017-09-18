package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BankMapperTest {

    @Autowired
    private BankMapper bankMapper;

    @Test
    public void shouldUpdateBank() throws Exception {

        BankModel bankModel = new BankModel();
        bankModel.setId(1001L);
        bankModel.setSeq(1);
        bankModel.setSingleAmount(10000);
        bankModel.setSingleDayAmount(100000);
        bankModel.setUpdatedBy("test");
        bankModel.setUpdatedTime(new Date());

        bankMapper.update(bankModel);
        BankModel bankModel1 = bankMapper.findById(1001L);

        assertThat(bankModel1.getSingleAmount(), is(10000L));
        assertThat(bankModel1.getSingleDayAmount(), is(100000L));

    }

}

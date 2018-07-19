package com.tuotiansudai.fudian.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryUserServiceTest {

    @Autowired
    private UpdateMapper updateMapper;

    @Test
    public void name() {
        LoanCallbackInvestItemContentDto iterm1 = new LoanCallbackInvestItemContentDto();
        iterm1.setOrderNo("20180604000000000321");
        iterm1.setRetCode("0000");
        iterm1.setRetMsg("交易成功");

        LoanCallbackInvestItemContentDto iterm2 = new LoanCallbackInvestItemContentDto();
        iterm1.setOrderNo("20180604000000000322");
        iterm1.setRetCode("0000");
        iterm1.setRetMsg("交易成功");
        updateMapper.updateLoanCallbackInvestItems(Lists.newArrayList(iterm1, iterm2));
    }
}

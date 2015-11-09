package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppInvestListServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class MobileAppInvestListServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppInvestListServiceImpl mobileAppInvestListService;
    @Autowired
    private IdGenerator idGenerator;
    @Mock
    private InvestMapper investMapper;
    @Test
    public void shouldGenerateInvestListIsOk(){

        InvestModel investModel1 = new InvestModel();
        investModel1.setAmount(1000000L);
        investModel1.setCreatedTime(new Date());
        investModel1.setId(idGenerator.generate());
        investModel1.setIsAutoInvest(false);
        investModel1.setLoginName("loginName1");
        investModel1.setLoanId(idGenerator.generate());
        investModel1.setSource(Source.ANDROID);
        investModel1.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel2 = new InvestModel();
        investModel2.setAmount(1100000L);
        investModel2.setCreatedTime(new Date());
        investModel2.setId(idGenerator.generate());
        investModel2.setIsAutoInvest(false);
        investModel2.setLoginName("loginName2");
        investModel2.setLoanId(idGenerator.generate());
        investModel2.setSource(Source.WEB);
        investModel2.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel3 = new InvestModel();
        investModel3.setAmount(1200000L);
        investModel3.setCreatedTime(new Date());
        investModel3.setId(idGenerator.generate());
        investModel3.setIsAutoInvest(false);
        investModel3.setLoginName("loginName3");
        investModel3.setLoanId(idGenerator.generate());
        investModel3.setSource(Source.IOS);
        investModel3.setStatus(InvestStatus.SUCCESS);

        List<InvestModel> investModels = Lists.newArrayList();
        investModels.add(investModel1);
        investModels.add(investModel2);
        investModels.add(investModel3);



        when(investMapper.findByStatus(anyLong(), anyInt(), anyInt(), any(InvestStatus.class))).thenReturn(investModels);

        when(investMapper.findCountByStatus(anyLong(), any(InvestStatus.class))).thenReturn(3L);

        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        investListRequestDto.setLoanId("1111");
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        BaseResponseDto<InvestListResponseDataDto> baseResponseDto = mobileAppInvestListService.generateInvestList(investListRequestDto);

        assertEquals("10000.00", baseResponseDto.getData().getInvestRecord().get(0).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(0).getUserName());
        assertEquals("11000.00",baseResponseDto.getData().getInvestRecord().get(1).getInvestMoney());
        assertEquals("log***",baseResponseDto.getData().getInvestRecord().get(1).getUserName());
        assertEquals("12000.00",baseResponseDto.getData().getInvestRecord().get(2).getInvestMoney());
        assertEquals("log***",baseResponseDto.getData().getInvestRecord().get(2).getUserName());


    }


}

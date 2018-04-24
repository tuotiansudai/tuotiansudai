package com.tuotiansudai.fudian.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.dto.request.LoanCallbackInvestItemRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanCallbackServiceTest {

    @Autowired
    private LoanCallbackService loanCallbackService;

    @Test
    public void test() {
        LoanCallbackInvestItemRequestDto item1 = new LoanCallbackInvestItemRequestDto(
                "0.00",
                "0.50",
                "0.00",
                "0.00",
                "UU02624634769241001",
                "UA02624634769281001",
                "20180427000000000021",
                "20180427");

//        LoanCallbackInvestItemRequestDto item2 = new LoanCallbackInvestItemRequestDto(
//                "1.00",
//                "0.00",
//                "0.00",
//                "0.00",
//                "UU02619471098561001",
//                "UA02619471098591001",
//                "20180426000000000017",
//                "20180426");
//
//        LoanCallbackInvestItemRequestDto item3 = new LoanCallbackInvestItemRequestDto(
//                "1.00",
//                "0.00",
//                "0.00",
//                "0.00",
//                "UU02624634769241001",
//                "UA02624634769281001",
//                "20180426000000000018",
//                "20180426");
//
//        LoanCallbackInvestItemRequestDto item4 = new LoanCallbackInvestItemRequestDto(
//                "1.00",
//                "0.00",
//                "0.00",
//                "0.00",
//                "UU02624634769241001",
//                "UA02624634769281001",
//                "20180426000000000019",
//                "20180426");

        ResponseDto responseDto = loanCallbackService.loanCallback("LU02625453517541001", Lists.newArrayList(item1));

        System.out.println(responseDto);
    }
}

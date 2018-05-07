package com.tuotiansudai.fudian.sign;

import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.util.AmountUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SignatureHelperTest {

    @Autowired
    private SignatureHelper signatureHelper;

    @Autowired
    private InsertMapper insertMapper;

    @Test
    public void test() {
//        ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson("{\"apiType\": \"register\"}", ExtMarkDto.class);
//
//        insertMapper.insertRegister(new RegisterRequestDto("", "", ""));

        System.out.println(AmountUtils.toAmount("1"));
        System.out.println(AmountUtils.toAmount("10"));
        System.out.println(AmountUtils.toAmount("100"));
        System.out.println(AmountUtils.toAmount("110"));
        System.out.println(AmountUtils.toAmount("101"));
        insertMapper.insertRegister(new RegisterRequestDto("", "", "", ""));
        ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson("{\"apiType\": \"register\"}", ExtMarkDto.class);
    }
}

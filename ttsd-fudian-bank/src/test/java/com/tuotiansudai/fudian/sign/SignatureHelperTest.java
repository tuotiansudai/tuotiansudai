package com.tuotiansudai.fudian.sign;

import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.util.AmountUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SignatureHelperTest {

    @Autowired
    private SignatureHelper signatureHelper;

    @Autowired
    private InsertMapper insertMapper;

    @Test
    public void test() {
        System.out.println(AmountUtils.toCent("0.11"));




    }
}

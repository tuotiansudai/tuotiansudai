package com.tuotiansudai.fudian.sign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.umpdto.UmpLoanRepayDto;
import org.assertj.core.util.Lists;
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
        UmpLoanRepayDto umpLoanRepayDto = new UmpLoanRepayDto("1", "1", 1, 1, 1, true, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(umpLoanRepayDto);
        UmpLoanRepayDto umpLoanRepayDto1 = gson.fromJson(json, UmpLoanRepayDto.class);


    }
}

package com.tuotiansudai.fudian.sign;

import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignatureHelperTest {

    @Autowired
    private SignatureHelper signatureHelper;

    @Autowired
    private InsertMapper insertMapper;

    @Test
    public void test() {
        insertMapper.insertRegister(new RegisterRequestDto("", "", "", ""));
    }
}

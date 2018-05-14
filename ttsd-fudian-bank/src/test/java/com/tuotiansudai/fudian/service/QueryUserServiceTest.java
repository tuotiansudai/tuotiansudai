package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryUserServiceTest {

    @Autowired
    private QueryUserService queryUserService;

    @Test
    public void name() {
        ResponseDto responseDto = queryUserService.query("UA02615960791501001", "UU02615960791461001", "", "");

        System.out.println(responseDto);
    }
}

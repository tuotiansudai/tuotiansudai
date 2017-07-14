package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CurrentRestClientTest {

    @Autowired
    private CurrentRestClient currentRestClient;

    @Test
    public void shouldCallRestRequest() throws Exception {
        try {
            BaseDto<PayFormDataDto> investForm = currentRestClient.invest(new DepositRequestDto(10000, Source.IOS), "YG2xj1Es");
            System.out.println(investForm);
        } catch (RestException e) {
            e.printStackTrace();
        }
    }

}

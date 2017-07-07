package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ExchangePrize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class LotteryDrawActivityTest {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private ExerciseVSWorkActivityService exerciseVSWorkActivityService;
    


}

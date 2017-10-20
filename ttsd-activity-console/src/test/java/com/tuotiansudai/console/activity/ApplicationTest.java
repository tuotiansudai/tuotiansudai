package com.tuotiansudai.console.activity;


import com.tuotiansudai.console.activity.controller.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private AnnualPrizeController annualPrizeController;
    @Autowired
    private ExportController exportController;
    @Autowired
    private HeadlinesTodayController headlinesTodayController;
    @Autowired
    private HeroRankingController heroRankingController;
    @Autowired
    private IPhone7LotteryController iPhone7LotteryController;
    @Autowired
    private LotteryController lotteryController;
    @Autowired
    private LuxuryPrizeController luxuryPrizeController;
    @Autowired
    private NotWorkController notWorkController;
    @Autowired
    private PromotionController promotionController;
    @Autowired
    private TravelPrizeController travelPrizeController;

    @Test
    public void shouldLoadContext() {
        assertNotNull(annualPrizeController);
        assertNotNull(exportController);
        assertNotNull(headlinesTodayController);
        assertNotNull(heroRankingController);
        assertNotNull(iPhone7LotteryController);
        assertNotNull(lotteryController);
        assertNotNull(luxuryPrizeController);
        assertNotNull(notWorkController);
        assertNotNull(promotionController);
        assertNotNull(travelPrizeController);
    }
}

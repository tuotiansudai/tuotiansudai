package com;

import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.special.model.InvestLuckyDraw;
import com.ttsd.special.model.InvestLuckyDrawPrizeLevel;
import com.ttsd.special.model.InvestLuckyDrawType;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class InvestLuckyDrawTest {

    @Resource
    HibernateTemplate ht;

    @org.junit.Test
    public void ReadAndWrite() {
        Invest invest = ht.get(Invest.class, "20150605000001");

        String rndId = IdGenerator.randomUUID();

        InvestLuckyDraw ild = new InvestLuckyDraw();
        ild.setId(rndId);
        ild.setAmount(0D);
        ild.setAwardTime(new Date());
        ild.setCreatedTime(new Date());
        ild.setInvest(invest);
        ild.setIsValid(true);
        ild.setPrizeLevel(InvestLuckyDrawPrizeLevel.A);
        ild.setType(InvestLuckyDrawType.PT);
        ild.setUser(invest.getUser());

        ht.save(ild);

        InvestLuckyDraw ild1 = ht.get(InvestLuckyDraw.class,rndId);
        assert ild1.getPrizeLevel() == InvestLuckyDrawPrizeLevel.A;

        ht.delete(ild1);
    }

}

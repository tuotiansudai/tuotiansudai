package com;

import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryPrizeType;
import com.ttsd.special.model.InvestLotteryType;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class InvestLotteryTest{

    @Resource
    HibernateTemplate ht;

    @org.junit.Test
    public void ReadAndWrite() {
        Invest invest = ht.get(Invest.class, "20150605000001");

        String rndId = IdGenerator.randomUUID();

        InvestLottery ild = new InvestLottery();
        ild.setId(rndId);
        ild.setAmount(3289439L);
        ild.setCreatedTime(new Date());
        ild.setInvest(invest);
        ild.setValid(true);
        ild.setPrizeType(InvestLotteryPrizeType.A);
        ild.setType(InvestLotteryType.NOVICE);
        ild.setUser(invest.getUser());

        ht.save(ild);

        InvestLottery ild1 = ht.get(InvestLottery.class,rndId);
        assert ild1.getPrizeType() == InvestLotteryPrizeType.A;


        ht.delete(ild1);
    }

}

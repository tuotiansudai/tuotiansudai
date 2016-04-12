package com;

import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/8/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class Test {

    @Resource
    HibernateTemplate ht;

    @org.junit.Test
    public void getTrusteeshipAccount() {
        String user_id = "Yupeng";
        TrusteeshipAccount ta = null;
        List<TrusteeshipAccount> taList = ht.find(
                "from TrusteeshipAccount t where t.user.id=?",
                new String[] { user_id });
        if (null != taList && taList.size() > 0) {
            ta = taList.get(0);
        }
    }

    @org.junit.Test
    public void getDate() {
        String temp = "1441678080000";
        Date d = new Date(Long.parseLong(temp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(d));


        Date date = new Date();
        Date triggerTime = new DateTime(2015, 9, 7, 17, 8, 0).toDate();
        System.out.println(sdf.format(d));
    }

}

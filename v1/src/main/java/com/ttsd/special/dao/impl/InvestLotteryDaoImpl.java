package com.ttsd.special.dao.impl;

import com.esoft.core.annotations.Logger;
import com.ttsd.special.dao.InvestLotteryDao;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryType;
import org.apache.commons.logging.Log;
import org.hibernate.SQLQuery;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InvestLotteryDaoImpl implements InvestLotteryDao {
    @Autowired
    private HibernateTemplate ht;

    @Logger
    Log log;

    @Override
    public List<InvestLottery> findInvestLotteryByType(InvestLotteryType type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<InvestLottery> list = null;

        try {
            Date createdTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            list = ht.find("from InvestLottery t where t.user.id = ? and t.type = ? and t.createdTime = ? "
                           +  " and t.valid != '1' order by createdTime ",
                    new Object[]{userId, type, createdTime});
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(),e);
        }
        return list;
    }

    @Override
    public void updateInvestLottery(InvestLottery investLottery) {
        ht.save(investLottery);
    }
}

package com.ttsd.special.services.impl;

import com.ttsd.special.dao.InvestmentTopDao;
import com.ttsd.special.dto.InvestTopItem;
import com.ttsd.special.dto.InvestTopList;
import com.ttsd.special.dto.InvestTopStatPeriod;
import com.ttsd.special.services.InvestmentTopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InvestmentTopServiceImpl implements InvestmentTopService {
    @Autowired
    private InvestmentTopDao dao;

    @Override
    public InvestTopList queryInvestTopListCache(InvestTopStatPeriod period) {
        return queryInvestTopList(period);
    }

    @Override
    public InvestTopList queryInvestTopList(InvestTopStatPeriod period) {
        Calendar calc = Calendar.getInstance();
        calc.set(2015, 0, 1, 0, 0, 0);
        Date beginTime = calc.getTime();
        calc.set(2015, 11, 1, 0, 0, 0);
        Date endTime = calc.getTime();
        System.out.println(beginTime);
        System.out.println(endTime);
        List<InvestTopItem> d = dao.StatInvestmentTop(beginTime, endTime);

        return null;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
        Date[] dates = null;

        dates = buildStartAndEndTime(InvestTopStatPeriod.Week);
        System.out.println(sdf.format(dates[0]));
        System.out.println(sdf.format(dates[1]));
        System.out.println("");

        dates = buildStartAndEndTime(InvestTopStatPeriod.Month);
        System.out.println(sdf.format(dates[0]));
        System.out.println(sdf.format(dates[1]));
        System.out.println("");

        dates = buildStartAndEndTime(InvestTopStatPeriod.Quarter);
        System.out.println(sdf.format(dates[0]));
        System.out.println(sdf.format(dates[1]));
        System.out.println("");

        dates = buildStartAndEndTime(InvestTopStatPeriod.Year);
        System.out.println(sdf.format(dates[0]));
        System.out.println(sdf.format(dates[1]));
    }

    private static Date[] buildStartAndEndTime(InvestTopStatPeriod period){
        Date[] dates = new Date[2];

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int weekofyear = cal.get(Calendar.WEEK_OF_YEAR);

        if(period == InvestTopStatPeriod.Week){
            cal.setWeekDate(year,weekofyear, 2);
            dates[0] = cal.getTime();
            cal.setWeekDate(year,weekofyear+1, 2);
            dates[1] = cal.getTime();
        }

        if(period == InvestTopStatPeriod.Month){
            cal.set(year, month, 1);
            dates[0] = cal.getTime();
            cal.set(year, month + 1, 1);
            dates[1] = cal.getTime();
        }

        if(period == InvestTopStatPeriod.Quarter){
            cal.set(year, (month/3)*3, 1);
            dates[0] = cal.getTime();
            cal.add(Calendar.MONTH, 3);
            dates[1] = cal.getTime();
        }

        if(period == InvestTopStatPeriod.Year){
            cal.set(year, 0, 1);
            dates[0] = cal.getTime();
            cal.set(year+1, 0, 1);
            dates[1] = cal.getTime();
        }

        return dates;
    }

}

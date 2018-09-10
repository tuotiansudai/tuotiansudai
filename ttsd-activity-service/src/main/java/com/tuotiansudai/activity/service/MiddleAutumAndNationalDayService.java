package com.tuotiansudai.activity.service;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.InvestNewmanTyrantMapper;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by qduljs2011 on 2018/9/7.
 */
@Service
public class MiddleAutumAndNationalDayService {
    static Logger logger = Logger.getLogger(MiddleAutumAndNationalDayService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    @Autowired
    private InvestNewmanTyrantMapper investNewmanTyrantMapper;

    @Value("#{'${activity.middleautum.nationalday.activity.period}'.split('\\~')}")
    private List<String> middleautumNationalDayActivityPeriod = Lists.newArrayList();

    public List<String> getActivityTime() {

        return Lists.newArrayList(middleautumNationalDayActivityPeriod.get(0), middleautumNationalDayActivityPeriod.get(1));
    }

    public List<NewmanTyrantView> obtainRecords(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("【MiddleAutumAndNationalDayService.obtainNewman】参数为空tradingTime");
            return null;
        }
        Date tradingStartTime = null;
        Date tradingEndTime = null;
        //基准比较时间---查询时间当天的22点
        Date standardDate = new DateTime(tradingTime).withTime(22, 0, 0, 0).toDate();
        if (standardDate.before(tradingTime)) {
            tradingStartTime = standardDate;
            tradingEndTime = new DateTime(tradingStartTime).plusDays(1).toDate();
        } else {
            tradingEndTime = standardDate;
            tradingStartTime = new DateTime(tradingEndTime).minusDays(1).toDate();
        }

        List<NewmanTyrantView> newmanViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTimeWithEnd(tradingStartTime, tradingEndTime, middleautumNationalDayActivityPeriod.get(0), middleautumNationalDayActivityPeriod.get(1));
        return CollectionUtils.isNotEmpty(newmanViews) && newmanViews.size() > 10 ? newmanViews.subList(0, 10) : newmanViews;
    }

}

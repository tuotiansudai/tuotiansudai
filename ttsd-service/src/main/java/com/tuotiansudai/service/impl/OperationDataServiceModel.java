package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.service.OperationDataService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huoxuanbo on 16/5/9.
 */
public class OperationDataServiceModel {
    final private String startOperationTime = "2015-07-01";
    private long userCount;
    private String investTotalAmount;
    private List<String> investMonthAmount = new LinkedList<>();
    private List<String> investMonth = new LinkedList<>();
    private int investMonthSize;

    public OperationDataServiceModel() {
        this.setInvestMonthSize();
    }

    public OperationDataDto getOperationDataDto()
    {
        OperationDataDto operationDataDto = new OperationDataDto();
        operationDataDto.setUsersCount(this.getOperationTime());
        operationDataDto.setTradeAmount(this.getInvestTotalAmount());
        operationDataDto.setOperationDays(this.getOperationTime());
        operationDataDto.setMonth(this.getInvestMonth());
        operationDataDto.setMoney(this.getInvestMonthAmount());

        return operationDataDto;
    }

    public int getOperationTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = new Date();
        try {
            startTime = simpleDateFormat.parse(this.startOperationTime);
        } catch (ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        long startMillis = calendar.getTimeInMillis();
        Date endTime = new Date();
        long endMillis = endTime.getTime();
        return (int) ((endMillis - startMillis) / (1000 * 3600 * 24));
    }

    public String getStartOperationTime() {
        return startOperationTime;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public String getInvestTotalAmount() {
        return investTotalAmount;
    }

    public void setInvestTotalAmount(String investTotalAmount) {
        this.investTotalAmount = investTotalAmount;
    }

    public List<String> getInvestMonthAmount() {
        List<String> investMonthAmountCopy = new LinkedList<>();
        for (String number : investMonthAmount) {
            investMonthAmountCopy.add(number);
        }
        return investMonthAmountCopy;
    }

    public String getInvestMonthAmountString()
    {
        return Joiner.on(",").join(investMonthAmount);
    }

    public void setInvestMonthAmount(String listString)
    {
        investMonthAmount = Splitter.on(",").splitToList(listString);
    }

    public void addInvestMonthAmount(String number) {
        if (investMonthAmount.size() >= investMonthSize) {
            throw new IndexOutOfBoundsException();
        }
        this.investMonthAmount.add(number);
    }

    public List<String> getInvestMonth() {
        List<String> investMonthCopy = new LinkedList<>();
        for (String month : investMonth) {
            investMonthCopy.add(month);
        }
        return investMonthCopy;
    }

    public String getInvestMonthString()
    {
        return Joiner.on(",").join(investMonth);
    }

    public void setInvestMonth(String listString) {
        investMonth = Splitter.on(",").splitToList(listString);
    }

    public void addInvestMonth(String month) {
        if (investMonth.size() >= investMonthSize) {
            throw new IndexOutOfBoundsException();
        }
        this.investMonth.add(month);
    }

    public int getInvestMonthSize() {
        return investMonthSize;
    }

    private void setInvestMonthSize() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = new Date();
        try {
            startTime = simpleDateFormat.parse(this.startOperationTime);
        } catch (ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        calendar.setTime(new Date());
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        investMonthSize = (endYear - startYear) * 12 + (endMonth - startMonth);
    }
}

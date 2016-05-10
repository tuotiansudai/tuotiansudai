package com.tuotiansudai.repository.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huoxuanbo on 16/5/9.
 */
public class OperationDataModel {
    final private String startOperationTime = "2015-07-01";
    private long userAmount;
    private BigDecimal investTotalAmount;
    private List<BigDecimal> investMonthAmount;
    private List<String> investMonth;
    final private int investMonthSize = 10;

    public OperationDataModel() {
        investMonthAmount = new LinkedList<>();
        investMonth = new LinkedList<>();
    }

    public String getJSONString()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operationDays", this.getOperationTime());
        jsonObject.put("usersAmount", this.getUserAmount());
        jsonObject.put("TradeAmount", this.getInvestTotalAmount());
        JSONArray jsonMonthAmount = new JSONArray();
        for(BigDecimal number : this.getInvestMonthAmount())
        {
            jsonMonthAmount.add(number);
        }
        jsonObject.put("money", jsonMonthAmount);
        JSONArray jsonMonth = new JSONArray();
        for(String month : this.getInvestMonth())
        {
            jsonMonth.add(month);
        }
        jsonObject.put("month", jsonMonth);
        return jsonObject.toString();
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

    public long getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(long userAmount) {
        this.userAmount = userAmount;
    }

    public BigDecimal getInvestTotalAmount() {
        return investTotalAmount;
    }

    public void setInvestTotalAmount(BigDecimal investTotalAmount) {
        this.investTotalAmount = investTotalAmount;
    }

    public List<BigDecimal> getInvestMonthAmount() {
        List<BigDecimal> investMonthAmountCopy = new LinkedList<>();
        for (BigDecimal number : investMonthAmount) {
            investMonthAmountCopy.add(number);
        }
        return investMonthAmountCopy;
    }

    public void addInvestMonthAmount(BigDecimal number) {
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

    public void addInvestMonth(String month) {
        if (investMonth.size() >= investMonthSize) {
            throw new IndexOutOfBoundsException();
        }
        this.investMonth.add(month);
    }

    public int getInvestMonthSize() {
        return investMonthSize;
    }
}

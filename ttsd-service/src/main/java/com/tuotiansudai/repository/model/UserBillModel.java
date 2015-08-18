package com.tuotiansudai.repository.model;

import java.util.Date;

public class UserBillModel {
    private long id;

    private String loginName;

    private int amount;

    private int balance;

    private int freeze;

    private Date createdTime;

    private UserBillOperationType operationType;

    private UserBillBusinessType businessType;



}

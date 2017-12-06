package com.tuotiansudai.repository.model;

import java.util.Date;

public interface UserRegisterInfo {

    String[] fields = new String[]{"login_name", "mobile", "referrer", "register_time", "channel"};

    String getMobile();

    String getLoginName();

    Date getRegisterTime();

    String getReferrer();

    String getChannel();
}

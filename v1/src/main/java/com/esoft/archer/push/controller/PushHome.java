package com.esoft.archer.push.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.push.model.PushHistory;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.ttsd.push.PushClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(ScopeType.REQUEST)
public class PushHome extends EntityHome<PushHistory> {

    final static Log log = LogFactory.getLog(PushHome.class);

    private PushHistory pushHistory = new PushHistory();

    @Autowired
    private HibernateTemplate ht;

    @Autowired
    private PushClient pushClient;

    @Autowired
    private LoginUserInfo loginUserInfo;

    @Transactional(readOnly = false)
    public void push() {
        pushClient.sendIOSBroadcast();

        this.pushHistory.setOperator(loginUserInfo.getLoginUserId());
        this.pushHistory.setRequestData("requestData");
        this.pushHistory.setResponseData("requestData");
        this.save();
    }

    public String save() {
        try {
            ht.save(pushHistory);
            FacesUtil.addInfoMessage("消息已推送");
        } catch (DataAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            FacesUtil.addErrorMessage("消息推送失败");
        }
        return null;
    }

    public PushHistory getPushHistory() {
        return pushHistory;
    }

    public void setPushHistory(PushHistory pushHistory) {
        this.pushHistory = pushHistory;
    }
}

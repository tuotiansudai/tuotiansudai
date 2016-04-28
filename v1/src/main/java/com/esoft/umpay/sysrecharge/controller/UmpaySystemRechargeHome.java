package com.esoft.umpay.sysrecharge.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.pay.service.PayService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.umpay.sysrecharge.model.SystemRecharge;
import com.esoft.umpay.sysrecharge.service.impl.UmPaySystemRechargeOteration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;

@Component
@Scope(ScopeType.VIEW)
public class UmpaySystemRechargeHome extends EntityHome<SystemRecharge> {

    @Logger
    static Log log;

    @Resource
    HibernateTemplate ht;

    @Resource
    UmPaySystemRechargeOteration umPaySystemRechargeOteration;

    @Resource
    private LoginUserInfo loginUserInfo;

    @Override
    protected SystemRecharge createInstance() {
        SystemRecharge recharge = new SystemRecharge();
        recharge.setUser(new User());
        return recharge;
    }

    /**
     * 充值
     */
    public void recharge() {
        SystemRecharge recharge = this.getInstance();
        if (StringUtils.isEmpty(recharge.getUser().getId())) {
            FacesUtil.addErrorMessage("请选择资金来源账户！");
            return;
        }
        if (recharge.getMoney() <= 0) {
            FacesUtil.addErrorMessage("请输入要充值的金额！");
            return;
        }
        String remark = "{operator} 从 {user} 账户为平台账户充值 {money} 元"
                .replace("{operator}", loginUserInfo.getLoginUserId())
                .replace("{user}", recharge.getUser().getId())
                .replace("{money}", String.valueOf(recharge.getMoney()));
        recharge.setRemark(remark);

        try {
            umPaySystemRechargeOteration.createOperation(recharge, FacesContext.getCurrentInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

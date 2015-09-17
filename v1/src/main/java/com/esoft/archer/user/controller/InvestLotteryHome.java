package com.esoft.archer.user.controller;


import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.services.InvestLotteryService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class InvestLotteryHome extends EntityHome<InvestLottery> implements java.io.Serializable {

    @Autowired
    private InvestLotteryService investLotteryService;
    @Logger
    static Log log;

    public String updateInvestLotteryGranted(long id,boolean granted){

        investLotteryService.updateInvestLotteryGranted(id,granted);

        log.info("investLottery id: "+ id +" is_granted from " + !granted + " to "+ granted);
        return getSaveView();
    }

}

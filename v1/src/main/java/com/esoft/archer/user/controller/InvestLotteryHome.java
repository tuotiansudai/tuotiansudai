package com.esoft.archer.user.controller;


import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.ReceiveStatus;
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

    public String updateInvestLotteryGranted(long id,boolean receivedFlag){

        if(receivedFlag){
            investLotteryService.updateInvestLotteryGranted(id, ReceiveStatus.RECEIVED);
            log.info("investLottery id: " + id + " ReceiveStatus from " + ReceiveStatus.NOT_RECEIVED + " to " + ReceiveStatus.RECEIVED);
        }else{

            investLotteryService.updateInvestLotteryGranted(id, ReceiveStatus.NOT_RECEIVED);
            log.info("investLottery id: " + id + " ReceiveStatus from " + ReceiveStatus.RECEIVED + " to " + ReceiveStatus.NOT_RECEIVED);
        }

        return getSaveView();
    }

}

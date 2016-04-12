package com.ttsd.api.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.umpay.invest.service.impl.UmPayInvestOeration;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.ttsd.api.dao.MobileAppInvestListDao;
import com.ttsd.api.dao.MobileAppLoanDetailDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppChannelService;
import com.ttsd.api.service.MobileAppUmPayInvestService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Locale;

@Service
public class MobileAppUmPayInvestServiceImpl implements MobileAppUmPayInvestService {
    @Autowired
    UmPayInvestOeration umPayInvestOeration;
    @Logger
    private static Log log;

    @Resource
    MobileAppLoanDetailDao mobileAppLoanDetailDao;

    @Autowired
    MobileAppInvestListDao mobileAppInvestListDao;

    @Autowired
    private InvestService investService;

    @Resource
    UserBO userBO;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Override
    public BaseResponseDto invest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestResponseDataDto> dto = new BaseResponseDto<InvestResponseDataDto>();
        String returnCode = this.verifyInvestRequestDto(investRequestDto);
        if (returnCode.equals(ReturnMessage.SUCCESS.getCode())) {
            Invest invest = this.createInvest(investRequestDto);

            try {
                InvestResponseDataDto investResponseDataDto = umPayInvestOeration.createOperation(invest);
                dto.setData(investResponseDataDto);
            } catch (UmPayOperationException e) {
                log.error(e.getLocalizedMessage(), e);
                returnCode = e.getMessage();
            }


        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));

        return dto;
    }

    @Override
    public String verifyInvestRequestDto(InvestRequestDto investRequestDto) {
        String userId = investRequestDto.getUserId();
        String loanId = investRequestDto.getLoanId();
        Loan loan = mobileAppLoanDetailDao.getLoanById(loanId);
        if (userId.equals(loan.getUser().getId())) {
            return ReturnMessage.INVESTOR_SAME_TO_LOANER.getCode();
        }

        if(loan.isLoanXs()){
            long investedCount = investService.getUserInvestXSCount(userId) + 1;
            int defaultCount = mobileAppInvestListDao.getConfigIntValue("sprog_invest_count");
            if(investedCount > defaultCount){
                return ReturnMessage.NO_MATCH_XS_INVEST_CONDITION.getCode();
            }
        }
        if (loan.isLoanDx()) {
            if (StringUtils.isEmpty(investRequestDto.getPassword())) {
                return ReturnMessage.INVEST_PASSWORD_IS_NULL.getCode();
            }
            if (!loan.getInvestPassword().equals(investRequestDto.getPassword())) {
                return ReturnMessage.INVEST_PASSWORD_IS_WRONG.getCode();
            }

        }
        User user = userBO.getUserByUsername(userId);
        if (StringUtils.isEmpty(user.getIdCard()) || StringUtils.isEmpty(user.getRealname())) {
            return ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode();
        }


        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public Invest createInvest(InvestRequestDto investRequestDto) {
        String loanId = investRequestDto.getLoanId();
        String userId = investRequestDto.getUserId();
        Invest invest = new Invest();
        Loan loan = new Loan();
        User user = new User();
        loan.setId(loanId);
        user.setId(userId);
        invest.setMoney(investRequestDto.getInvestMoney());
        invest.setIsAutoInvest(false);
        invest.setLoan(loan);
        invest.setUser(user);
        invest.setSource(AccessSource.valueOf(investRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)).name());
        invest.setChannel(mobileAppChannelService.obtainChannelBySource(investRequestDto.getBaseParam()));
        return invest;
    }

}

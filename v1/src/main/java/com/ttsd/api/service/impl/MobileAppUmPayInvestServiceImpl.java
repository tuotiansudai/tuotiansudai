package com.ttsd.api.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.umpay.invest.service.impl.UmPayInvestOeration;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.ttsd.api.dao.MobileAppLoanDetailDao;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestRequestDto;
import com.ttsd.api.dto.InvestResponseDataDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppUmPayInvestService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class MobileAppUmPayInvestServiceImpl implements MobileAppUmPayInvestService {
    @Autowired
    UmPayInvestOeration umPayInvestOeration;
    @Logger
    private static Log log;

    @Resource
    MobileAppLoanDetailDao mobileAppLoanDetailDao;

    @Resource
    UserBO userBO;

    @Override
    public BaseResponseDto invest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestResponseDataDto> dto = new BaseResponseDto<InvestResponseDataDto>();
        Map<String, String> map = null;
        String returnCode = this.verifyInvestRequestDto(investRequestDto);
        if (returnCode.equals(ReturnMessage.SUCCESS.getCode())) {
            Invest invest = this.createInvest(investRequestDto);

            try {
                map = umPayInvestOeration.createOperation(invest);
            } catch (UmPayOperationException e) {
                log.error(e.getLocalizedMessage(), e);
                returnCode = e.getMessage();
            }


        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (map != null) {
            InvestResponseDataDto investResponseDataDto = convertInvestResponseDataDtoFromMap(map);
            dto.setData(investResponseDataDto);

        }
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


        if (loan.isLoanDx()) {
            if (StringUtils.isEmpty(investRequestDto.getPassword())) {
                return ReturnMessage.INVEST_PASSWORD_IS_NULL.getCode();
            }
            if (!loan.getInvestPassword().equals(investRequestDto.getPassword())) {
                return ReturnMessage.INVEST_PASSWORD_IS_WRONG.getCode();
            }

        }
        User user = userBO.getUserByUsername(userId);
        if(StringUtils.isEmpty(user.getIdCard()) || StringUtils.isEmpty(user.getRealname())){
            return ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode();
        }


        return ReturnMessage.SUCCESS.getCode();
    }

    @Override
    public InvestResponseDataDto convertInvestResponseDataDtoFromMap(Map<String, String> map) {
        InvestResponseDataDto investResponseDataDto = new InvestResponseDataDto();
        investResponseDataDto.setService(map.get("service"));
        investResponseDataDto.setSignType(map.get("sign_type"));
        investResponseDataDto.setSign(map.get("sign"));
        investResponseDataDto.setCharset(map.get("charset"));
        investResponseDataDto.setResFormat(map.get("res_format"));
        investResponseDataDto.setMerId(map.get("mer_id"));
        investResponseDataDto.setRetUrl(map.get("ret_url"));
        investResponseDataDto.setNotifyUrl(map.get("notify_url"));
        investResponseDataDto.setVersion(map.get("version"));
        investResponseDataDto.setSourceV(map.get("sourceV"));
        investResponseDataDto.setOrdeId(map.get("order_id"));
        investResponseDataDto.setMerDate(map.get("mer_date"));
        investResponseDataDto.setProjectId(map.get("project_id"));
        investResponseDataDto.setProjectAccountId(map.get("project_account_id"));
        investResponseDataDto.setServType(map.get("serv_type"));
        investResponseDataDto.setTransAction(map.get("trans_action"));
        investResponseDataDto.setParticType(map.get("partic_type"));
        investResponseDataDto.setParticAccType(map.get("partic_acc_type"));
        investResponseDataDto.setParticUserId(map.get("partic_user_id"));
        investResponseDataDto.setProjectAccountId(map.get("partic_account_id"));
        investResponseDataDto.setAmount(map.get("amount"));
        investResponseDataDto.setUrl(map.get("url"));
        return investResponseDataDto;
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
        return invest;
    }

}

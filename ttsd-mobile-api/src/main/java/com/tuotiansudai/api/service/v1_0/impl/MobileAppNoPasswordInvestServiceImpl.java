package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNoPasswordInvestServiceImpl implements MobileAppNoPasswordInvestService {

    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public MobileAppNoPasswordInvestServiceImpl(BankAccountMapper bankAccountMapper) {
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BaseResponseDto<NoPasswordInvestResponseDataDto> getNoPasswordInvestData(String loginName) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, Role.INVESTOR);
        if(bankAccountModel == null){
            return new BaseResponseDto<>(ReturnMessage.USER_IS_NOT_CERTIFICATED);
        }

        BaseResponseDto<NoPasswordInvestResponseDataDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        NoPasswordInvestResponseDataDto noPasswordInvestResponseDataDto = new NoPasswordInvestResponseDataDto();
        noPasswordInvestResponseDataDto.setAutoInvest(bankAccountModel.isAuthorization());
        noPasswordInvestResponseDataDto.setNoPasswordInvest(bankAccountModel.isAutoInvest());
        baseResponseDto.setData(noPasswordInvestResponseDataDto);
        return baseResponseDto;
    }
}

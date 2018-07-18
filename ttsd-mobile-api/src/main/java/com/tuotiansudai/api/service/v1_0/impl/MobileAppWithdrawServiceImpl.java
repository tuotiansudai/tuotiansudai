package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppWithdrawService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import com.tuotiansudai.service.BankWithdrawService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppWithdrawServiceImpl implements MobileAppWithdrawService {
    private static Logger logger = Logger.getLogger(MobileAppWithdrawServiceImpl.class);

    @Autowired
    private UserBankCardMapper userBankCardMapper;

    @Autowired
    private BankWithdrawMapper bankWithdrawMapper;

    @Autowired
    private BankWithdrawService bankWithdrawService;

    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<WithdrawListResponseDataDto> queryUserWithdrawLogs(WithdrawListRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());

        if (index == null || index <= 0) {
            index = 1;
        }

        long count = bankWithdrawMapper.findWithdrawCount(null,null, requestDto.getBaseParam().getPhoneNum(), null, null, null, null);
        List<WithdrawPaginationView> views = bankWithdrawMapper.findWithdrawPagination(null,null,
                requestDto.getBaseParam().getPhoneNum(), null, null, (index - 1) * pageSize, 10, null, null);

        List<WithdrawDetailResponseDataDto> withdrawDetailResponseDataDtos = views.stream().map(view -> new WithdrawDetailResponseDataDto(view.getId(), view.getAmount(), view.getStatus(), view.getCreatedTime(), view.getUpdatedTime())).collect(Collectors.toList());

        WithdrawListResponseDataDto listDataDto = new WithdrawListResponseDataDto();
        listDataDto.setIndex(index);
        listDataDto.setPageSize(pageSize);
        listDataDto.setTotalCount(count);
        listDataDto.setWithdrawList(withdrawDetailResponseDataDtos);

        BaseResponseDto<WithdrawListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(listDataDto);

        return dto;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> generateWithdrawRequest(WithdrawOperateRequestDto requestDto) {
        long withdrawAmount = AmountConverter.convertStringToCent(String.valueOf(requestDto.getMoney()));

        if (withdrawAmount <= withdrawFee) {
            return new BaseResponseDto(ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getCode(), ReturnMessage.WITHDRAW_AMOUNT_NOT_REACH_FEE.getMsg());
        }
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginNameAndRole(requestDto.getBaseParam().getUserId(), Role.INVESTOR);
        if (userBankCardModel == null) {
            return new BaseResponseDto(ReturnMessage.NOT_BIND_CARD.getCode(), ReturnMessage.NOT_BIND_CARD.getMsg());
        }

        if (userBankCardMapper.findByLoginNameAndRole(requestDto.getBaseParam().getUserId(), Role.INVESTOR) == null) {
            return new BaseResponseDto<>(ReturnMessage.NOT_BIND_CARD);
        }

        BankAsyncMessage bankAsyncMessage = bankWithdrawService.withdraw(Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase()),
                requestDto.getBaseParam().getUserId(),
                requestDto.getBaseParam().getPhoneNum(),
                withdrawAmount, withdrawFee, Role.INVESTOR);
        return CommonUtils.mapToFormData(bankAsyncMessage);
    }
}

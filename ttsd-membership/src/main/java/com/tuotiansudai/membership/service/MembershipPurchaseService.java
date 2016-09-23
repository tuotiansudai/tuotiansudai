package com.tuotiansudai.membership.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPurchaseDto;
import com.tuotiansudai.membership.dto.MembershipPurchasePaginationItemDto;
import com.tuotiansudai.membership.exception.MembershipIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.mapper.MembershipPriceMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import com.tuotiansudai.membership.repository.model.MembershipPurchaseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class MembershipPurchaseService {

    private static Logger logger = Logger.getLogger(MembershipPurchaseService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipPriceMapper membershipPriceMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private MembershipPurchaseMapper membershipPurchaseMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    public BaseDto<PayFormDataDto> purchase(String loginName, int level, int duration, Source source) throws MembershipIsPurchasedException, NotEnoughAmountException {
        logger.info(MessageFormat.format("[membership purchase] user({0}) purchase membership level({1}) duration({2})",
                loginName, String.valueOf(level), String.valueOf(duration)));

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        if (membershipModel.getLevel() == level) {
            throw new MembershipIsPurchasedException();
        }

        MembershipPriceModel priceModel = membershipPriceMapper.find(level, duration);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        if (accountModel == null || accountModel.getBalance() < priceModel.getPrice()) {
            throw new NotEnoughAmountException();
        }

        MembershipPurchaseDto purchaseDto = new MembershipPurchaseDto(loginName,
                userMapper.findByLoginName(loginName).getMobile(),
                accountModel.getUserName(),
                level, duration, priceModel.getPrice(), source);

        return payWrapperClient.membershipPurchase(purchaseDto);
    }

    public BaseDto<BasePaginationDataDto> getMembershipPurchaseList(String mobile, Integer duration, Source source, Date startTime, Date endTime, int index, int pageSize) {
        long count = membershipPurchaseMapper.countByPagination(mobile, duration, source, startTime, endTime);
        List<MembershipPurchaseModel> models = membershipPurchaseMapper.findByPagination(mobile, duration, source, startTime, endTime, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<MembershipPurchasePaginationItemDto> items = Lists.transform(models, new Function<MembershipPurchaseModel, MembershipPurchasePaginationItemDto>() {
            @Override
            public MembershipPurchasePaginationItemDto apply(MembershipPurchaseModel input) {
                return new MembershipPurchasePaginationItemDto(input);
            }
        });

        BasePaginationDataDto<MembershipPurchasePaginationItemDto> paginationDataDto = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        paginationDataDto.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(paginationDataDto);
    }
}

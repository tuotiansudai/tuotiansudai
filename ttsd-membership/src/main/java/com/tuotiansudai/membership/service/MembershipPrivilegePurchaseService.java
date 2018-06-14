package com.tuotiansudai.membership.service;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchasePaginationItemDto;
import com.tuotiansudai.membership.exception.MembershipPrivilegeIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegePurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipPrivilegePurchaseService {

    private static Logger logger = Logger.getLogger(MembershipPrivilegePurchaseService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    private static final double MEMBERSHIP_PRIVILEGE_SERVICE_FEE = 0.07;

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;

    public BaseDto<PayFormDataDto> purchase(String loginName, MembershipPrivilegePriceType membershipPrivilegePriceType, Source source) throws MembershipPrivilegeIsPurchasedException, NotEnoughAmountException {
        logger.info(String.format("[membership privilege purchase:] user(%s) purchase duration(%s)", loginName, String.valueOf(membershipPrivilegePriceType.getDuration())));
        if (membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName, new Date()) != null) {
            throw new MembershipPrivilegeIsPurchasedException();
        }
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);

        if (bankAccountModel == null || bankAccountModel.getBalance() < membershipPrivilegePriceType.getPrice()) {
            throw new NotEnoughAmountException();
        }

        MembershipPrivilegePurchaseDto purchaseDto = new MembershipPrivilegePurchaseDto(loginName,
                userModel.getMobile(),
                userModel.getUserName(),
                membershipPrivilegePriceType.getDuration(), membershipPrivilegePriceType.getPrice(), source);

        return payWrapperClient.membershipPrivilegePurchase(purchaseDto);
    }

    public double obtainServiceFee(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return defaultFee;
        }
        MembershipPrivilegeModel membershipPrivilegeModel = membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName, new Date());
        if (membershipPrivilegeModel != null) {
            return MEMBERSHIP_PRIVILEGE_SERVICE_FEE;
        }
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        return membershipModel != null ? membershipModel.getFee() : defaultFee;

    }

    public MembershipPrivilegeModel obtainMembershipPrivilege(String loginName) {
        return membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName, new Date());
    }

    public BaseDto<BasePaginationDataDto> getMembershipPurchaseList(String mobile, MembershipPrivilegePriceType membershipPrivilegePriceType, Source source, Date startTime, Date endTime, int index, int pageSize) {
        long count = membershipPrivilegePurchaseMapper.findCountMembershipPrivilegePagination(mobile, membershipPrivilegePriceType, source, startTime, endTime);
        List<MembershipPrivilegePurchaseModel> membershipPrivilegeModels = membershipPrivilegePurchaseMapper.findMembershipPrivilegePagination(mobile, membershipPrivilegePriceType, source, startTime, endTime, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<MembershipPrivilegePurchasePaginationItemDto> itemDtos = membershipPrivilegeModels
                .stream()
                .map(membershipPrivilegePurchaseModel -> new MembershipPrivilegePurchasePaginationItemDto(membershipPrivilegePurchaseModel)).collect(Collectors.toList());
        BasePaginationDataDto paginationDataDto = new BasePaginationDataDto(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, itemDtos);
        paginationDataDto.setStatus(true);
        return new BaseDto<>(paginationDataDto);

    }

}

package com.tuotiansudai.console.service;

import com.google.common.base.*;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleInvestService {

    static Logger logger = Logger.getLogger(ConsoleInvestService.class);

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "${web.newbie.invest.limit}")
    private int newbieInvestLimit;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private UserMapper userMapper;

    public InvestPaginationDataDto getInvestPagination(Long loanId, String investorMobile, String channel, Source source,
                                                       Role role, Date startTime, Date endTime, InvestStatus investStatus,
                                                       PreferenceType preferenceType, int index, int pageSize) {
        UserModel investor = userMapper.findByMobile(investorMobile);
        String investorLoginName = investor != null ? investor.getLoginName() : null;

        endTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().plusSeconds(-1).toDate();
        long count = investMapper.findCountInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType);
        long investAmountSum = investMapper.sumInvestAmountConsole(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType);
        index = PaginationUtil.validateIndex(index, pageSize, count);
        List<InvestPaginationItemView> items = investMapper.findInvestPagination(loanId, investorLoginName, channel, source, role, startTime, endTime, investStatus, preferenceType, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

        List<InvestPaginationItemDataDto> records = Lists.transform(items, new Function<InvestPaginationItemView, InvestPaginationItemDataDto>() {
            @Override
            public InvestPaginationItemDataDto apply(InvestPaginationItemView view) {
                InvestPaginationItemDataDto investPaginationItemDataDto = new InvestPaginationItemDataDto(view);
                CouponModel couponModel = couponMapper.findById(view.getCouponId());
                if (null != couponModel) {
                    long couponActualInterest = 0;
                    if (couponModel.getCouponType().equals(CouponType.RED_ENVELOPE)) {
                        List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(view.getInvestId());
                        for (UserCouponModel userCouponModel : userCouponModels) {
                            couponActualInterest += userCouponModel.getActualInterest();
                        }
                    } else {
                        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findByUserCouponByInvestId(view.getInvestId());
                        for (CouponRepayModel couponRepayModel : couponRepayModels) {
                            couponActualInterest += couponRepayModel.getActualInterest();
                        }
                    }
                    investPaginationItemDataDto.setCouponActualInterest(couponActualInterest);
                    investPaginationItemDataDto.setCouponDetail(couponModel);
                }
                return investPaginationItemDataDto;
            }
        });

        InvestPaginationDataDto dto = new InvestPaginationDataDto(index, pageSize, count, records);

        dto.setSumAmount(investAmountSum);

        dto.setStatus(true);

        return dto;
    }

    public List<String> findAllChannel() {
        return investMapper.findAllChannels();
    }

    public List<String> findAllInvestChannels() {
        return investMapper.findAllInvestChannels();
    }
}

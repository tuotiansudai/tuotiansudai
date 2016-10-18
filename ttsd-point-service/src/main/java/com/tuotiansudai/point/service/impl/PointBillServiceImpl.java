package com.tuotiansudai.point.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PointBillServiceImpl implements PointBillService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    @Transactional
    public void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setPoint(accountModel.getPoint() + point);
        String note = this.generatePointBillNote(businessType, orderId);
        pointBillMapper.create(new PointBillModel(loginName, orderId, point, businessType, note));
        accountMapper.update(accountModel);
    }

    @Override
    @Transactional
    public void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point, String note) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setPoint(accountModel.getPoint() + point);
        pointBillMapper.create(new PointBillModel(loginName, orderId, point, businessType, note));
        accountMapper.update(accountModel);
    }

    @Override
    @Transactional
    public void createTaskPointBill(String loginName, long pointTaskId, long point, String note) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setPoint(accountModel.getPoint() + point);
        pointBillMapper.create(new PointBillModel(loginName, pointTaskId, point, PointBusinessType.TASK, note));
        accountMapper.update(accountModel);
    }

    @Override
    public BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPagination(String loginName,
                                                                                        int index,
                                                                                        int pageSize,
                                                                                        Date startTime,
                                                                                        Date endTime,
                                                                                        List<PointBusinessType> businessTypes) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<PointBillModel> items = Lists.newArrayList();

        long count = pointBillMapper.findCountPointBillPagination(loginName, startTime, endTime, businessTypes);
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = pointBillMapper.findPointBillPagination(loginName, (index - 1) * pageSize, pageSize, startTime, endTime, businessTypes);
        }
        List<PointBillPaginationItemDataDto> records = Lists.transform(items, new Function<PointBillModel, PointBillPaginationItemDataDto>() {
            @Override
            public PointBillPaginationItemDataDto apply(PointBillModel view) {
                return new PointBillPaginationItemDataDto(view);
            }
        });

        BasePaginationDataDto<PointBillPaginationItemDataDto> dto = new BasePaginationDataDto<PointBillPaginationItemDataDto>(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    @Override
    public List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int index, int pageSize){
        List<PointBillModel> pointBillModels =  pointBillMapper.findPointBillByLoginName(loginName, (index - 1) * pageSize, pageSize);

        List<PointBillPaginationItemDataDto> pointBillPaginationItemDataDtoList = new ArrayList<>();
        for(PointBillModel pointBillModel : pointBillModels) {
            PointBillPaginationItemDataDto pointBillPaginationItemDataDto = new PointBillPaginationItemDataDto(pointBillModel);
            pointBillPaginationItemDataDtoList.add(pointBillPaginationItemDataDto);
        }
        return pointBillPaginationItemDataDtoList;
    }

    @Override
    public long getPointBillCountByLoginName(String loginName){
        return pointBillMapper.findCountPointBillByLoginName(loginName);
    }

    @Override
    public List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, Integer currentPageNo, Integer pageSize){
        List<AccountModel> accountModels =  accountMapper.findUsersAccountPoint(loginName, userName, mobile,
                currentPageNo != null ? (currentPageNo - 1) * pageSize : null,
                pageSize);

        List<AccountItemDataDto> accountItemDataDtoList = Lists.newArrayList();
        for(AccountModel accountModel : accountModels) {
            AccountItemDataDto accountItemDataDto = new AccountItemDataDto(accountModel);
            accountItemDataDto.setTotalPoint(pointBillMapper.findUserTotalPoint(accountModel.getLoginName()));
            accountItemDataDto.setMobile(userMapper.findByLoginName(accountModel.getLoginName()).getMobile());
            accountItemDataDtoList.add(accountItemDataDto);
        }
        return accountItemDataDtoList;
    }

    @Override
    public int findUsersAccountPointCount(String loginName, String userName, String mobile){
        return accountMapper.findUsersAccountPointCount(loginName, userName, mobile);
    }

    private String generatePointBillNote(PointBusinessType businessType, Long orderId) {
        switch (businessType) {
            case SIGN_IN:
                return MessageFormat.format("{0} 签到", new DateTime().toString("yyyy-MM-dd"));
            case EXCHANGE:
                CouponModel couponModel = couponMapper.findById(orderId);
                switch (couponModel.getCouponType()) {
                    case INTEREST_COUPON:
                        double rate = new BigDecimal(couponModel.getRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                        return MessageFormat.format("{0}% {1}", String.valueOf(rate), couponModel.getCouponType().getName());
                    case INVEST_COUPON:
                    case RED_ENVELOPE:
                        return MessageFormat.format("{0}元 {1}", AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
                    default:
                        return null;
                }
            case INVEST:
                LoanModel loanModel = loanMapper.findById(investMapper.findById(orderId).getLoanId());
                return MessageFormat.format("投资项目：{0}", loanModel.getName());
            case LOTTERY:
                return PointBusinessType.LOTTERY.getDescription();
            case ACTIVITY:
                return PointBusinessType.ACTIVITY.getDescription();
        }

        return null;
    }
}


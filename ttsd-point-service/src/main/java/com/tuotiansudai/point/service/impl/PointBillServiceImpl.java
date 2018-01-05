package com.tuotiansudai.point.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.UserPointItemDataDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.UserPointModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PointBillServiceImpl implements PointBillService {
    static Logger logger = Logger.getLogger(PointBillServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private UserPointMapper userPointMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;


    @Override
    @Transactional
    public void createTaskPointBill(String loginName, long pointTaskId, long point, String note) {
        createPointBill(loginName, pointTaskId, PointBusinessType.TASK, point, note);
    }

    @Override
    @Transactional
    public void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point) {
        String note = generatePointBillNote(businessType, orderId);
        createPointBill(loginName, orderId, businessType, point, note);
    }

    @Override
    @Transactional
    public void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point, String note) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            logger.info(String.format("createPointBill: %s no account", loginName));
            return;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);

        if (!userPointMapper.exists(loginName)) {
            userPointMapper.createIfNotExist(new UserPointModel(loginName, 0, 0, null));
        }
        UserPointModel userPointModel = userPointMapper.lockByLoginName(loginName);

        long channelPoint = calculateChannelPoint(userPointModel, point, businessType);
        long sudaiPoint = point - channelPoint;
        pointBillMapper.create(new PointBillModel(loginName, orderId, sudaiPoint, channelPoint, businessType, note, userModel.getMobile(), userModel.getUserName()));
        userPointMapper.increasePoint(loginName, sudaiPoint, channelPoint, new Date());
    }

    private long calculateChannelPoint(UserPointModel userPointModel, long point, PointBusinessType businessType) {
        if (businessType == PointBusinessType.CHANNEL_IMPORT) {
            return point;
        }
        if (point > 0) {
            return 0;
        }
        double channelRate = userPointModel.getPoint() == 0 ? 0.5 : (double)userPointModel.getChannelPoint() / userPointModel.getPoint();
        // 分配积分时，如果产生小数，则将小数部分归到速贷积分中
        // 此时 point < 0, 因此在计算渠道积分时，应向上取整。 如计算结果为 -2.4 时，实际渠道积分应为 -2
        return (long) Math.ceil(point * channelRate);
    }


    @Override
    public BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPagination(
            String loginName, String pointType,
            int index, int pageSize,
            Date startTime, Date endTime,
            List<PointBusinessType> businessTypes) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = CalculateUtil.calculateMaxDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<PointBillModel> items = Lists.newArrayList();

        long count = pointBillMapper.findCountPointBillPagination(loginName, pointType, startTime, endTime, businessTypes);
        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            items = pointBillMapper.findPointBillPagination(loginName, pointType, (index - 1) * pageSize, pageSize, startTime, endTime, businessTypes);
        }
        List<PointBillPaginationItemDataDto> records = items.stream()
                .map(PointBillPaginationItemDataDto::new)
                .collect(Collectors.toList());

        BasePaginationDataDto<PointBillPaginationItemDataDto> dto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    @Override
    public BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPaginationConsole(Date startTime,
                                                                                               Date endTime,
                                                                                               PointBusinessType businessType,
                                                                                               String channel, Long minPoint,
                                                                                               Long maxPoint,
                                                                                               String loginNameOrMobile, int index, int pageSize) {


        long count = pointBillMapper.getCountPointBillPaginationConsole(startTime, endTime, businessType, channel, minPoint, maxPoint, loginNameOrMobile, PointBusinessType.getPointConsumeBusinessType());
        List<PointBillPaginationItemDataDto> dataDtos = pointBillMapper.getPointBillPaginationConsole(startTime, endTime, businessType, channel, minPoint, maxPoint, loginNameOrMobile, PointBusinessType.getPointConsumeBusinessType(), PaginationUtil.calculateOffset(index, pageSize, count), pageSize)
                .stream()
                .map(dto -> new PointBillPaginationItemDataDto(dto)).collect(Collectors.toList());

        BasePaginationDataDto<PointBillPaginationItemDataDto> dto = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, dataDtos);
        dto.setStatus(true);
        return dto;
    }


    @Override
    public List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int index, int pageSize) {
        List<PointBillModel> pointBillModels = pointBillMapper.findPointBillByLoginName(loginName, (index - 1) * pageSize, pageSize);

        List<PointBillPaginationItemDataDto> pointBillPaginationItemDataDtoList = new ArrayList<>();
        for (PointBillModel pointBillModel : pointBillModels) {
            PointBillPaginationItemDataDto pointBillPaginationItemDataDto = new PointBillPaginationItemDataDto(pointBillModel);
            pointBillPaginationItemDataDtoList.add(pointBillPaginationItemDataDto);
        }
        return pointBillPaginationItemDataDtoList;
    }

    @Override
    public long getPointBillCountByLoginName(String loginName) {
        return pointBillMapper.findCountPointBillByLoginName(loginName);
    }

    @Override
    public BasePaginationDataDto<UserPointItemDataDto> findUsersAccountPoint(String loginNameOrMobile, String channel, Long minPoint, Long maxPoint, int currentPageNo, int pageSize) {
        if (StringUtils.isNotEmpty(loginNameOrMobile)) {
            return findUsersAccountPoint(loginNameOrMobile);
        } else {
            return findUsersAccountPoint(channel, minPoint, maxPoint, currentPageNo, pageSize);
        }
    }

    // 根据用户名查询时，最多只返回一条数据
    private BasePaginationDataDto<UserPointItemDataDto> findUsersAccountPoint(String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        AccountModel accountModel = userModel == null ? null : accountMapper.findByLoginName(userModel.getLoginName());
        List<UserModel> userModels = accountModel == null ? Collections.emptyList() : Collections.singletonList(userModel);
        List<UserPointItemDataDto> records = userModels.stream()
                .map(u -> new UserPointItemDataDto(
                        u.getLoginName(),
                        u.getUserName(),
                        u.getMobile(),
                        userPointMapper.findByLoginName(u.getLoginName()),
                        pointBillMapper.findUserTotalPoint(u.getLoginName())))
                .collect(Collectors.toList());
        return new BasePaginationDataDto<>(1, 1, userModels.size(), records);
    }

    private BasePaginationDataDto<UserPointItemDataDto> findUsersAccountPoint(String channel, Long minPoint, Long maxPoint, int pageNo, int pageSize) {
        Long minTotalPoint = null, maxTotalPoint = null, minSudaiPoint = null, maxSudaiPoint = null, minChannelPoint = null, maxChannelPoint = null;
        UserPointMapper.UserPointSortField sortField;
        if (StringUtils.isBlank(channel)) {
            minTotalPoint = minPoint;
            maxTotalPoint = maxPoint;
            sortField = UserPointMapper.UserPointSortField.POINT_FIELD_NAME;
        } else if (channel.equalsIgnoreCase(PointBillService.CHANNEL_SUDAI)) {
            channel = null;
            minSudaiPoint = minPoint;
            maxSudaiPoint = maxPoint;
            sortField = UserPointMapper.UserPointSortField.SUDAI_POINT_FIELD_NAME;
        } else {
            minChannelPoint = minPoint;
            maxChannelPoint = maxPoint;
            sortField = UserPointMapper.UserPointSortField.CHANNEL_POINT_FIELD_NAME;
        }
        List<UserPointModel> userPointModelList = userPointMapper.list(channel, minTotalPoint, maxTotalPoint, minSudaiPoint, maxSudaiPoint, minChannelPoint, maxChannelPoint, sortField, (pageNo - 1) * pageSize, pageSize);
        List<UserPointItemDataDto> records = userPointModelList.stream()
                .map(userPointModel -> {
                    UserModel userModel = userMapper.findByLoginName(userPointModel.getLoginName());
                    return userModel == null ? null : new UserPointItemDataDto(
                            userModel.getLoginName(),
                            userModel.getUserName(),
                            userModel.getMobile(),
                            userPointModel,
                            pointBillMapper.findUserTotalPoint(userModel.getLoginName()));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        long count = userPointMapper.count(channel, minTotalPoint, maxTotalPoint, minSudaiPoint, maxSudaiPoint, minChannelPoint, maxChannelPoint);
        return new BasePaginationDataDto<>(pageNo, pageSize, count, records);
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
            case CHANNEL_IMPORT:
                return PointBusinessType.CHANNEL_IMPORT.getDescription();
        }

        return null;
    }
}


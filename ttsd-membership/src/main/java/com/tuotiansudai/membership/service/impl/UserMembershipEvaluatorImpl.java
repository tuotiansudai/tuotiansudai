package com.tuotiansudai.membership.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipItemModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserMembershipEvaluatorImpl implements UserMembershipEvaluator {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Override
    public MembershipModel evaluate(String loginName) {
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(loginName);

        if (CollectionUtils.isEmpty(userMembershipModels)) {
            return null;
        }

        UnmodifiableIterator<UserMembershipModel> filter = Iterators.filter(userMembershipModels.iterator(), new Predicate<UserMembershipModel>() {
            @Override
            public boolean apply(UserMembershipModel input) {
                return input.getExpiredTime().after(new Date());
            }
        });

        UserMembershipModel max = new Ordering<UserMembershipModel>() {
            @Override
            public int compare(UserMembershipModel left, UserMembershipModel right) {
                return Ints.compare(membershipMapper.findById(left.getMembershipId()).getLevel(), membershipMapper.findById(right.getMembershipId()).getLevel());
            }
        }.max(filter);

        return membershipMapper.findById(max.getMembershipId());
    }

    @Override
    public BasePaginationDataDto<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                                               Date registerStartTime, Date registerEndTime,
                                                                               UserMembershipType userMembershipType,
                                                                               List<Integer> levels, int index, int pageSize) {
        if (StringUtils.isEmpty(loginName)) {
            loginName = null;
        }
        if (StringUtils.isEmpty(mobile)) {
            mobile = null;
        }
        if (UserMembershipType.ALL == userMembershipType) {
            userMembershipType = null;
        }
        if (CollectionUtils.isEmpty(levels)) {
            return new BasePaginationDataDto<>(index, pageSize, 0, new ArrayList<UserMembershipItemDto>());
        }
        List<UserMembershipItemModel> userMembershipItemModels = userMembershipMapper.findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(loginName, mobile, registerStartTime, registerEndTime, userMembershipType, levels);
        Collections.sort(userMembershipItemModels, new Comparator<UserMembershipItemModel>() {
            @Override
            public int compare(UserMembershipItemModel o1, UserMembershipItemModel o2) {
                return o2.getRegisterTime().after(o1.getRegisterTime()) ? 1 : -1;
            }
        });

        List<UserMembershipItemDto> results = new ArrayList<>();
        for (int startIndex = (index - 1) * pageSize,
             endIndex = index * pageSize <= userMembershipItemModels.size() ? index * pageSize : userMembershipItemModels.size();
             startIndex < endIndex; ++startIndex) {
            results.add(new UserMembershipItemDto(userMembershipItemModels.get(startIndex)));
        }
        return new BasePaginationDataDto<>(index, pageSize, userMembershipItemModels.size(), results);
    }

    @Override
    public List<Integer> getAllLevels() {
        return membershipMapper.findAllLevels();
    }
}

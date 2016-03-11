package com.tuotiansudai.coupon.util;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffRecommendLevelOneCollector implements UserCollector{

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Override
    public List<String> collect(long couponId) {
        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
        return Lists.transform(referrerRelationModels, new Function<ReferrerRelationModel, String>() {
            @Override
            public String apply(ReferrerRelationModel input) {
                return input.getLoginName();
            }
        });
    }

    @Override
    public long count(long couponId) {
        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
        return CollectionUtils.isNotEmpty(referrerRelationModels) ? referrerRelationModels.size() : 0;
    }

    @Override
    public boolean contains(long couponId, final String loginName) {
        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
        return CollectionUtils.isNotEmpty(referrerRelationModels) && Iterators.any(referrerRelationModels.iterator(), new Predicate<ReferrerRelationModel>() {
            @Override
            public boolean apply(ReferrerRelationModel input) {
                return input.getLoginName().equals(loginName);
            }
        });
    }

}

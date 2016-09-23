package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipPriceMapper {

    List<MembershipPriceModel> findAll();

    MembershipPriceModel find(@Param("level") int level, @Param("duration") int duration);
}

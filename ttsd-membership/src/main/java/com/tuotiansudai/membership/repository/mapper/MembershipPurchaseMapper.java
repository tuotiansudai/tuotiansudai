package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPurchaseModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MembershipPurchaseMapper {

    void create(MembershipPurchaseModel model);

    MembershipPurchaseModel findById(long id);

    void update(MembershipPurchaseModel model);

    long countByPagination(@Param("mobile") String mobile,
                           @Param("duration") Integer duration,
                           @Param("source") Source source,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);

    List<MembershipPurchaseModel> findByPagination(@Param("mobile") String mobile,
                                                   @Param("duration") Integer duration,
                                                   @Param("source") Source source,
                                                   @Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime,
                                                   @Param("index") Integer integer,
                                                   @Param("pageSize") Integer pageSize);
}

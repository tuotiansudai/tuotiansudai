package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MembershipPrivilegePurchaseMapper {
    int create(MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel);

    int update(MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel);

    MembershipPrivilegePurchaseModel findById(long id);

    List<MembershipPrivilegePurchaseModel> findMembershipPrivilegePagination(@Param("mobile") String mobile,
                                                                             @Param("membershipPrivilegePriceType") MembershipPrivilegePriceType membershipPrivilegePriceType,
                                                                             @Param("source") Source source,
                                                                             @Param("startTime") Date startTime,
                                                                             @Param("endTime") Date endTime,
                                                                             @Param("index") Integer integer,
                                                                             @Param("pageSize") Integer pageSize);

    long findCountMembershipPrivilegePagination(@Param("mobile") String mobile,
                                                @Param("membershipPrivilegePriceType") MembershipPrivilegePriceType membershipPrivilegePriceType,
                                                @Param("source") Source source,
                                                @Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime);
}

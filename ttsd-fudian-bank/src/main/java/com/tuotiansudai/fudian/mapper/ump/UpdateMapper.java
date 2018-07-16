package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UpdateMapper {

    @Update("update project_transfer_request set status = #{status} where id = #{id}")
    void updateProjectTransfer(@Param(value = "status") SyncRequestStatus status,
                               @Param(value = "id") long id);

    @Update("update coupon_repay_transfer_request set status = #{status} where id = #{id}")
    void updateCouponRepay(@Param(value = "status") SyncRequestStatus status,
                           @Param(value = "id") long id);

    @Update("update extra_rate_transfer_request set status = #{status} where id = #{id}")
    void updateExtraRate(@Param(value = "status") SyncRequestStatus status,
                           @Param(value = "id") long id);

    @Update("update transfer_request set status = #{status} where id = #{id}")
    void updateTransfer(@Param(value = "status") SyncRequestStatus status,
                         @Param(value = "id") long id);
}

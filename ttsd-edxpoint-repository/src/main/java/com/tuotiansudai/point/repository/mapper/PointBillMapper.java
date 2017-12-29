package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.UserTotalPointViewDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PointBillMapper {

    void create(PointBillModel pointBillModel);

    List<PointBillModel> findByLoginName(String loginName);

    long findCountPointBillPagination(@Param(value = "loginName") String loginName,
                                      @Param(value = "pointType") String pointType,
                                      @Param(value = "startTime") Date startTime,
                                      @Param(value = "endTime") Date endTime,
                                      @Param(value = "businessTypes") List<PointBusinessType> businessTypes);

    List<PointBillModel> findPointBillPagination(@Param(value = "loginName") String loginName,
                                                 @Param(value = "pointType") String pointType,
                                                 @Param(value = "index") int index,
                                                 @Param(value = "pageSize") int pageSize,
                                                 @Param(value = "startTime") Date startTime,
                                                 @Param(value = "endTime") Date endTime,
                                                 @Param(value = "businessTypes") List<PointBusinessType> businessTypes);

    List<PointBillModel> getPointBillPaginationConosle(@Param(value = "startTime") Date startTime,
                                                       @Param(value = "endTime") Date endTime,
                                                       @Param(value = "pointBusinessType") PointBusinessType businessType,
                                                       @Param(value = "channel") String channel,
                                                       @Param("minPoint") Long minPoint,
                                                       @Param("maxPoint") Long maxPoint,
                                                       @Param("userNameOrMobile") String userNameOrMobile);

    long findCountPointBillByLoginName(@Param(value = "loginName") String loginName);

    List<PointBillModel> findPointBillByLoginName(@Param(value = "loginName") String loginName,
                                                  @Param(value = "index") int index,
                                                  @Param(value = "pageSize") int pageSize);

    PointBillModel findLatestSignInPointBillByLoginName(@Param(value = "loginName") String loginName);

    UserTotalPointViewDto findUserTotalPoint(@Param(value = "loginName") String loginName);

    long findSumPointByLoginNameAndBusinessType(@Param(value = "loginName") String loginName,
                                                @Param(value = "startTime") Date startTime,
                                                @Param(value = "endTime") Date endTime,
                                                @Param(value = "businessTypes") List<PointBusinessType> businessTypes);
}

package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by qduljs2011 on 2018/7/11.
 */
@Repository
public interface WithdrawMapper {
    void create(WithdrawModel withdrawModel);

    WithdrawModel findById(long id);

    void update(WithdrawModel withdrawModel);


    List<WithdrawPaginationView> findWithdrawPagination(@Param(value = "withdrawId") Long withdrawId,
                                                        @Param(value = "mobile") String mobile,
                                                        @Param(value = "status") WithdrawStatus status,
                                                        @Param(value = "source") Source source,
                                                        @Param(value = "index") int index,
                                                        @Param(value = "pageSize") int pageSize,
                                                        @Param(value = "startTime") Date startTime,
                                                        @Param(value = "endTime") Date endTime);


    long findSumWithdrawAmount(@Param(value = "withdrawId") Long withdrawId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "status") WithdrawStatus status,
                               @Param(value = "source") Source source,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);


    long findSumWithdrawFee(@Param(value = "withdrawId") Long withdrawId,
                            @Param(value = "mobile") String mobile,
                            @Param(value = "status") WithdrawStatus status,
                            @Param(value = "source") Source source,
                            @Param(value = "startTime") Date startTime,
                            @Param(value = "endTime") Date endTime);

    int findWithdrawCount(@Param(value = "withdrawId") Long withdrawId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "status") WithdrawStatus status,
                          @Param(value = "source") Source source,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);
}

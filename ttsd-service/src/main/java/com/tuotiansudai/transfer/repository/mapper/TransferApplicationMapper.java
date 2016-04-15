package com.tuotiansudai.transfer.repository.mapper;

import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferApplicationMapper {

    void create(TransferApplicationModel model);

    void update(TransferApplicationModel model);

    List<TransferApplicationModel> findByTransferInvestId(@Param("transferInvestId")long transferInvestId, @Param("transferStatusList")List<TransferStatus> transferStatusList);

    TransferApplicationModel findById(long id);

    TransferApplicationModel findByInvestId(Long investId);

    List<TransferApplicationRecordDto> findTransferApplicationPaginationByLoginName(@Param("loginName")String loginName,
                                                                                    @Param("transferStatusList")List<TransferStatus> transferStatusList,
                                                                                    @Param(value = "index") Integer index,
                                                                                    @Param(value = "pageSize") Integer pageSize);

    List<TransferApplicationRecordDto> findAllTransferApplicationPagination(@Param(value = "index") Integer index,
                                                                            @Param(value = "pageSize") Integer pageSize,
                                                                            @Param("rateLower") String rateLower,
                                                                            @Param("rateUpper") String rateUpper,
                                                                            @Param("transferStatusList") List<TransferStatus> transferStatusList);

    int findCountAllTransferApplicationPagination(@Param("rateLower") String rateLower,
                                                 @Param("rateUpper") String rateUpper,
                                                 @Param("transferStatusList") List<TransferStatus> transferStatusList);

    int findCountTransferApplicationPaginationByLoginName(@Param("loginName")String loginName, @Param("transferStatusList")List<TransferStatus> transferStatusList);

    List<TransferApplicationRecordDto> findTransfereeApplicationPaginationByLoginName(@Param("loginName")String loginName,
                                                                                    @Param(value = "index") Integer index,
                                                                                    @Param(value = "pageSize") Integer pageSize);

    int findCountTransfereeApplicationPaginationByLoginName(@Param("loginName")String loginName);




}

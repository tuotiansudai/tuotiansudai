package com.tuotiansudai.transfer.repository.mapper;

import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    int findCountTransferApplicationPaginationByLoginName(@Param("loginName")String loginName, @Param("transferStatusList")List<TransferStatus> transferStatusList);

    List<TransferApplicationRecordDto> findTransfereeApplicationPaginationByLoginName(@Param("loginName")String loginName,
                                                                                    @Param(value = "index") Integer index,
                                                                                    @Param(value = "pageSize") Integer pageSize);

    int findCountTransfereeApplicationPaginationByLoginName(@Param("loginName")String loginName);

    List<TransferApplicationRecordDto> findTransferApplicationPaginationList(@Param("transferApplicationId") Long transferApplicationId,
                                                                   @Param("startTime") Date startTime,
                                                                   @Param("endTime") Date endTime,
                                                                   @Param("status") TransferStatus status,
                                                                   @Param("transferrerLoginName") String transferrerLoginName,
                                                                   @Param("transfereeLoginName") String transfereeLoginName,
                                                                   @Param("loanId") Long loanId,
                                                                   @Param(value = "index") Integer index,
                                                                   @Param(value = "pageSize") Integer pageSize);

    int findCountTransferApplicationPagination(@Param("transferApplicationId") Long transferApplicationId,
                                                                   @Param("startTime") Date startTime,
                                                                   @Param("endTime") Date endTime,
                                                                   @Param("status") TransferStatus status,
                                                                   @Param("transferrerLoginName") String transferrerLoginName,
                                                                   @Param("transfereeLoginName") String transfereeLoginName,
                                                                   @Param("loanId") Long loanId);


    List<TransferApplicationRecordDto> findAllTransferApplicationPaginationList(@Param("transferStatus") TransferStatus transferStatus,
                                                                             @Param("rateStart") double rateStart,
                                                                             @Param("rateEnd") double rateEnd,
                                                                             @Param(value = "index") Integer index,
                                                                             @Param(value = "pageSize") Integer pageSize);


    int findCountAllTransferApplicationPagination(@Param("transferStatus") TransferStatus transferStatus,
                                                  @Param("rateStart") double rateStart,
                                                  @Param("rateEnd") double rateEnd);




}

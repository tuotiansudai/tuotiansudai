package com.tuotiansudai.transfer.repository.mapper;

import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferApplicationMapper {

    void create(TransferApplicationModel model);

    void update(TransferApplicationModel model);

    List<TransferApplicationModel> findByTransferInvestId(@Param("transferInvestId") long transferInvestId, @Param("transferStatusList") List<TransferStatus> transferStatusList);

    TransferApplicationModel findById(long id);

    TransferApplicationModel findByInvestId(Long investId);

    List<TransferApplicationRecordDto> findTransferApplicationPaginationByLoginName(@Param("loginName") String loginName,
                                                                                    @Param("transferStatusList") List<TransferStatus> transferStatusList,
                                                                                    @Param(value = "index") Integer index,
                                                                                    @Param(value = "pageSize") Integer pageSize);

    int findCountTransferApplicationPaginationByLoginName(@Param("loginName") String loginName, @Param("transferStatusList") List<TransferStatus> transferStatusList);

    List<TransferApplicationRecordDto> findTransfereeApplicationPaginationByLoginName(@Param("loginName") String loginName,
                                                                                      @Param(value = "index") Integer index,
                                                                                      @Param(value = "pageSize") Integer pageSize);

    int findCountTransfereeApplicationPaginationByLoginName(@Param("loginName") String loginName);


    List<TransferApplicationRecordDto> findTransferApplicationPaginationList(@Param("transferApplicationId") Long transferApplicationId,
                                                                             @Param("startTime") Date startTime,
                                                                             @Param("endTime") Date endTime,
                                                                             @Param("status") TransferStatus status,
                                                                             @Param("transferrerMobile") String transferrerMobile,
                                                                             @Param("transfereeMobile") String transfereeMobile,
                                                                             @Param("loanId") Long loanId,
                                                                             @Param("source") Source source,
                                                                             @Param(value = "index") Integer index,
                                                                             @Param(value = "pageSize") Integer pageSize);

    int findCountTransferApplicationPagination(@Param("transferApplicationId") Long transferApplicationId,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("status") TransferStatus status,
                                               @Param("transferrerMobile") String transferrerMobile,
                                               @Param("transfereeMobile") String transfereeMobile,
                                               @Param("loanId") Long loanId,
                                               @Param("source") Source source);


    List<TransferInvestDetailDto> findTransferInvestList(@Param(value = "investorLoginName") String investorLoginName,
                                                         @Param(value = "index") int index,
                                                         @Param(value = "pageSize") int pageSize,
                                                         @Param(value = "startTime") Date startTime,
                                                         @Param(value = "endTime") Date endTime,
                                                         @Param(value = "loanStatus") LoanStatus loanStatus);

    long findCountInvestTransferPagination(@Param(value = "investorLoginName") String investorLoginName,
                                           @Param(value = "startTime") Date startTime,
                                           @Param(value = "endTime") Date endTime,
                                           @Param(value = "loanStatus") LoanStatus loanStatus);


    List<TransferApplicationRecordDto> findAllTransferApplicationPaginationList(@Param("transferStatus") List<TransferStatus> transferStatus,
                                                                             @Param("rateStart") double rateStart,
                                                                             @Param("rateEnd") double rateEnd,
                                                                             @Param(value = "index") Integer index,
                                                                             @Param(value = "pageSize") Integer pageSize);


    int findCountAllTransferApplicationPagination(@Param("transferStatus") List<TransferStatus> transferStatus,
                                                  @Param("rateStart") double rateStart,
                                                  @Param("rateEnd") double rateEnd);

    long findCountTransferApplicationByApplicationTime(@Param("loginName") String loginName,@Param("tradingTime") Date tradingTime,@Param("activityBeginTime") String activityBeginTime);

    List<TransferApplicationModel> findByTransferInvestIdAndTransferTime(@Param(value = "loginName") String loginName,
                                                                 @Param(value = "year") String year,
                                                                 @Param(value = "month") String month,
                                                                 @Param(value = "day") String day);


}

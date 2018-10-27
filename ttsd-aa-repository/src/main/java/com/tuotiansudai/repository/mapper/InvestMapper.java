package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface InvestMapper {
    /**
     * 创建出借记录
     *
     * @param investModel
     */
    void create(InvestModel investModel);

    /**
     * 更新出借记录
     */
    void update(InvestModel investModel);

    /**
     * 更新转让记录的状态
     *
     * @param id
     * @param transferStatus
     */
    void updateTransferStatus(@Param(value = "id") long id, @Param(value = "transferStatus") TransferStatus transferStatus);

    /**
     * 根据ID查找对应的出借
     *
     * @param id
     * @return
     */
    InvestModel findById(@Param(value = "id") Long id);

    InvestModel lockById(@Param(value = "id") Long id);

    /**
     * 查找用户的出借记录
     *
     * @param loginName
     * @return
     */
    List<InvestModel> findPaginationByLoginName(@Param(value = "loginName") String loginName,
                                                @Param(value = "index") Integer index,
                                                @Param(value = "pageSize") Integer pageSize);

    long findCountByLoginName(@Param(value = "loginName") String loginName);

    List<InvestModel> findByLoginNameExceptTransfer(@Param(value = "loginName") String loginName,
                                                    @Param(value = "index") Integer index,
                                                    @Param(value = "pageSize") Integer pageSize,
                                                    @Param(value = "isPagination") boolean isPagination);

    long findCountByLoginNameExceptTransfer(@Param(value = "loginName") String loginName);

    /**
     * 计算标的的出借总额
     *
     * @param loanId
     * @return
     */
    long sumSuccessInvestAmount(@Param(value = "loanId") long loanId);

    /**
     * 分页获取出借记录
     *
     * @param loanId
     * @param index
     * @param pageSize
     * @param status
     * @return
     */
    List<InvestModel> findByStatus(@Param(value = "loanId") long loanId,
                                   @Param(value = "index") Integer index,
                                   @Param(value = "pageSize") Integer pageSize,
                                   @Param(value = "status") InvestStatus status);

    /**
     * 获取标的的出借记录数
     *
     * @param loanId
     * @param status
     * @return
     */
    long findCountByStatus(@Param(value = "loanId") long loanId,
                           @Param(value = "status") InvestStatus status);

    /**
     * 获取所有出借成功的记录
     *
     * @param loanId
     * @return
     */
    List<InvestModel> findSuccessInvestsByLoanId(@Param(value = "loanId") long loanId);

    /**
     * 将目前仍处于waiting状态的出借记录标记为失败
     *
     * @param loanId
     */
    void cleanWaitingInvest(@Param(value = "loanId") long loanId);

    /**
     * 获取标的是否存在在指定时间后创建，目前仍处于waiting状态的出借记录
     *
     * @param loanId
     * @param afterTime
     * @return
     */
    int findWaitingInvestCountAfter(@Param(value = "loanId") long loanId,
                                    @Param(value = "afterTime") Date afterTime);

    long findCountInvestPagination(@Param(value = "loanId") Long loanId,
                                   @Param(value = "investorLoginName") String investorLoginName,
                                   @Param(value = "channel") String channel,
                                   @Param(value = "source") Source source,
                                   @Param(value = "role") Role role,
                                   @Param(value = "startTime") Date startTime,
                                   @Param(value = "endTime") Date endTime,
                                   @Param(value = "investStatus") InvestStatus investStatus,
                                   @Param(value = "preferenceType") PreferenceType preferenceType,
                                   @Param(value = "productType") ProductType productType,
                                   @Param(value = "transferType") String transferType);

    List<InvestPaginationItemView> findInvestPagination(@Param(value = "loanId") Long loanId,
                                                        @Param(value = "investorLoginName") String investorLoginName,
                                                        @Param(value = "channel") String channel,
                                                        @Param(value = "source") Source source,
                                                        @Param(value = "role") Role role,
                                                        @Param(value = "startTime") Date startTime,
                                                        @Param(value = "endTime") Date endTime,
                                                        @Param(value = "investStatus") InvestStatus investStatus,
                                                        @Param(value = "preferenceType") PreferenceType preferenceType,
                                                        @Param(value = "productType") ProductType productType,
                                                        @Param(value = "transferType") String transferType,
                                                        @Param(value = "index") int index,
                                                        @Param(value = "pageSize") int pageSize);

    long sumInvestAmount(@Param(value = "loanId") Long loanId,
                         @Param(value = "investorLoginName") String investorLoginName,
                         @Param(value = "channel") String channel,
                         @Param(value = "source") Source source,
                         @Param(value = "role") String role,
                         @Param(value = "startTime") Date startTime,
                         @Param(value = "endTime") Date endTime,
                         @Param(value = "investStatus") InvestStatus investStatus,
                         @Param(value = "loanStatus") LoanStatus loanStatus);

    long sumInvestAmountConsole(@Param(value = "loanId") Long loanId,
                                @Param(value = "investorLoginName") String investorLoginName,
                                @Param(value = "channel") String channel,
                                @Param(value = "source") Source source,
                                @Param(value = "role") Role role,
                                @Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "investStatus") InvestStatus investStatus,
                                @Param(value = "preferenceType") PreferenceType preferenceType,
                                @Param(value = "productType") ProductType productType,
                                @Param(value = "transferType") String transferType);

    long sumInvestAmountRanking(@Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime);


    long sumInvestAmountByLoginNameInvestTimeProductType(@Param(value = "loginName") String loginName,
                                                         @Param(value = "startTime") Date startTime,
                                                         @Param(value = "endTime") Date endTime,
                                                         @Param(value = "productTypeList") List<ProductType> productTypeList);

    long sumSuccessInvestAmountByLoginName(@Param(value = "loanId") Long loanId,
                                           @Param(value = "loginName") String loginName,
                                           @Param(value = "includeTransferInvest") boolean includeTransferInvest);

    int sumSuccessInvestCountByLoginName(@Param(value = "loginName") String loginName);

    int countSuccessNewbieInvestByLoginName(@Param(value = "loginName") String loginName);

    long countSuccessInvest(@Param(value = "loanId") Long loanId);

    List<String> findAllChannels();

    List<String> findAllInvestChannels();

    long countAutoInvest(@Param(value = "loanId") Long loanId, @Param(value = "loginName") String loginName);

    List<String> findInvestorLoginNames();

    long findInvestorCount();

    long findRegisteredNotInvestCount();

    Set<String> findNoInvestInThirtyDay();

    InvestModel findLatestSuccessInvest(@Param(value = "loginName") String loginName);

    List<InvestDataView> getInvestDetail();

    int countAchievementTimesByLoginName(@Param(value = "loginName") String loginName,
                                         @Param(value = "achievement") InvestAchievement achievement,
                                         @Param(value = "startTime") Date startTime,
                                         @Param(value = "endTime") Date endTime);

    List<InvestModel> findInvestorInvestPagination(@Param(value = "loginName") String loginName,
                                                   @Param(value = "loanStatus") LoanStatus loanStatus,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize,
                                                   @Param(value = "startTime") Date startTime,
                                                   @Param(value = "endTime") Date endTime);

    long countInvestorInvestPagination(@Param(value = "loginName") String loginName,
                                       @Param(value = "loanStatus") LoanStatus loanStatus,
                                       @Param(value = "startTime") Date startTime,
                                       @Param(value = "endTime") Date endTime);

    Date findFirstTradeTimeInvestByLoanId(@Param(value = "loanId") long loanId);

    List<HeroRankingView> findHeroRankingByTradingTime(@Param(value = "tradingTime") Date tradingTime,
                                                       @Param(value = "activityBeginTime") String activityBeginTime,
                                                       @Param(value = "activityEndTime") String activityEndTime);

    List<HeroRankingView> findHeroRankingByReferrer(@Param(value = "tradingTime") Date tradingTime,
                                                    @Param(value = "activityBeginTime") String activityBeginTime,
                                                    @Param(value = "activityEndTime") String activityEndTime,
                                                    @Param(value = "index") int index,
                                                    @Param(value = "pageSize") int pageSize);

    List<TransferableInvestView> findWebTransferableApplicationPaginationByLoginName(@Param("loginName") String loginName,
                                                                                     @Param(value = "index") Integer index,
                                                                                     @Param(value = "pageSize") Integer pageSize);

    long findWebCountTransferableApplicationPaginationByLoginName(@Param("loginName") String loginName);

    List<InvestModel> findTransferableApplicationPaginationByLoginName(@Param(value = "loginName") String loginName,
                                                                       @Param(value = "index") Integer index,
                                                                       @Param(value = "pageSize") Integer pageSize);

    long findCountTransferableApplicationPaginationByLoginName(@Param(value = "loginName") String loginName);

    List<InvestModel> findInvestAchievementsByLoanId(@Param(value = "loanId") long loanId);

    List<InvestModel> findInvestorInvestWithoutTransferPagination(@Param(value = "loginName") String loginName,
                                                                  @Param(value = "loanStatus") LoanStatus loanStatus,
                                                                  @Param(value = "index") int index,
                                                                  @Param(value = "pageSize") int pageSize);

    long countInvestorInvestWithoutTransferPagination(@Param(value = "loginName") String loginName,
                                                      @Param(value = "loanStatus") LoanStatus loanStatus);

    List<InvestModel> findByLoanIdAndLoginName(@Param(value = "loanId") long loanId,
                                               @Param(value = "loginName") String loginName);

    List<InvestModel> countSuccessInvestByInvestTime(@Param(value = "loanId") Long loanId,
                                                     @Param(value = "startTime") Date startTime,
                                                     @Param(value = "endTime") Date endTime);

    int findCountSuccessByLoginNameAndProductTypes(@Param(value = "loginName") String loginName,
                                                   @Param("productTypeList") List<ProductType> productTypeList);

    long countInvestorSuccessInvestByInvestTime(@Param(value = "loginName") String loginName,
                                                @Param(value = "startTime") Date startTime,
                                                @Param(value = "endTime") Date endTime);

    List<InvestModel> findSuccessInvestByInvestTime(@Param(value = "loginName") String loginName,
                                                    @Param(value = "isContainTransfer") boolean isContainTransfer,
                                                    @Param(value = "isContainExperience") boolean isContainExperience,
                                                    @Param(value = "startTime") Date startTime,
                                                    @Param(value = "endTime") Date endTime);

    void updateContractNoById(@Param(value = "investId") Long investId,
                              @Param(value = "contractNo") String contractNo);

    void updateAllContractNoByLoanId(@Param(value = "loanId") Long loanId,
                                     @Param(value = "contractNo") String contractNo);

    List<InvestModel> findNoContractNoInvest(@Param(value = "loanId") long loanId);

    long sumSuccessActivityInvestAmount(@Param(value = "loginName") String loginName,
                                        @Param(value = "activityDesc") String activityDesc,
                                        @Param(value = "startTime") Date startTime,
                                        @Param(value = "endTime") Date endTime);

    int sumDrawCountByLoginName(@Param(value = "loginName") String loginName,
                                @Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "baseAmount") long baseAmount);

    List<Map<String, String>> findInvestAmountScaleTop3(@Param(value = "endDate") Date endDate);

    long findInvestAmountScale(@Param(value = "endDate") Date endDate);


    List<InvestModel> findInvestorInvestAndTransferPagination(@Param(value = "loginName") String loginName,
                                                              @Param(value = "loanStatus") LoanStatus loanStatus,
                                                              @Param(value = "index") int index,
                                                              @Param(value = "pageSize") int pageSize);

    long countInvestorInvestAndTransferPagination(@Param(value = "loginName") String loginName,
                                                  @Param(value = "loanStatus") LoanStatus loanStatus);

    int countInvestBeforeDate(@Param(value = "loginName") String loginName,
                              @Param(value = "datePoint") Date datePoint);

    InvestModel findFirstInvestAmountByLoginName(@Param(value = "loginName") String loginName,
                                                 @Param(value = "startTime") Date startTime,
                                                 @Param(value = "endTime") Date endTime);

    List<InvestAchievementView> findAmountOrderByLoanId(@Param(value = "loanId") long loanId,
                                                        @Param(value = "activityStartTime") Date activityStartTime,
                                                        @Param(value = "activityEndTime") Date activityEndTime,
                                                        @Param(value = "limit") Integer limit);

    List<InvestModel> findSuccessByLoginNameExceptTransferAndTime(@Param(value="loginName") String loginName,
                                                                  @Param(value="startTime") Date startTime,
                                                                  @Param(value="endTime") Date endTime);

    List<LoanInvestAmountView> findAmountByNationalDayActivity(@Param(value="startTime") Date startTime,
                                         @Param(value="endTime") Date endTime,
                                         @Param(value = "loanActivityDescList") List<String> loanActivityDescList);

    List<InvestProductTypeView> findAmountOrderByNameAndProductType(@Param(value = "startTime") Date startTime,
                                                                    @Param(value = "endTime") Date endTime,
                                                                    @Param(value = "activityDesc") String activityDesc);

    List<InvestModel> findSuccessDoubleElevenActivityByTime(@Param(value = "loanId") Long loanId,
                                                            @Param(value="startTime") Date startTime,
                                                            @Param(value="endTime") Date endTime);

    long sumAmountActivityDoubleElevenByLoginName(@Param(value = "loginName") String loginName,
                                                  @Param(value = "startTime") Date startTime,
                                                  @Param(value="endTime") Date endTime);

    List<String> findInvestorMobileByLoanId(@Param(value = "loanId") long loanId);

    List<InvestModel> findTransfeeInvestByTransfer(@Param(value = "transfer") String transfer);

    long sumUsedFund(@Param(value = "loginName")String loginName);

    void updateIsOverdueTransfer(@Param(value = "investId") long investId);
}

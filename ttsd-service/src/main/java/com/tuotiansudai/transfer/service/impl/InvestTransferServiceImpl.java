package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.dto.sms.JianZhouSmsTemplate;
import com.tuotiansudai.dto.sms.SmsDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.CalculateLeftDays;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestTransferServiceImpl implements InvestTransferService {

    static Logger logger = Logger.getLogger(InvestTransferServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;


    protected final static String TRANSFER_APPLY_NAME = "ZR{0}-{1}";

    public static String redisTransferApplicationNumber = "web:{0}:transferApplicationNumber";

    @Override
    public BaseDto<BaseDataDto> isInvestTransferable(long investId) {
        BaseDto<BaseDataDto> dto = new BaseDto<>(new BaseDataDto());
        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null) {
            dto.getData().setMessage("债权不存在。");
            return dto;
        }

        if (CollectionUtils.isNotEmpty(transferApplicationMapper.findByTransferInvestId(investId, Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING)))) {
            dto.getData().setMessage("债权已申请转让。");
            return dto;
        }

        long loanId = investModel.getLoanId();
        LoanRepayModel currentLoanRepay = loanRepayMapper.findCurrentLoanRepayByLoanId(loanId);
        int transferDaysLimit = transferRuleMapper.find().getDaysLimit();
        if (Days.daysBetween(new DateTime(), new DateTime(currentLoanRepay.getRepayDate())).getDays() < transferDaysLimit) {
            dto.getData().setMessage(MessageFormat.format("该项目即将在{0}日内回款，暂不可转让，请选择其他项目。", String.valueOf(transferDaysLimit)));
            return dto;
        }

        if (loanMapper.findById(loanId).getStatus() != LoanStatus.REPAYING) {
            dto.getData().setMessage("该项目已提前回款，不可进行转让。");
            return dto;
        }

        dto.getData().setStatus(true);
        return dto;
    }


    @Override
    public TransferApplicationFormDto getApplicationForm(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();

        Date expiredDate = new DateTime().plusDays(transferRuleModel.getDaysLimit() + 1).withTimeAtStartOfDay().toDate();
        long transferFee = TransferRuleUtil.getTransferFee(loanModel.getType(), loanModel.getRecheckTime(), investModel.getAmount(), investModel.getCreatedTime(), transferRuleModel);
        double transferFeeRate = TransferRuleUtil.getTransferFeeRate(loanModel.getType(), loanModel.getRecheckTime(), investModel.getCreatedTime(), transferRuleModel);
        long transferAmountLower = new BigDecimal(1 - transferRuleModel.getDiscount()).multiply(new BigDecimal(investModel.getAmount())).setScale(0, BigDecimal.ROUND_UP).longValue();
        int holdDays = TransferRuleUtil.getInvestHoldDays(loanModel.getType(), loanModel.getRecheckTime(), investModel.getCreatedTime());

        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(investModel.getLoginName());

        return new TransferApplicationFormDto(investId, investModel.getAmount(), transferAmountLower, transferFeeRate, transferFee, expiredDate, holdDays,
                anxinProp != null && anxinProp.isAnxinUser(),
                anxinWrapperClient.isAuthenticationRequired(investModel.getLoginName()).getData().getStatus());
    }

    @Override
    @Transactional
    public boolean investTransferApply(TransferApplicationDto transferApplicationDto) {
        InvestModel investModel = investMapper.lockById(transferApplicationDto.getTransferInvestId());

        if (investModel.getStatus() != InvestStatus.SUCCESS) {
            logger.error(MessageFormat.format("[Transfer Apply {0}] invest status({1}) is not SUCCESS", String.valueOf(investModel.getId()), investModel.getStatus()));
            return false;
        }

        if (investModel.getAmount() < transferApplicationDto.getTransferAmount()) {
            logger.error(MessageFormat.format("[Transfer Apply {0}] invest amount({1}) is less than transfer amount({2})",
                    String.valueOf(investModel.getId()), String.valueOf(investModel.getAmount()), String.valueOf(transferApplicationDto.getTransferAmount())));
            return false;
        }

        if (loanMapper.findById(investModel.getLoanId()).getStatus() != LoanStatus.REPAYING) {
            logger.error(MessageFormat.format("[Transfer Apply {0}] loan status is not REPAYING", String.valueOf(investModel.getId())));
            return false;
        }

        boolean isApplied = CollectionUtils.isNotEmpty(transferApplicationMapper.findByTransferInvestId(investModel.getId(),
                Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING)));
        if (isApplied) {
            logger.error(MessageFormat.format("[Transfer Apply {0}] invest was applied", String.valueOf(investModel.getId())));
            return false;
        }

        boolean isCanceledToday = Iterators.any(transferApplicationMapper.findByTransferInvestId(investModel.getId(),
                Lists.newArrayList(TransferStatus.CANCEL)).iterator(), input -> new DateTime(input.getApplicationTime()).isEqual(new DateTime().withTimeAtStartOfDay()));
        if (isCanceledToday) {
            logger.error(MessageFormat.format("[Transfer Apply {0}] invest application was canceled today", String.valueOf(investModel.getId())));
            return false;
        }

        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(investModel.getLoanId());
        int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(transferApplicationDto.getTransferInvestId(), loanRepayModel.getPeriod());

        long transferFee = TransferRuleUtil.getTransferFee(loanModel.getType(), loanModel.getRecheckTime(), investModel.getAmount(), investModel.getCreatedTime(), transferRuleModel);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, this.generateTransferApplyName(), loanRepayModel.getPeriod(), transferApplicationDto.getTransferAmount(),
                transferFee, getDeadlineFromNow(), leftPeriod, transferApplicationDto.getSource());

        transferApplicationMapper.create(transferApplicationModel);

        investMapper.updateTransferStatus(investModel.getId(), TransferStatus.TRANSFERRING);

        this.investTransferApplyJob(transferApplicationModel);

        return true;
    }

    @Override
    public Date getDeadlineFromNow() {
        return new DateTime().plusDays(transferRuleMapper.find().getDaysLimit() + 1).withTimeAtStartOfDay().toDate();
    }

    @Override
    public boolean cancelTransferApplicationManually(long transferApplicationId) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING) {
            return false;
        }

        transferApplicationModel.setStatus(TransferStatus.CANCEL);
        transferApplicationMapper.update(transferApplicationModel);
        investMapper.updateTransferStatus(transferApplicationModel.getTransferInvestId(), TransferStatus.TRANSFERABLE);

        return true;
    }


    @Override
    public boolean cancelTransferApplication(long transferApplicationId) {
        boolean status = this.cancelTransferApplicationManually(transferApplicationId);

        if (!status) {
            return false;
        }

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        //Title:您提交的债权转让到期取消，请查看！
        //Content:尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.TRANSFER_FAIL,
                Lists.newArrayList(transferApplicationModel.getLoginName()),
                MessageEventType.TRANSFER_FAIL.getTitleTemplate(),
                MessageEventType.TRANSFER_FAIL.getContentTemplate(),
                transferApplicationId));

        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(transferApplicationModel.getLoginName()),
                PushSource.ALL,
                PushType.TRANSFER_FAIL,
                MessageEventType.TRANSFER_FAIL.getTitleTemplate(),
                AppUrl.MESSAGE_CENTER_LIST));

        String mobile = userMapper.findByLoginName(transferApplicationModel.getLoginName()).getMobile();
        mqWrapperClient.sendMessage(MessageQueue.UserSms, new SmsDto(JianZhouSmsTemplate.SMS_TRANSFER_LOAN_OVERDUE_TEMPLATE, Lists.newArrayList(mobile), Lists.newArrayList(transferApplicationModel.getName())));

        return true;
    }

    private void investTransferApplyJob(TransferApplicationModel transferApplicationModel) {
        if (!transferApplicationModel.getDeadline().after(new Date())) {
            logger.info("investTransferApplyJob create failed, expect deadline is before now, id = " + transferApplicationModel.getId());
            return;
        }
        DelayMessageDeliveryJobCreator.createCancelTransferApplicationDelayJob(jobManager, transferApplicationModel.getId(), transferApplicationModel.getDeadline());
    }

    protected String generateTransferApplyName() {
        String date = new DateTime().toString("yyyyMMdd");
        String name = String.format("%03d", redisWrapperClient.incr(MessageFormat.format(redisTransferApplicationNumber, date)));
        return MessageFormat.format(TRANSFER_APPLY_NAME, date, name);
    }

    @Override
    public boolean isTransferable(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS) {
            logger.info(MessageFormat.format("{0} is not exist or invest failed", investId));
            return false;
        }
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        if (loanModel.getStatus() != LoanStatus.REPAYING) {
            logger.info(MessageFormat.format("{0} is not REPAYING", investModel.getLoanId()));
            return false;
        }
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
        if (loanDetailsModel != null && loanDetailsModel.getNonTransferable()) {
            return false;
        }

        if (!validTransferIsCanceled(investId)) {
            logger.debug(MessageFormat.format("{0} is transferred", investModel.getLoanId()));
            return false;
        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(investModel.getLoanId());
        if (loanRepayModel == null) {
            logger.info(MessageFormat.format("{0} is completed ", investModel.getLoanId()));
            return false;
        }

        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        if (!transferRuleModel.isMultipleTransferEnabled()) {
            TransferApplicationModel transfereeApplicationModel = transferApplicationMapper.findByInvestId(investId);
            if (transfereeApplicationModel != null) {
                logger.info(MessageFormat.format("{0} MultipleTransferEnabled is false ", investId));
                return false;
            }

        }

        if (!validTransferIsDayLimit(investModel.getLoanId())) {
            logger.debug(MessageFormat.format("{0} right away repay ", investId));
            return false;
        }

        return true;
    }

    @Override
    public boolean validTransferIsDayLimit(long loanId) {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        DateTime current = new DateTime().withTimeAtStartOfDay();
        LoanRepayModel currentLoanRepay = loanRepayMapper.findCurrentLoanRepayByLoanId(loanId);

        if (currentLoanRepay == null) {
            return false;
        }

        int periodDuration = Days.daysBetween(current.withTimeAtStartOfDay(), new DateTime(currentLoanRepay.getRepayDate()).withTimeAtStartOfDay()).getDays();

        if (periodDuration <= transferRuleModel.getDaysLimit()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validTransferIsCanceled(long investId) {
        DateTime current = new DateTime().withTimeAtStartOfDay();
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investId, Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING, TransferStatus.CANCEL));
        for (TransferApplicationModel transferApplicationModelTemp : transferApplicationModels) {
            if (transferApplicationModelTemp.getStatus() != TransferStatus.CANCEL) {
                return false;
            }
            DateTime applyTransferTime = new DateTime(transferApplicationModelTemp.getApplicationTime()).withTimeAtStartOfDay();
            if (transferApplicationModelTemp.getStatus() == TransferStatus.CANCEL && current.compareTo(applyTransferTime) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName, List<TransferStatus> statusList, Integer index, Integer pageSize) {

        int count = transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(transferrerLoginName, statusList);
        List<TransferApplicationRecordView> items = Lists.newArrayList();
        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findTransferApplicationPaginationByLoginName(transferrerLoginName, statusList, (index - 1) * pageSize, pageSize);

        }
        List<TransferApplicationPaginationItemDataDto> records = Lists.transform(items, input -> {
            TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto(input);
            if (input.getTransferStatus() == TransferStatus.TRANSFERABLE) {
                transferApplicationPaginationItemDataDto.setTransferStatus(isTransferable(input.getTransferApplicationId()) ? input.getTransferStatus().getDescription() : "--");
            } else if (input.getTransferStatus() == TransferStatus.NONTRANSFERABLE) {
                transferApplicationPaginationItemDataDto.setTransferStatus("--");
            } else {
                transferApplicationPaginationItemDataDto.setTransferStatus(input.getTransferStatus().getDescription());
                InvestModel investModel = investMapper.findById(input.getInvestId());
                if (input.getTransferStatus() == TransferStatus.CANCEL) {
                    transferApplicationPaginationItemDataDto.setCancelTransfer(true);
                } else if (investModel != null && !Strings.isNullOrEmpty(investModel.getContractNo()) && !investModel.getContractNo().equals("OLD")) {
                    transferApplicationPaginationItemDataDto.setTransferNewSuccess(true);
                    transferApplicationPaginationItemDataDto.setContractNo(investModel.getContractNo());
                } else {
                    transferApplicationPaginationItemDataDto.setTransferOldSuccess(true);
                }
            }
            LoanModel loanModel = loanMapper.findById(input.getLoanId());
            InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(input.getTransferInvestId(), loanModel.getPeriods());
            Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
            transferApplicationPaginationItemDataDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
            return transferApplicationPaginationItemDataDto;
        });

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> dto = new BasePaginationDataDto(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    public BasePaginationDataDto<TransferInvestDetailView> getInvestTransferList(String investorLoginName,
                                                                                 int index,
                                                                                 int pageSize,
                                                                                 Date startTime,
                                                                                 Date endTime,
                                                                                 LoanStatus loanStatus) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = CalculateUtil.calculateMaxDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<TransferInvestDetailView> items = Lists.newArrayList();
        long count = transferApplicationMapper.findCountInvestTransferPagination(investorLoginName, startTime, endTime, loanStatus);

        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findTransferInvestList(investorLoginName, (index - 1) * pageSize, pageSize, startTime, endTime, loanStatus);
        }

        items.forEach(transferInvestDetailView -> {
            if (ContractNoStatus.OLD.name().equals(transferInvestDetailView.getContractNo())) {
                transferInvestDetailView.setContractOld("1");
            } else if (StringUtils.isNotEmpty(transferInvestDetailView.getContractNo())) {
                transferInvestDetailView.setContractOK("1");
            }
        });

        BasePaginationDataDto dto = new BasePaginationDataDto(index, pageSize, count, items);
        dto.setStatus(true);
        return dto;
    }

}

package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.tuotiansudai.dto.ContractInvestDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContractServiceImpl implements ContractService {
    static Logger logger = Logger.getLogger(ContractServiceImpl.class);
    /**
     * 后缀为FTL
     */
    private static final String FTL = ".ftl";

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Override
    public String getContract(String templateName, Map<String, Object> dataModel) {
        Writer out = null;
        StringReader reader = null;
        try {
            out = new StringWriter();
            Configuration cfg = new Configuration(new Version(2, 3, 23));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(ContractServiceImpl.class, "/");
            Template template = cfg.getTemplate(templateName + FTL);
            template.process(dataModel, out);
            reader = new StringReader(out.toString());
            out.flush();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        BufferedReader br = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();
        String str = null;
        try {
            while ((str = br.readLine()) != null) {
                content.append(str);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return content.toString();
    }

    @Override
    public String generateInvestorContract(String loginName, long loanId, ContractType contractType) {
        Map<String, Object> dataModel = this.collectContractModel(loginName, loanId, contractType);
        if (dataModel.isEmpty()) {
            return "";
        }
        String content = getContract("contract", dataModel).replace("&nbsp;", "&#160;");
        return content;
    }

    public String generateInvestorContract(String loginName, long loanId, long investId) {
        Map<String, Object> dataModel = collectInvestorContractModel(loginName, loanId, investId);
        if (dataModel.isEmpty()) {
            return "";
        }
        String content
    }

    @Override
    public void generateContractPdf(String pdfString, OutputStream outputStream) {

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();

        try {
            fontResolver.addFont(ContractServiceImpl.class.getClassLoader().getResource("SIMSUN.TTC").toString(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.setDocumentFromString(pdfString);
            //renderer.setDocument(xhtmlContent,"");
            renderer.layout();
            renderer.createPDF(outputStream, true);
        } catch (DocumentException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private Map<String, Object> collectInvestorContractModel(String investorLoginName, long loanId, long investId) {
        Map<String, Object> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        AccountModel agentAccount = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        AccountModel investorAccount = accountMapper.findByLoginName(investorLoginName);
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentAccount.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", investorAccount.getIdentityNumber());
        dataModel.put("loanerUserName", agentAccount.getUserName());
        dataModel.put("loanerIdentityNumber", agentAccount.getIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("periods", loanModel.getPeriods());
        dataModel.put("totalRate", loanModel.getBaseRate() + loanModel.getActivityRate());
        dataModel.put("recheckTime", loanModel.getRecheckTime());
        dataModel.put("endTime", investRepayModel.getRepayDate());
        if (loanModel.getPledgeType().equals(PledgeType.HOUSE)) {
            dataModel.put("pledge", "房屋");
        } else if (loanModel.getPledgeType().equals(PledgeType.VEHICLE)) {
            dataModel.put("pledge", "车辆");
        }
        return dataModel;
    }

    private Map<String, Object> collectContractModel(String loginName, long loanId, ContractType contractType) {
        Map<String, Object> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return dataModel;
        }
        AccountModel agentAccountModel = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        dataModel.put("loanId", "" + loanId);
        dataModel.put("loanerUserName", Strings.nullToEmpty(loanModel.getLoanerUserName()));
        dataModel.put("loanerLoginName", Strings.nullToEmpty(loanModel.getLoanerLoginName()));
        dataModel.put("loanerIdentityNumber", Strings.nullToEmpty(loanModel.getLoanerIdentityNumber()));

        dataModel.put("agentUserName", Strings.nullToEmpty(agentAccountModel.getUserName()));
        dataModel.put("agentLoginName", Strings.nullToEmpty(loanModel.getAgentLoginName()));
        dataModel.put("agentIdentityNumber", Strings.nullToEmpty(agentAccountModel.getIdentityNumber()));
        dataModel.put("investList", getContractInvestList(loginName, loanId, loanModel, contractType));
        //dataModel.put("investList", getInvestListTable(loginName, loanId, loanModel, contractType));
        dataModel.put("actualMoney", AmountConverter.convertCentToString(loanModel.getLoanAmount()));

        dataModel.put("fen", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 0));
        dataModel.put("bugle", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 1));
        dataModel.put("yuan", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 2));
        dataModel.put("ten", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 3));
        dataModel.put("hundred", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 4));
        dataModel.put("thousand", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 5));
        dataModel.put("tenThousand", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 6));
        dataModel.put("hundredThousand", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 7));
        dataModel.put("million", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 8));

        dataModel.put("deadline", "" + loanModel.getPeriods());
        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            Date repayDay = loanRepayModels.get(0).getRepayDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(repayDay);
            dataModel.put("repayDay", cal.get(Calendar.DATE));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date endDate = null;
        if (LoanPeriodUnit.DAY.equals(loanModel.getType().getLoanPeriodUnit())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loanModel.getRecheckTime());
            cal.add(Calendar.DATE, loanModel.getPeriods());
            endDate = cal.getTime();
        } else if (LoanPeriodUnit.MONTH.equals(loanModel.getType().getLoanPeriodUnit())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loanModel.getRecheckTime());
            cal.add(Calendar.MONTH, loanModel.getPeriods());
            endDate = cal.getTime();
        }
        dataModel.put("interestBeginTime", format.format(loanModel.getRecheckTime()));
        dataModel.put("interestEndTime", format.format(endDate));
        //TODO:罚息比例修改为系统配置或者数据库配置
        Double overdueRepayInvestor = 0.0002;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        dataModel.put("overdue_repay_investor", nt.format(overdueRepayInvestor));
        dataModel.put("loanPurpose", loanModel.getName());

        return dataModel;
    }

    enum EncryptType {
        LOGIN_NAME,
        REAL_NAME,
        ID_CARD
    }

    private List<ContractInvestDto> getContractInvestList(String loginName, long loanId, LoanModel loanModel, ContractType contractType) {
        List<InvestModel> invests = investMapper.findSuccessInvestsByLoanId(loanId);
        List<ContractInvestDto> contractInvestDtos = new ArrayList<>();
        for (InvestModel invest : invests) {
            AccountModel accountModel = accountMapper.findByLoginName(invest.getLoginName());

            if (!invest.getLoginName().equals(loginName) && ContractType.INVEST == contractType) {
                contractInvestDtos.add(new ContractInvestDto(encryptData(accountModel.getLoginName(), EncryptType.LOGIN_NAME),
                        encryptData(accountModel.getUserName(), EncryptType.REAL_NAME), encryptData(accountModel.getIdentityNumber(), EncryptType.ID_CARD),
                        AmountConverter.convertCentToString(invest.getAmount()), loanModel.getPeriods() + "(" + loanModel.getType().getLoanPeriodUnit().getDesc() + ")",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(null == invest.getTradingTime() ? invest.getCreatedTime() : invest.getTradingTime())));
            } else {
                contractInvestDtos.add(new ContractInvestDto(accountModel.getLoginName(), accountModel.getUserName(),
                        accountModel.getIdentityNumber(), AmountConverter.convertCentToString(invest.getAmount()),
                        loanModel.getPeriods() + "(" + loanModel.getType().getLoanPeriodUnit().getDesc() + ")",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(null == invest.getTradingTime() ? invest.getCreatedTime() : invest.getTradingTime())));
            }
        }
        return contractInvestDtos;
    }

    private String encryptData(String source, EncryptType encryptType) {
        switch (encryptType) {
            case LOGIN_NAME:
                if (source.length() > 3) {
                    source = source.substring(0, 3) + "***";
                }
                break;
            case REAL_NAME:
                if (source.length() >= 1) {
                    source = source.substring(0, 1) + "*";
                }
                break;
            case ID_CARD:
                if (source.length() >= 4) {
                    source = source.substring(0, 4) + "**************";
                }
                break;
        }
        return source;
    }

    private String getDigitBySerialNo(String dou, int serialNo) {
        String digit = "";
        char[] digits = new StringBuffer(dou.replace(".", "")).reverse().toString().toCharArray();
        if (digits.length > serialNo) {
            digit = String.valueOf(digits[serialNo]);
        }
        return digit;
    }

    @Override
    public String generateTransferContract(long transferApplicationId) {

        Map<String, Object> dataModel = this.collectTransferContractModel(transferApplicationId);
        if (dataModel.isEmpty()) {
            return "";
        }
        return getContract("transferContract", dataModel).replace("&nbsp;", "&#160;");
    }

    private Map<String, Object> collectTransferContractModel(long transferApplicationId) {
        Map<String, Object> dataModel = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (null == transferApplicationModel) {
            return dataModel;
        }

        AccountModel transferrerAccountModel = accountMapper.findByLoginName(transferApplicationModel.getLoginName());
        if (transferrerAccountModel != null) {
            dataModel.put("transferrerUserName", transferrerAccountModel.getUserName());
            dataModel.put("transferrerMobile", userMapper.findByLoginName(transferrerAccountModel.getLoginName()).getMobile());
            dataModel.put("transferrerIdentityNumber", transferrerAccountModel.getIdentityNumber());
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        AccountModel investAccountModel = accountMapper.findByLoginName(investModel.getLoginName());
        if (investAccountModel != null) {
            dataModel.put("transfereeUserName", investAccountModel.getUserName());
            dataModel.put("transfereeMobile", userMapper.findByLoginName(investAccountModel.getLoginName()).getMobile());
            dataModel.put("transfereeIdentityNumber", investAccountModel.getIdentityNumber());
        }

        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (null != loanModel) {
            dataModel.put("loanerUserName", loanerDetailsMapper.getLoanerDetailByLoanId(loanModel.getId()).getUserName());
            dataModel.put("loanerIdentityNumber", loanModel.getLoanerIdentityNumber());
            dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
            dataModel.put("totalRate", loanModel.getBaseRate() + loanModel.getActivityRate());
            dataModel.put("periods", loanModel.getPeriods());
        }

        if (transferApplicationModel.getPeriod() != 1) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), transferApplicationModel.getPeriod() - 1);
            dataModel.put("transferStartTime", simpleDateFormat.format(new LocalDate(investRepayModel.getRepayDate()).plusDays(1).toDate()));
        } else {
            if (loanModel.getType().equals(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY) || loanModel.getType().equals(LoanType.INVEST_INTEREST_MONTHLY_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(investModel.getInvestTime()));
            } else if (loanModel.getType().equals(LoanType.LOAN_INTEREST_MONTHLY_REPAY) || loanModel.getType().equals(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(loanModel.getRecheckTime()));
            }
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanModel.getPeriods());
        dataModel.put("transferEndTime", simpleDateFormat.format(investRepayModel.getRepayDate()));

        dataModel.put("investAmount", AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
        dataModel.put("transferTime", simpleDateFormat.format(transferApplicationModel.getTransferTime()));
        dataModel.put("leftPeriod", transferApplicationModel.getLeftPeriod());

        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        dataModel.put("fee30", transferRuleModel.getLevelOneFee());
        dataModel.put("fee30_90", transferRuleModel.getLevelTwoFee());
        dataModel.put("fee90", transferRuleModel.getLevelThreeFee());

        return dataModel;
    }
}
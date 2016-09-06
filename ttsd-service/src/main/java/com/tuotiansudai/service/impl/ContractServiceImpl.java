package com.tuotiansudai.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
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
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
    public String generateInvestorContract(String loginName, long loanId, long investId) {
        Map<String, Object> dataModel = collectInvestorContractModel(loginName, loanId, investId);
        if (dataModel.isEmpty()) {
            return "";
        }
        return getContract("contract", dataModel).replace("&nbsp;", "&#160;");
    }

    private Map<String, Object> collectInvestorContractModel(String investorLoginName, long loanId, long investId) {
        Map<String, Object> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        AccountModel agentAccount = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        AccountModel investorAccount = accountMapper.findByLoginName(investorLoginName);
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getLoanerDetailByLoanId(loanId);
        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentAccount.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", investorAccount.getIdentityNumber());
        dataModel.put("loanerUserName", loanerDetailsModel.getUserName());
        dataModel.put("loanerIdentityNumber", loanerDetailsModel.getIdentityNumber());
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
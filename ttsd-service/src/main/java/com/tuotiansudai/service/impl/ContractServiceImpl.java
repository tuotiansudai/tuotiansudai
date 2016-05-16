package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ContractService;
import com.tuotiansudai.util.AmountConverter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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
    private InvestMapper investMapper;

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
    public String generateInvestorContract(String loginName,long loanId,ContractType contractType) {
        Map<String, Object> dataModel = this.collectContractModel(loginName,loanId,contractType);
        if(dataModel.isEmpty()){
            return "";
        }
        String content = getContract("contract", dataModel).replace("&nbsp;", "&#160;");
        return content;

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
            logger.error(e.getLocalizedMessage(),e);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }

    private Map<String, Object> collectContractModel(String loginName,long loanId,ContractType contractType) {
        Map<String, Object> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        if(loanModel == null){
            return dataModel;
        }
        AccountModel agentAccountModel = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        dataModel.put("loanId","" + loanId);
        dataModel.put("loanerUserName",Strings.nullToEmpty(loanModel.getLoanerUserName()));
        dataModel.put("loanerLoginName",Strings.nullToEmpty(loanModel.getLoanerLoginName()));
        dataModel.put("loanerIdentityNumber",Strings.nullToEmpty(loanModel.getLoanerIdentityNumber()));

        dataModel.put("agentUserName",Strings.nullToEmpty(agentAccountModel.getUserName()));
        dataModel.put("agentLoginName",Strings.nullToEmpty(loanModel.getAgentLoginName()));
        dataModel.put("agentIdentityNumber",Strings.nullToEmpty(agentAccountModel.getIdentityNumber()));
        dataModel.put("investList",getInvestListTable(loginName,loanId,loanModel,contractType));
        dataModel.put("actualMoney", AmountConverter.convertCentToString(loanModel.getLoanAmount()));

        dataModel.put("fen", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 0));
        dataModel.put("bugle", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 1));
        dataModel.put("yuan", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 2));
        dataModel.put("ten", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 3));
        dataModel.put("hundred", this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 4));
        dataModel.put("thousand",this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 5));
        dataModel.put("tenThousand",this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 6));
        dataModel.put("hundredThousand",this.getDigitBySerialNo(AmountConverter.convertCentToString(loanModel.getLoanAmount()), 7));

        dataModel.put("deadline","" + loanModel.getPeriods());
        if(CollectionUtils.isNotEmpty(loanRepayModels)){
            Date repayDay = loanRepayModels.get(0).getRepayDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(repayDay);
            dataModel.put("repayDay",cal.get(Calendar.DATE));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date endDate = null;
        if (LoanPeriodUnit.DAY.equals(loanModel.getType().getLoanPeriodUnit())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loanModel.getRecheckTime());
            cal.add(Calendar.DATE,loanModel.getPeriods());
            endDate = cal.getTime();
        } else if (LoanPeriodUnit.MONTH.equals(loanModel.getType().getLoanPeriodUnit())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loanModel.getRecheckTime());
            cal.add(Calendar.MONTH,loanModel.getPeriods());
            endDate = cal.getTime();
        }
        dataModel.put("interestBeginTime",format.format(loanModel.getRecheckTime()));
        dataModel.put("interestEndTime",format.format(endDate));
        //TODO:罚息比例修改为系统配置或者数据库配置
        Double overdueRepayInvestor = 0.0005;
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        dataModel.put("overdue_repay_investor", nt.format(overdueRepayInvestor));
        dataModel.put("loanPurpose",loanModel.getName());

        return dataModel;
    }


    private String getInvestListTable(String loginName,long loanId,LoanModel loanModel,ContractType contractType){

        Element table = Jsoup
                .parseBodyFragment("<table border='1' style='margin: 0px auto; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width: 80%; '><tbody><tr class='firstRow'><td style='text-align:center;'>平台账号</td><td style='text-align:center;'>真实姓名</td><td style='text-align:center;'>身份证号</td><td style='text-align:center;'>出借金额</td><td style='text-align:center;'>借款期限</td><td style='text-align:center;'>投资确认日期</td></tr></tbody></table>");
        Element tbody = table.getElementsByTag("tbody").first();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<InvestModel> invests = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel invest : invests) {
            AccountModel accountModel = accountMapper.findByLoginName(invest.getLoginName());
            String rowsString = insertRow(6);
            String deadLine = loanModel.getPeriods() + "(" + loanModel.getType().getLoanPeriodUnit().getDesc() + ")";
            rowsString = rowsString.replace("#{0}",encryString(invest.getLoginName(), "platformAccount", invest.getLoginName(), loginName, contractType))
                    .replace("#{1}", encryString(accountModel.getUserName(), "realName", invest.getLoginName(), loginName,contractType))
                    .replace("#{2}",encryString(accountModel.getIdentityNumber(), "IdCard", invest.getLoginName(), loginName, contractType))
                    .replace("#{3}", AmountConverter.convertCentToString(invest.getAmount()))
                    .replace("#{4}",deadLine)
                    .replace("#{5}", format.format(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime()));

            tbody.append(rowsString);
        }
        return table.outerHtml();
    }
    private   String encryString(String sourceString,String type,String investId,String loginUserId,ContractType contractType){

        String encryString = sourceString;

        if (ContractType.INVEST.equals(contractType)){
            if (!investId.equals(loginUserId)){

                if ("platformAccount".equals(type)){//平台账号

                    if (sourceString.length() >= 3){
                        encryString = sourceString.substring(0,3)+ "***";
                    }

                }else if("realName".equals(type)){//真实姓名

                    if (sourceString.length() >= 1){
                        encryString = sourceString.substring(0,1)+ "*";
                    }

                }else if("IdCard".equals(type)){//身份证

                    if (sourceString.length() >= 4){
                        encryString = sourceString.substring(0,4)+ "**************";
                    }
                }

            }
        }

        return encryString;

    }

    private String insertRow(int tdNum){
        String rowsString = "";
        String rowsHead = "<tr style='text-align:center;'>";
        String rowsFoot = "</tr>";
        String rowsBody = "";
        for(int i = 0 ; i < tdNum ;i++){
            rowsBody += "<td>";
            rowsBody += "#{" + i +"}";
            rowsBody += "</td>";
        }
        rowsString = rowsHead + rowsBody + rowsFoot;
        return rowsString;
    }

    private String getDigitBySerialNo(String dou,int serialNo){
        String digit = "";
        char[] digits = new StringBuffer(dou.replace(".", "")).reverse().toString().toCharArray();
        if(digits.length > serialNo){
            digit = String.valueOf(digits[serialNo]) ;
        }
        return  digit;
    }
}

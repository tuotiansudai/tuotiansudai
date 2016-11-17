package com.tuotiansudai.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.service.ContractService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {
    static Logger logger = Logger.getLogger(ContractServiceImpl.class);

    @Autowired
    private AnxinSignService anxinSignService;

    /**
     * 后缀为FTL
     */
    private static final String FTL = ".ftl";

    @Override
    public String getContract(String templateName, Map<String, String> dataModel) {
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
        Map<String, String> dataModel = anxinSignService.collectInvestorContractModel(loginName, loanId, investId);
        if (dataModel.isEmpty()) {
            return "";
        }
        return getContract("contract", dataModel).replace("&nbsp;", "&#160;");
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

        Map<String, String> dataModel = anxinSignService.collectTransferContractModel(transferApplicationId);
        if (dataModel.isEmpty()) {
            return "";
        }
        return getContract("transferContract", dataModel).replace("&nbsp;", "&#160;");
    }

}
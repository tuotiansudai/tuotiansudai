package com.esoft.jdp2p.invest.controller;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.NumberFormat;



import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.esoft.archer.user.model.User;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.esoft.archer.node.model.Node;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.RepayConstants;
import com.esoft.jdp2p.risk.model.FeeConfig;
import com.google.common.base.Strings;
import com.lowagie.text.pdf.BaseFont;

@Component
@Scope(ScopeType.VIEW)
public class ContractHome {

	@Resource
	private HibernateTemplate ht;

	@Resource
	private DictUtil dictUtil;

	private String contractContent;

	public void contractPdfDownload(String fileName) {
		String body = contractContent.replace("&nbsp;", "&#160;");

		body = "<html><head></head><body style=\"font-family:'SimSun';\">"
				+ body + "</body></html>";
		StringReader contentReader = new StringReader(body);
		InputSource source = new InputSource(contentReader);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		ServletOutputStream sos = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document xhtmlContent = documentBuilder.parse(source);
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(FacesUtil.getRealPath("/SIMSUN.TTC"),
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocument(xhtmlContent, "");
			renderer.layout();
			FacesUtil.getHttpServletResponse().setHeader(
					"Content-Disposition",
					"attachment; filename=\""
							+ new String((fileName + ".pdf").getBytes("utf-8"),
									"iso8859-1") + "\"");
			FacesUtil.getHttpServletResponse().setContentType(
					"application/pdf;charset=UTF-8");
			sos = FacesUtil.getHttpServletResponse().getOutputStream();
			renderer.createPDF(sos);
			FacesUtil.getCurrentInstance().responseComplete();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(sos);
		}
	}

	/**
	 * 替换 借款合同正文中的信息
	 * 
	 * @param loanId
	 * @return
	 */
	public String dealLoanContractContent(String loanId) {

		Loan loan = ht.get(Loan.class, loanId);

		Element table = Jsoup
				.parseBodyFragment("<table border='1' style='margin: 0px auto; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width: 70%; '><tbody><tr class='firstRow'><td style='text-align:center;'>用户名</td><td style='text-align:center;'>投标来源</td><td style='text-align:center;'>借出金额</td><td style='text-align:center;'>借款期限</td><td style='text-align:center;'>应收利息</td></tr></tbody></table>");
		Element tbody = table.getElementsByTag("tbody").first();

		List<Invest> is = ht.find(
				"from Invest i where i.loan.id=? and i.status!=?",
				new String[] { loan.getId(), InvestStatus.CANCEL });
		Double sumMoney = 0.0; // XXX:修改等额本息借款0.01差错
		for (Invest invest : is) {
			tbody.append("<tr><td style='text-align:center;'>"
					+ invest.getUser().getUsername()
					+ "</td><td style='text-align:center;'>"
					+ (invest.getIsAutoInvest() ? "自动投标" : "手动投标")
					+ "</td><td style='text-align:center;'>"
					+ invest.getMoney()
					+ "</td><td style='text-align:center;'>"
					+ loan.getDeadline()
					* loan.getType().getRepayTimePeriod()
					+ "("
					+ dictUtil.getValue("repay_unit", loan.getType()
							.getRepayTimeUnit()) + ")"
					+ "</td><td style='text-align:center;'>"
					+ invest.getRepayRoadmap().getRepayInterest()
					+ "</td></tr>");
			sumMoney += invest.getMoney();
		}

		tbody.append("<tr><td style='text-align:center;'>总计</td><td></td><td style='text-align:center;'>"
				+ sumMoney
				+ "</td><td></td><td style='text-align:center;'>"
				+ loan.getRepayRoadmap().getRepayInterest() + "</td></tr>");

		Node node = ht.get(Node.class, loan.getContractType());
		if (contractContent == null) {
			/** caijinmin 201412231623 判断还款单元是天还是月，计算借款到期日 add begin **/
			Date endDate = null;
			if (RepayConstants.RepayUnit.DAY.equals(loan.getType()
					.getRepayTimeUnit())) {
				endDate = DateUtil.addDay(loan.getInterestBeginTime(),
						loan.getDeadline());
			} else if (RepayConstants.RepayUnit.MONTH.equals(loan.getType()
					.getRepayTimeUnit())) {
				endDate = DateUtil.addMonth(loan.getInterestBeginTime(),
						loan.getDeadline());
			}
			/** caijinmin 201412231623 add 判断还款单元是天还是月，计算借款到期日 end **/
			contractContent = node.getNodeBody().getBody();
			contractContent = contractContent
					.replace("#{loanId}", Strings.nullToEmpty(loan.getId()))
					.replace("#{actualMoney}", loan.getMoney().toString())
					.replace("#{investList}", table.outerHtml())
					.replace(
							"#{interest}",
							loan.getRepayRoadmap().getRepayInterest()
									.toString())
					.replace("#{loanerRealname}",
							Strings.nullToEmpty(loan.getUser().getRealname()))
					.replace("#{loanerIdCard}",
							Strings.nullToEmpty(loan.getUser().getIdCard()))
					.replace("#{loanerUsername}", loan.getUser().getUsername())

					.replace("#{guaranteeCompanyName}",
							Strings.nullToEmpty(loan.getGuaranteeCompanyName()))
					.replace(
							"#{giveMoneyDate}",
							Strings.nullToEmpty(DateUtil.DateToString(
									loan.getGiveMoneyTime(),
									DateStyle.YYYY_MM_DD_CN)))
					.replace("#{rate}", loan.getRatePercent().toString())
					.replace(
							"#{deadline}",
							String.valueOf(loan.getRepayRoadmap()
									.getRepayPeriod()))
					.replace(
							"#{interestBeginTime}",
							Strings.nullToEmpty(DateUtil.DateToString(
									loan.getInterestBeginTime(),
									DateStyle.YYYY_MM_DD_CN)))
					.replace(
							"#{interestEndTime}",
							// FIXME:需要根据借款类型，来计算借款到期日
							Strings.nullToEmpty(DateUtil.DateToString(endDate,
									DateStyle.YYYY_MM_DD_CN)))
					.replace("#{repayAllMoney}",
							loan.getRepayRoadmap().getRepayMoney().toString())
					// FIXME:需要根据借款类型，来显示还款日
					.replace(
							"#{repayDay}",
							String.valueOf(DateUtil.getDay(loan.getLoanRepays()
									.get(0).getRepayDay())))
					.replace("#{investTransferList}", "债权转让列表");
		}
		return contractContent;
	}
	/**
	 * 替换 借款合同正文中的信息
	 *
	 * @param loanId
	 * @return
	 */
	public String dealLoanContractContentNew(String loanId,String loginUserId,String contractType) {

		Loan loan = ht.get(Loan.class, loanId);
		// #{investList} 投资列表
		Element table = Jsoup
				.parseBodyFragment("<table border='1' style='margin: 0px auto; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width: 70%; '><tbody><tr class='firstRow'><td style='text-align:center;'>平台账号</td><td style='text-align:center;'>真实姓名</td><td style='text-align:center;'>身份证号</td><td style='text-align:center;'>出借金额</td><td style='text-align:center;'>借款期限</td><td style='text-align:center;'>投资确认日期</td></tr></tbody></table>");
		Element tbody = table.getElementsByTag("tbody").first();
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Invest> is = ht.find(
				"from Invest i where i.loan.id=? and i.status not in (?,?)",
				new String[] { loan.getId(), InvestStatus.CANCEL,InvestStatus.UNFINISHED });
		for (Invest invest : is) {
			//生成模版
			String rowsString = insertRow(6);
			//替换值
			String deadLine = loan.getDeadline() * loan.getType().getRepayTimePeriod()
							+ "(" + dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()) + ")";
			rowsString = rowsString.replace("#{0}",encryString(invest.getUser().getId(), "platformAccount", invest.getUser().getId(), loginUserId, contractType))
			.replace("#{1}", encryString(invest.getUser().getRealname(), "realName", invest.getUser().getId(), loginUserId, contractType))
			.replace("#{2}",encryString(invest.getUser().getIdCard(), "IdCard", invest.getUser().getId(), loginUserId, contractType))
			.replace("#{3}", String.valueOf(invest.getMoney()))
			.replace("#{4}",deadLine)
			.replace("#{5}", format.format(invest.getTime()));

			tbody.append(rowsString);
		}


		//获取代理人
		String agentId = loan.getAgent();//代理人平台账号
		String agentRealName = "";
		String agentIdCard = "";
		if (StringUtils.isNotEmpty(agentId)){
			User agent = ht.get(User.class, agentId);
			if(agent != null){
				agentRealName = agent.getRealname();
				agentIdCard = agent.getIdCard();
			}
		}
		//获取违约金收取比例
		FeeConfig feeConfig = ht.get(FeeConfig.class, "overdue_repay_investor");

		NumberFormat nt = NumberFormat.getPercentInstance();

		nt.setMinimumFractionDigits(2);

		Node node = ht.get(Node.class, loan.getContractType());

		if (contractContent == null) {

			Date endDate = null;
			if (RepayConstants.RepayUnit.DAY.equals(loan.getType()
					.getRepayTimeUnit())) {
				endDate = DateUtil.addDay(loan.getInterestBeginTime(),
						loan.getDeadline());
			} else if (RepayConstants.RepayUnit.MONTH.equals(loan.getType()
					.getRepayTimeUnit())) {
				endDate = DateUtil.addMonth(loan.getInterestBeginTime(),
						loan.getDeadline());
			}
			contractContent = node.getNodeBody().getBody();
			contractContent = contractContent
					.replace("#{loanId}", Strings.nullToEmpty(loan.getId()))
					.replace("#{actualMoney}", loan.getMoney().toString())
					.replace("#{investList}", table.outerHtml())
					.replace("#{loanerRealname}",
							Strings.nullToEmpty(loan.getUser().getRealname()))
					.replace("#{loanerId}",
							Strings.nullToEmpty(loan.getUser().getId()))
					.replace("#{loanerIdCard}",
							Strings.nullToEmpty(loan.getUser().getIdCard()))
					.replace("#{agentRealName}", Strings.nullToEmpty(agentRealName))
					.replace("#{agentId}", Strings.nullToEmpty(agentId))
					.replace("#{agentIdCard}", Strings.nullToEmpty(agentIdCard))
					.replace("#{loanPurpose}", Strings.nullToEmpty(loan.getLoanPurpose()))
					.replace("#{fen}", this.getDigitBySerialNo(loan.getMoney().doubleValue(), 0))
					.replace("#{bugle}", this.getDigitBySerialNo(loan.getMoney().doubleValue(), 1))
					.replace("#{yuan}", this.getDigitBySerialNo(loan.getMoney().doubleValue(), 2))
					.replace("#{ten}", this.getDigitBySerialNo(loan.getMoney().doubleValue(), 3))
					.replace("#{hundred}", this.getDigitBySerialNo(loan.getMoney().doubleValue(), 4))
					.replace("#{thousand}",this.getDigitBySerialNo(loan.getMoney().doubleValue(), 5))
					.replace("#{tenThousand}",this.getDigitBySerialNo(loan.getMoney().doubleValue(), 6))
					.replace("#{hundredThousand}",this.getDigitBySerialNo(loan.getMoney().doubleValue(), 7))
					.replace("#{overdue_repay_investor}",  nt.format(feeConfig.getFee()))
					.replace(
							"#{deadline}",
							String.valueOf(loan.getRepayRoadmap()
									.getRepayPeriod()))
					.replace(
							"#{interestBeginTime}",
							Strings.nullToEmpty(DateUtil.DateToString(
									loan.getInterestBeginTime(),
									DateStyle.YYYY_MM_DD_CN)))
					.replace(
							"#{interestEndTime}",
							// FIXME:需要根据借款类型，来计算借款到期日
							Strings.nullToEmpty(DateUtil.DateToString(endDate,
									DateStyle.YYYY_MM_DD_CN)))
							// FIXME:需要根据借款类型，来显示还款日
					.replace(
							"#{repayDay}",
							String.valueOf(DateUtil.getDay(loan.getLoanRepays()
									.get(0).getRepayDay())));
		}
		return contractContent;
	}

	public String insertRow(int tdNum){
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

	public  String encryString(String sourceString,String type,String investId,String loginUserId,String contractType){

		 String encryString = sourceString;

		 if ("invest".equals(contractType)){
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

	public String getDigitBySerialNo(double dou,int serialNo){
		String digit = "";
		java.text.DecimalFormat   df = new java.text.DecimalFormat("#.00");
		char[] digits =new StringBuffer(df.format(dou).replace(".", "")).reverse().toString().toCharArray();
		if(digits.length > serialNo){
			digit = String.valueOf(digits[serialNo]) ;
		}
		return  digit;
	}

	/**
	 * 替换债权借款合同正文中的信息
	 * 
	 * @param loanId
	 * @return
	 */
	@SuppressWarnings({ "unused", "static-access" })
	public String dealLoanContractTransferContent(String transferId) {
		// 获取债权信息
		TransferApply transferApply = ht.get(TransferApply.class, transferId);
		// 获取投资列表
		Invest invest = ht.get(Invest.class, transferApply.getInvest().getId());
		//
		List<Invest> is = ht.find(
				"from Invest i where i.transferApply.id=? and i.status!=?",
				new String[] { transferId, InvestStatus.CANCEL });
		if (is.size() > 1 || null == is) {
			return "合同信息有误!编号:" + transferId;
		}
		Invest investmom = is.get(0);
		// 获取项目列表
		Loan loan = ht.get(Loan.class, invest.getLoan().getId());
		// 获取合同模板
		Node node = ht.get(Node.class, loan.getTransferType());
		FeeConfig feeConfig = ht.get(FeeConfig.class, "transfer");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 替换合同模板中的内容
		if (contractContent == null) {
			contractContent = node.getNodeBody().getBody();
			contractContent = contractContent
			// 合同编号
					.replace("#{contractId}", Strings.nullToEmpty(loan.getId()))
					// 时间
					.replace("#{time}", transferApply.getDeadline().toString())
					// 转让人(甲方)
					.replace("#{realname_zr}", invest.getUser().getRealname())
					// 转让人身份证
					.replace("#{idCard_zr}", invest.getUser().getIdCard())
					// 转让人用户名
					.replace("#{username_zr}", invest.getUser().getUsername())
					// 购买人
					.replace("#{realname_gm}",
							investmom.getUser().getRealname())
					// 购买人身份证
					.replace("#{idCard_gm}", investmom.getUser().getIdCard())
					// 购买人用户名
					.replace("#{username_gm}",
							investmom.getUser().getUsername())

					// 项目编号
					.replace("#{loanId}", loan.getId())
					// 借款人姓名/名称
					.replace("#{realname_loan}", loan.getUser().getRealname())
					// 借款人证件类型及号码
					.replace("#{idCard_loan}", loan.getUser().getIdCard())
					// 借款人用户名
					.replace("#{username_loan}", loan.getUser().getUsername())
					// 借款本金总数额
					.replace("#{money_loan}", loan.getMoney().toString())

					// 本次转让的债权本金数额
					.replace("#{corpus}",
							String.valueOf(transferApply.getCorpus()))
					// 借款年利率
					.replace("#{rate_loan}", loan.getRatePercent().toString())
					// 借款用途
					.replace("#{purpose_loan}", loan.getLoanPurpose())
					// 原借款期限
					.replace("#{deadline_loan}", loan.getDeadline().toString())
					// 替换日期(月/天)
					.replace(
							"#{deadline_format}",
							dictUtil.getValue("repay_unit", loan.getType()
									.getRepayTimeUnit()))
					// 起始日期
					.replace("#{jk_begin}", loan.getCommitTime().toString())
					// 结束日期
					.replace("#{jk_end}", loan.getExpectTime().toString())
					// 计息方式
					.replace("#{repayType_loan}",
							loan.getType().getDescription())
					// 付息方式
					.replace("#{nextRepayDay}",
							simpleDateFormat.format(loan.getCommitTime()))
					// 还本方式
					.replace("#{repayDay}",
							simpleDateFormat.format(loan.getCommitTime()))
					// 转让价款
					.replace(
							"#{money_gm}",
							String.valueOf(transferApply.getCorpus()
									- transferApply.getPremium()))
					// 转让管理费
					.replace(
							"#{fee_gm}",
							String.valueOf(feeConfig.getFee()
									* (transferApply.getCorpus() - transferApply
											.getPremium())))
					// 转让日期
					.replace("#{time_gm}",
							transferApply.getApplyTime().toString());
		}
		return contractContent;
	}
}

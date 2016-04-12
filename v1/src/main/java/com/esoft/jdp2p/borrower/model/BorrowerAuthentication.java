package com.esoft.jdp2p.borrower.model;

// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.picture.model.AuthenticationMaterials;
import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.borrower.BorrowerConstant;

/**
 * 借款人认证资料
 */
@Entity
@Table(name = "borrower_authentication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerAuthentication implements java.io.Serializable {

	// Fields

	private String userId;
	private BorrowerInfo borrowerInfo;
	/** 审核人*/
	private User verifiedUser;
	/** 身份证扫描件*/
	private AuthenticationMaterials idCardScan;
	/** 手持身份证照片*/
	private AuthenticationMaterials idCardPhoto;
	/** 银行征信报告*/
	private AuthenticationMaterials bankCreditReport;
	/** 户口卡*/
	private AuthenticationMaterials huKouScan;
	/** 手持户口卡照片*/
	private AuthenticationMaterials huKouPhoto;
	/** 学历证书扫描件*/
	private AuthenticationMaterials diplomaScan;
	/** 收入证明*/
	private AuthenticationMaterials proofEarnings;
	/** 账户流水扫描件*/
	private AuthenticationMaterials accountFlow;
	/** 工作证件扫描件*/
	private AuthenticationMaterials workCertificate;
	/** 学生证*/
	private AuthenticationMaterials studentId;
	/** 职称证书*/
	private AuthenticationMaterials positionalTitles;
	/** 房产证明*/
	private AuthenticationMaterials houseInfo;
	/** 车辆证明*/
	private AuthenticationMaterials carInfo;
	/** 结婚证*/
	private AuthenticationMaterials marriageCertificate;
	/** 其他财产证明*/
	private AuthenticationMaterials otherEstate;
	/** 其他居住地证明*/
	private AuthenticationMaterials otherDomicile;
	/** 其他可确认身份的证件*/
	private AuthenticationMaterials otherIdCertificate;
	/** 其他能证明稳定收入的材料*/
	private AuthenticationMaterials otherIncomeInfo;
	/** 企业营业执照*/
	private AuthenticationMaterials businessLicense;
	/** 企业流水账户信息*/
	private AuthenticationMaterials businessAccountFlow;
	/** 微博认证*/
	private AuthenticationMaterials microblogInfo;
	/** 是否审核通过*/
	private String verified;
	/** 审核意见*/
	private String verifiedMessage;
	
	//审核时间
	private Date verifiedTime;

	// Constructors

	/** default constructor */
	public BorrowerAuthentication() {
	}

	/** minimal constructor */
	public BorrowerAuthentication(String userId, BorrowerInfo borrowerInfo) {
		this.userId = userId;
		this.borrowerInfo = borrowerInfo;
	}


	// Property accessors
	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public BorrowerInfo getBorrowerInfo() {
		return this.borrowerInfo;
	}
	
	@Column(name = "verified_time")
	public Date getVerifiedTime() {
		return verifiedTime;
	}

	public void setVerifiedTime(Date verifiedTime) {
		this.verifiedTime = verifiedTime;
	}

	public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
		this.borrowerInfo = borrowerInfo;
		if (borrowerInfo != null) {
			this.userId = borrowerInfo.getUserId();
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verified_user")
	public User getVerifiedUser() {
		return this.verifiedUser;
	}

	public void setVerifiedUser(User user) {
		this.verifiedUser = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_card_scan")
	public AuthenticationMaterials getIdCardScan() {
		return this.idCardScan;
	}

	public void setIdCardScan(AuthenticationMaterials idCardScan) {
		this.idCardScan = idCardScan;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_card_photo")
	public AuthenticationMaterials getIdCardPhoto() {
		return this.idCardPhoto;
	}

	public void setIdCardPhoto(AuthenticationMaterials idCardPhoto) {
		this.idCardPhoto = idCardPhoto;
	}
	
	@Transient
	public Boolean getIsPassedVerify(){
		if (StringUtils.equals(this.getVerified(), BorrowerConstant.Verify.passed)) {
			return true;
		}
		if (StringUtils.equals(this.getVerified(), BorrowerConstant.Verify.refuse)) {
			return false;
		}
		return null;
	}
	
	public void setIsPassedVerify(boolean isPassed){
		if (isPassed) {
			this.setVerified(BorrowerConstant.Verify.passed);
		} else {
			this.setVerified(BorrowerConstant.Verify.refuse);			
		}
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_credit_report")
	public AuthenticationMaterials getBankCreditReport() {
		return this.bankCreditReport;
	}

	public void setBankCreditReport(AuthenticationMaterials bankCreditReport) {
		this.bankCreditReport = bankCreditReport;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hu_kou_scan")
	public AuthenticationMaterials getHuKouScan() {
		return this.huKouScan;
	}

	public void setHuKouScan(AuthenticationMaterials huKouScan) {
		this.huKouScan = huKouScan;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hu_kou_photo")
	public AuthenticationMaterials getHuKouPhoto() {
		return this.huKouPhoto;
	}

	public void setHuKouPhoto(AuthenticationMaterials huKouPhoto) {
		this.huKouPhoto = huKouPhoto;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diploma_scan")
	public AuthenticationMaterials getDiplomaScan() {
		return this.diplomaScan;
	}

	public void setDiplomaScan(AuthenticationMaterials diplomaScan) {
		this.diplomaScan = diplomaScan;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proof_earnings")
	public AuthenticationMaterials getProofEarnings() {
		return this.proofEarnings;
	}

	public void setProofEarnings(AuthenticationMaterials proofEarnings) {
		this.proofEarnings = proofEarnings;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_flow")
	public AuthenticationMaterials getAccountFlow() {
		return this.accountFlow;
	}

	public void setAccountFlow(AuthenticationMaterials accountFlow) {
		this.accountFlow = accountFlow;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_certificate")
	public AuthenticationMaterials getWorkCertificate() {
		return this.workCertificate;
	}

	public void setWorkCertificate(AuthenticationMaterials workCertificate) {
		this.workCertificate = workCertificate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	public AuthenticationMaterials getStudentId() {
		return this.studentId;
	}

	public void setStudentId(AuthenticationMaterials studentId) {
		this.studentId = studentId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positional_titles")
	public AuthenticationMaterials getPositionalTitles() {
		return this.positionalTitles;
	}

	public void setPositionalTitles(AuthenticationMaterials positionalTitles) {
		this.positionalTitles = positionalTitles;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_info")
	public AuthenticationMaterials getHouseInfo() {
		return this.houseInfo;
	}

	public void setHouseInfo(AuthenticationMaterials houseInfo) {
		this.houseInfo = houseInfo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "car_info")
	public AuthenticationMaterials getCarInfo() {
		return this.carInfo;
	}

	public void setCarInfo(AuthenticationMaterials carInfo) {
		this.carInfo = carInfo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marriage_certificate")
	public AuthenticationMaterials getMarriageCertificate() {
		return this.marriageCertificate;
	}

	public void setMarriageCertificate(AuthenticationMaterials marriageCertificate) {
		this.marriageCertificate = marriageCertificate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "other_estate")
	public AuthenticationMaterials getOtherEstate() {
		return this.otherEstate;
	}

	public void setOtherEstate(AuthenticationMaterials otherEstate) {
		this.otherEstate = otherEstate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "other_domicile")
	public AuthenticationMaterials getOtherDomicile() {
		return this.otherDomicile;
	}

	public void setOtherDomicile(AuthenticationMaterials otherDomicile) {
		this.otherDomicile = otherDomicile;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "other_id_certificate")
	public AuthenticationMaterials getOtherIdCertificate() {
		return this.otherIdCertificate;
	}

	public void setOtherIdCertificate(AuthenticationMaterials otherIdCertificate) {
		this.otherIdCertificate = otherIdCertificate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "other_income_info")
	public AuthenticationMaterials getOtherIncomeInfo() {
		return this.otherIncomeInfo;
	}

	public void setOtherIncomeInfo(AuthenticationMaterials otherIncomeInfo) {
		this.otherIncomeInfo = otherIncomeInfo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_license")
	public AuthenticationMaterials getBusinessLicense() {
		return this.businessLicense;
	}

	public void setBusinessLicense(AuthenticationMaterials businessLicense) {
		this.businessLicense = businessLicense;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_account_flow")
	public AuthenticationMaterials getBusinessAccountFlow() {
		return this.businessAccountFlow;
	}

	public void setBusinessAccountFlow(AuthenticationMaterials businessAccountFlow) {
		this.businessAccountFlow = businessAccountFlow;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "microblog_info")
	public AuthenticationMaterials getMicroblogInfo() {
		return this.microblogInfo;
	}

	public void setMicroblogInfo(AuthenticationMaterials microblogInfo) {
		this.microblogInfo = microblogInfo;
	}

	
	@Column(name = "verified", length = 32)
	public String getVerified() {
		return this.verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	@Column(name = "verified_message", length = 1000)
	public String getVerifiedMessage() {
		return this.verifiedMessage;
	}

	public void setVerifiedMessage(String verifiedMessage) {
		this.verifiedMessage = verifiedMessage;
	}

}
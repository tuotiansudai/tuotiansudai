package com.esoft.jdp2p.borrower.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.Area;
import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.borrower.BorrowerConstant;

/**
 * 借款人一般信息
 */
@Entity
@Table(name = "borrower_personal_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerPersonalInfo implements java.io.Serializable {

	// Fields

	private String userId;
	private BorrowerInfo borrowerInfo;
	// 审核人
	private User verifiedUser;
	// 学历
	private String degree;
	// 毕业院校
	private String school;
	// 入学年份
	private String schoolYear;
	// 婚姻状况
	private String marriageInfo;
	// 是否有孩子
	private String childrenInfo;
	// 是否有房子
	private String hasHouse;
	// 是否房贷
	private String hasHousingLoan;
	// 是否有车
	private String hasCar;
	// 是否车贷
	private String hasCarLoan;
	
	/**
	 * 直属亲属
	 */
	private String direct;
	// 关系
	private String directRelative;
	// 电话
	private String directRelativePhone;
	/**
	 * 其他联系人
	 */
	private String other;
	private String otherContact;
	private String otherContactPhone;
	
	// 审核是否通过
	private String verified;
	// 审核信息
	private String verifiedMessage;
	// 审核时间
	private Date verifiedTime;

	// Constructors

	/** default constructor */
	public BorrowerPersonalInfo() {
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
	public BorrowerInfo getBorrowerInfo() {
		return this.borrowerInfo;
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

	@Column(name = "degree", length = 200)
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "school", length = 200)
	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	@Column(name = "school_year", length = 200)
	public String getSchoolYear() {
		return this.schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	@Column(name = "marriage_info", length = 200)
	public String getMarriageInfo() {
		return this.marriageInfo;
	}

	public void setMarriageInfo(String marriageInfo) {
		this.marriageInfo = marriageInfo;
	}

	@Column(name = "children_info", length = 200)
	public String getChildrenInfo() {
		return this.childrenInfo;
	}

	public void setChildrenInfo(String childrenInfo) {
		this.childrenInfo = childrenInfo;
	}

	@Column(name = "has_house", length = 200)
	public String getHasHouse() {
		return this.hasHouse;
	}

	public void setHasHouse(String hasHouse) {
		this.hasHouse = hasHouse;
	}

	@Column(name = "has_housing_loan", length = 200)
	public String getHasHousingLoan() {
		return this.hasHousingLoan;
	}

	public void setHasHousingLoan(String hasHousingLoan) {
		this.hasHousingLoan = hasHousingLoan;
	}

	@Column(name = "has_car", length = 200)
	public String getHasCar() {
		return this.hasCar;
	}

	public void setHasCar(String hasCar) {
		this.hasCar = hasCar;
	}

	@Column(name = "has_car_loan", length = 200)
	public String getHasCarLoan() {
		return this.hasCarLoan;
	}

	public void setHasCarLoan(String hasCarLoan) {
		this.hasCarLoan = hasCarLoan;
	}

	@Column(name = "verified", length = 32, nullable=false)
	public String getVerified() {
		return this.verified;
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

	public void setVerified(String verified) {
		this.verified = verified;
	}

	@Column(name = "verified_message", length = 500)
	public String getVerifiedMessage() {
		return this.verifiedMessage;
	}

	public void setVerifiedMessage(String verifiedMessage) {
		this.verifiedMessage = verifiedMessage;
	}

	@Column(name = "verified_time")
	public Date getVerifiedTime() {
		return verifiedTime;
	}

	public void setVerifiedTime(Date verifiedTime) {
		this.verifiedTime = verifiedTime;
	}
	
	@Column(name = "direct_relative", length = 100)
	public String getDirectRelative() {
		return this.directRelative;
	}

	public void setDirectRelative(String directRelative) {
		this.directRelative = directRelative;
	}

	@Column(name = "direct_relative_phone", length = 100)
	public String getDirectRelativePhone() {
		return this.directRelativePhone;
	}

	public void setDirectRelativePhone(String directRelativePhone) {
		this.directRelativePhone = directRelativePhone;
	}

	@Column(name = "other_contact", length = 100)
	public String getOtherContact() {
		return this.otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	@Column(name = "other_contact_phone", length = 100)
	public String getOtherContactPhone() {
		return this.otherContactPhone;
	}

	public void setOtherContactPhone(String otherContactPhone) {
		this.otherContactPhone = otherContactPhone;
	}
	
	@Column(name = "direct", length = 10)
	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	@Column(name = "other", length = 10)
	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	
	
}
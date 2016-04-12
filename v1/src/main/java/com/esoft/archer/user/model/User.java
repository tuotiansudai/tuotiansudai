package com.esoft.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User entity. 用户，包括系统管理员；暂定username和id为相同值
 */
@Entity
@Table(name = "user")
@NamedQueries({
		@NamedQuery(name = "User.findUserByUsername", query = "from User u where u.username = ?"),
		@NamedQuery(name = "User.findUserByEmail", query = "from User u where u.email = ?"),
		@NamedQuery(name = "User.findUserbymobileNumber", query = "from User u where u.mobileNumber = ?") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class User implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -4560631695514690692L;

	private String id;
	/*** 居住地 */
	private Area area;
	private String username;
	/**
	 * 照片
	 */
	private String photo;
	private String email;
	private String password;
	private String status;
	private String realname;
	private String sex;
	private String cashPassword;
	private Date birthday;
	private String homeAddress;
	private String qq;
	private String msn;
	private String mobileNumber;
	private String idCard;
	private String currentAddress;
	private String nickname;
	private String securityQuestion1;
	private String securityQuestion2;
	private String securityAnswer1;
	private String securityAnswer2;
	private Date lastLoginTime;
	private Date registerTime;
	private String comment;
	private List<Role> roles = new ArrayList<Role>(0);
	private Date disableTime;

	/** 绑定的登录ip，如果是多个，用英文逗号分隔。为空则不限制登录ip */
	private String bindIP;

	/**
	 * 用户等级
	 */
	private LevelForUser level;

	/** 推荐人 */
	private String referrer;


	private List<User> referrers;

	/**
	 * 用户登录失败次数
	 */
	private Integer loginFailedTimes;

	private String source;

	private String channel;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String id, String username, String password, Date registerTime) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.registerTime = registerTime;
	}

	public User(String userId) {
		this.id = userId;
	}

	// Property accessors
	@Id
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area")
	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Column(name = "username", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", nullable = false, length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "realname", length = 10)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "sex", length = 2)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "birthday", length = 10)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "home_address", length = 100)
	public String getHomeAddress() {
		return this.homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	@Column(name = "QQ", length = 12)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "MSN", length = 100)
	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Column(name = "mobile_number", length = 18)
	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Column(name = "id_card", length = 20)
	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "current_address", length = 100)
	public String getCurrentAddress() {
		return this.currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "security_question1", length = 100)
	public String getSecurityQuestion1() {
		return this.securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	@Column(name = "security_question2", length = 100)
	public String getSecurityQuestion2() {
		return this.securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	@Column(name = "security_answer1", length = 100)
	public String getSecurityAnswer1() {
		return this.securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	@Column(name = "security_answer2", length = 100)
	public String getSecurityAnswer2() {
		return this.securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

	@Column(name = "last_login_time", length = 19)
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "register_time", nullable = false, length = 19)
	public Date getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Lob
	@Column(name = "comment", columnDefinition = "CLOB")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return this.status;
	}

	@Column(name = "cash_password", length = 50)
	public String getCashPassword() {
		return this.cashPassword;
	}

	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Column(name = "photo", length = 500)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "disable_time")
	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level")
	public LevelForUser getLevel() {
		return level;
	}

	public void setLevel(LevelForUser level) {
		this.level = level;
	}

	@Column(name = "bind_ip", length = 1000)
	public String getBindIP() {
		return bindIP;
	}

	public void setBindIP(String bindIP) {
		this.bindIP = bindIP;
	}

	@Column(name = "referrer", length = 1000)
	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "referrer_relation",
			joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "referrer_id", nullable = false, updatable = false) })
	public List<User> getReferrers() {
		return referrers;
	}

	public void setReferrers(List<User> referrers) {
		this.referrers = referrers;
	}

	@Column(name = "login_failed_times")
	public Integer getLoginFailedTimes() {
		return loginFailedTimes;
	}

	public void setLoginFailedTimes(Integer loginFailedTimes) {
		this.loginFailedTimes = loginFailedTimes;
	}

	@Column(name = "source", length = 10)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	@Column(name = "channel", length = 32)
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
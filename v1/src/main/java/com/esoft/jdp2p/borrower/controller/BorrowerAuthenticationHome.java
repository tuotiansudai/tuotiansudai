package com.esoft.jdp2p.borrower.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.picture.PictureConstants;
import com.esoft.archer.picture.model.AutcMtrPicture;
import com.esoft.archer.picture.model.AutcMtrType;
import com.esoft.archer.picture.model.AuthenticationMaterials;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.ImageUploadUtil;
import com.esoft.jdp2p.borrower.BorrowerConstant;
import com.esoft.jdp2p.borrower.model.BorrowerAuthentication;
import com.esoft.jdp2p.borrower.service.BorrowerService;

@Component
@Scope(ScopeType.VIEW)
public class BorrowerAuthenticationHome extends EntityHome<BorrowerAuthentication> {

	private boolean ispass = false;
	private String verifyMessage;
	@Resource
	private BorrowerService borrowerService;
	
	@Resource
	private LoginUserInfo loginUser;

	public BorrowerAuthenticationHome() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String save() {
		//保存BorrowerAuthentication，借款人材料信息
		borrowerService.saveOrUpdateBorrowerAuthentication(getInstance());
		FacesUtil.addInfoMessage("保存成功，请等待管理员审核。");
		return "pretty:user_loan_applying-p2p";
	}

	@Override
	protected void initInstance() {
		try {
			this.setInstance(borrowerService.initBorrowerAuthentication(loginUser.getLoginUserId()));
		} catch (UserNotFoundException e) {
			// FIXME:处理
			e.printStackTrace();
		}
	}

	//FIXME:这些上传和清除方法，肿么办？
	
	@Transactional(readOnly = false)
	private AuthenticationMaterials initAutcMtr(String autcMtrTypeId) {
		AuthenticationMaterials autcMtr = new AuthenticationMaterials();
		autcMtr.setId(IdGenerator.randomUUID());
		autcMtr.setType(new AutcMtrType(autcMtrTypeId));
		return getBaseService().merge(autcMtr);
	}

	private void handleAutcMtrUpload(AuthenticationMaterials mutcMtr,
			FileUploadEvent event) {
		// 判断最大数量
		if (mutcMtr.getPictures().size() < mutcMtr.getType().getMaxNumber()) {
			// 保存上传图片
			String picturePath = fileUpload(event);
			AutcMtrPicture amPic = new AutcMtrPicture();
			amPic.setId(IdGenerator.randomUUID());
			amPic.setAutcMtr(mutcMtr);
			amPic.setPicture(picturePath);
			amPic.setSeqNum(mutcMtr.getPictures().size() + 1);
			mutcMtr.getPictures().add(amPic);
			getBaseService().update(mutcMtr);
			getBaseService().merge(getInstance());
		} else {
			// 超出允许图片数量的上限。
			FacesUtil.addErrorMessage("上传失败，最多允许上传"
					+ mutcMtr.getType().getMaxNumber() + "张图片。");
		}
	}

	/**
	 * 清除某一项的所有图片
	 * 
	 * @param mutcMtr
	 */
	@Transactional(readOnly = false)
	private void handleAutcMtrClean(AuthenticationMaterials mutcMtr) {
		if (mutcMtr != null) {
			getBaseService().delete(mutcMtr);
		}
	}

	
	/**
	 * 上传身份证扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void idCardScanUpload(FileUploadEvent event) {
		if (this.getInstance().getIdCardScan() == null) {
			this.getInstance().setIdCardScan(
					initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_SCAN));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setIdCardScan(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getIdCardScan().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getIdCardScan();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除身份证扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void idCardScanClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getIdCardScan();
		this.getInstance().setIdCardScan(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	/**
	 * 上传手持身份证照片
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void idCardPhotoUpload(FileUploadEvent event) {
		if (this.getInstance().getIdCardPhoto() == null) {
			this.getInstance().setIdCardPhoto(
					initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_PHOTO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setIdCardPhoto(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getIdCardPhoto().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getIdCardPhoto();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除手持身份证照片
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void idCardPhotoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getIdCardPhoto();
		this.getInstance().setIdCardPhoto(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传银行征信报告
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void bankCreditReportUpload(FileUploadEvent event) {
		if (this.getInstance().getBankCreditReport() == null) {
			this.getInstance().setBankCreditReport(
					initAutcMtr(PictureConstants.AutcMtrType.ID_BANK_CREDITREPORT));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setBankCreditReport(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getBankCreditReport().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getBankCreditReport();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除银行征信报告
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void bankCreditReportClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getBankCreditReport();
		this.getInstance().setBankCreditReport(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传户口卡
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void huKouScanUpload(FileUploadEvent event) {
		if (this.getInstance().getHuKouScan() == null) {
			this.getInstance().setHuKouScan(
					initAutcMtr(PictureConstants.AutcMtrType.ID_HUKOU_SCAN));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setHuKouScan(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getHuKouScan().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getHuKouScan();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除户口卡
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void huKouScanClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getHuKouScan();
		this.getInstance().setHuKouScan(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}
	

	
	/**
	 * 上传手持户口卡照片
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void huKouPhotoUpload(FileUploadEvent event) {
		if (this.getInstance().getHuKouPhoto() == null) {
			this.getInstance().setHuKouPhoto(
					initAutcMtr(PictureConstants.AutcMtrType.ID_HUKOU_PHOTO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setHuKouPhoto(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getHuKouPhoto().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getHuKouPhoto();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除手持户口卡照片
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void huKouPhotoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getHuKouPhoto();
		this.getInstance().setHuKouPhoto(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传学历证书扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void diplomaScanUpload(FileUploadEvent event) {
		if (this.getInstance().getDiplomaScan() == null) {
			this.getInstance().setDiplomaScan(
					initAutcMtr(PictureConstants.AutcMtrType.ID_DIPLOMA_SCAN));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setDiplomaScan(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getDiplomaScan().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getDiplomaScan();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除学历证书扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void diplomaScanClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getDiplomaScan();
		this.getInstance().setDiplomaScan(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传收入证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void proofEarningsUpload(FileUploadEvent event) {
		if (this.getInstance().getProofEarnings() == null) {
			this.getInstance().setProofEarnings(
					initAutcMtr(PictureConstants.AutcMtrType.ID_PROOF_EARNINGS));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setProofEarnings(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getProofEarnings().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getProofEarnings();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除收入证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void proofEarningsClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getProofEarnings();
		this.getInstance().setProofEarnings(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传账户流水扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void accountFlowUpload(FileUploadEvent event) {
		if (this.getInstance().getAccountFlow() == null) {
			this.getInstance().setAccountFlow(
					initAutcMtr(PictureConstants.AutcMtrType.ID_ACCOUNT_FLOW));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setAccountFlow(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getAccountFlow().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getAccountFlow();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除账户流水扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void accountFlowClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getAccountFlow();
		this.getInstance().setAccountFlow(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传工作证件扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void workCertificateUpload(FileUploadEvent event) {
		if (this.getInstance().getWorkCertificate() == null) {
			this.getInstance().setWorkCertificate(
					initAutcMtr(PictureConstants.AutcMtrType.ID_WORK_CERTIFICATE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setWorkCertificate(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getWorkCertificate().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getWorkCertificate();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除工作证件扫描件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void workCertificateClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getWorkCertificate();
		this.getInstance().setWorkCertificate(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传学生证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void studentIdUpload(FileUploadEvent event) {
		if (this.getInstance().getStudentId() == null) {
			this.getInstance().setStudentId(
					initAutcMtr(PictureConstants.AutcMtrType.ID_STUDENT_ID));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setStudentId(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getStudentId().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getStudentId();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除学生证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void studentIdClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getStudentId();
		this.getInstance().setStudentId(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	 
	/**
	 * 上传职称证书
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void positionalTitlesUpload(FileUploadEvent event) {
		if (this.getInstance().getPositionalTitles() == null) {
			this.getInstance().setPositionalTitles(
					initAutcMtr(PictureConstants.AutcMtrType.ID_POSITIONAL_TITLES));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setPositionalTitles(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getPositionalTitles().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getPositionalTitles();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除职称证书
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void positionalTitlesClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getPositionalTitles();
		this.getInstance().setPositionalTitles(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	 
	
	/**
	 * 上传房产证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void houseInfoUpload(FileUploadEvent event) {
		if (this.getInstance().getHouseInfo() == null) {
			this.getInstance().setHouseInfo(
					initAutcMtr(PictureConstants.AutcMtrType.ID_HOUSE_INFO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setHouseInfo(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getHouseInfo().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getHouseInfo();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除房产证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void houseInfoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getHouseInfo();
		this.getInstance().setHouseInfo(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传车辆证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void carInfoUpload(FileUploadEvent event) {
		if (this.getInstance().getCarInfo() == null) {
			this.getInstance().setCarInfo(
					initAutcMtr(PictureConstants.AutcMtrType.ID_CAR_INFO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setCarInfo(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getCarInfo().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getCarInfo();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除车辆证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void carInfoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getCarInfo();
		this.getInstance().setCarInfo(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}


	/**
	 * 上传结婚证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void marriageCertificateUpload(FileUploadEvent event) {
		if (this.getInstance().getMarriageCertificate() == null) {
			this.getInstance().setMarriageCertificate(
					initAutcMtr(PictureConstants.AutcMtrType.ID_MARRIAGE_CERTIFICATE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setMarriageCertificate(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getMarriageCertificate().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getMarriageCertificate();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除结婚证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void marriageCertificateClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getMarriageCertificate();
		this.getInstance().setMarriageCertificate(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传其他财产证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherEstateUpload(FileUploadEvent event) {
		if (this.getInstance().getOtherEstate() == null) {
			this.getInstance().setOtherEstate(
					initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_ESTATE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setOtherEstate(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getOtherEstate().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherEstate();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除其他财产证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherEstateClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherEstate();
		this.getInstance().setOtherEstate(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传其他居住地证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherDomicileUpload(FileUploadEvent event) {
		if (this.getInstance().getOtherDomicile() == null) {
			this.getInstance().setOtherDomicile(
					initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_DOMICILE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setOtherDomicile(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getOtherDomicile().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherDomicile();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除其他居住地证明
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherDomicileClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherDomicile();
		this.getInstance().setOtherDomicile(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传其他可确认身份的证件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherIdCertificateUpload(FileUploadEvent event) {
		if (this.getInstance().getOtherIdCertificate() == null) {
			this.getInstance().setOtherIdCertificate(
					initAutcMtr(PictureConstants.AutcMtrType.ID_OTHERID_CERTIFICATE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setOtherIdCertificate(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getOtherIdCertificate().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherIdCertificate();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除其他可确认身份的证件
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherIdCertificateClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherIdCertificate();
		this.getInstance().setOtherIdCertificate(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传其他能证明稳定收入的材料
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherIncomeInfoUpload(FileUploadEvent event) {
		if (this.getInstance().getOtherIncomeInfo() == null) {
			this.getInstance().setOtherIncomeInfo(
					initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_INCOMEINFO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setOtherIncomeInfo(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getOtherIncomeInfo().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherIncomeInfo();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除其他能证明稳定收入的材料
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void otherIncomeInfoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getOtherIncomeInfo();
		this.getInstance().setOtherIncomeInfo(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传企业营业执照
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void businessLicenseUpload(FileUploadEvent event) {
		if (this.getInstance().getBusinessLicense() == null) {
			this.getInstance().setBusinessLicense(
					initAutcMtr(PictureConstants.AutcMtrType.ID_BUSINESS_LICENSE));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setBusinessLicense(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getBusinessLicense().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getBusinessLicense();
		handleAutcMtrUpload(mutcMtr, event);
	}
	
	/**
	 * 清除企业营业执照
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void businessLicenseClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getBusinessLicense();
		this.getInstance().setBusinessLicense(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	 
	/**
	 * 上传企业流水账户信息
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void businessAccountFlowUpload(FileUploadEvent event) {
		if (this.getInstance().getBusinessAccountFlow() == null) {
			this.getInstance().setBusinessAccountFlow(
					initAutcMtr(PictureConstants.AutcMtrType.ID_BUSINESS_ACCOUNTFLOW));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setBusinessAccountFlow(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getBusinessAccountFlow().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getBusinessAccountFlow();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除企业流水账户信息
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void businessAccountFlowClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getBusinessAccountFlow();
		this.getInstance().setBusinessAccountFlow(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	
	/**
	 * 上传微博认证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void microblogInfoUpload(FileUploadEvent event) {
		if (this.getInstance().getMicroblogInfo() == null) {
			this.getInstance().setMicroblogInfo(
					initAutcMtr(PictureConstants.AutcMtrType.ID_MICROBLOG_INFO));
		} else {
			// 延迟加载，session关闭了，所以。。。
			this.getInstance().setMicroblogInfo(
					getBaseService().get(AuthenticationMaterials.class,
							this.getInstance().getMicroblogInfo().getId()));
		}
		AuthenticationMaterials mutcMtr = this.getInstance().getMicroblogInfo();
		handleAutcMtrUpload(mutcMtr, event);
	}
	/**
	 * 清除微博认证
	 * @param event
	 */
	@Transactional(readOnly = false)
	public void microblogInfoClean() {
		AuthenticationMaterials mutcMtr = this.getInstance().getMicroblogInfo();
		this.getInstance().setMicroblogInfo(null);
		handleAutcMtrClean(mutcMtr);
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("清除成功！");
	}

	/**
	 * 上传文件
	 * @param event
	 */
	private String fileUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		InputStream is = null;
		try {
			is = file.getInputstream();
			FacesUtil.addInfoMessage("上传成功！");
			return ImageUploadUtil.upload(is, file.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("上传失败！");
			return null;
		}
	}

	/**
	 * 审核借款用户认证资料
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public String verify(BorrowerAuthentication ba) {
		borrowerService.verifyBorrowerAuthentication(ba.getUserId(), ispass, ba.getVerifiedMessage(), loginUser.getLoginUserId());
		FacesUtil.addInfoMessage("保存成功");
		return FacesUtil.redirect("/admin/verify/verifyLoanerList");
	}

	public void initVerify(BorrowerAuthentication borrowerAuthentication) {
		this.setInstance(borrowerAuthentication);
		if ((BorrowerConstant.Verify.passed).equals(this.getInstance().getVerified())) {
			ispass = true;
		}
	}

	public boolean getIspass() {
		return ispass;
	}

	public void setIspass(boolean ispass) {
		this.ispass = ispass;
	}

	public String getVerifyMessage() {
		return verifyMessage;
	}

	public void setVerifyMessage(String verifyMessage) {
		this.verifyMessage = verifyMessage;
	}

}

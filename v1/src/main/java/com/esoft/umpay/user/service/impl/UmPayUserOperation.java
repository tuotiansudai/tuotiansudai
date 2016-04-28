package com.esoft.umpay.user.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 开户
 * 
 * @author zt
 * 
 */
@Service("umPayUserOperation")
public class UmPayUserOperation extends UmPayOperationServiceAbs<User> {
	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	HibernateTemplate ht;

	@Resource
	UserService userService;

	@Logger
	static Log log;

	/**
	 * 开户-直连接口
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public TrusteeshipOperation createOperation(User user, FacesContext fc)
			throws IOException {
		Map<String, String> map = UmPaySignUtil
				.getSendMapDate(UmPayConstants.OperationType.MER_REGISTER_PERSON);
		map.put("mer_cust_id", user.getId());
		map.put("mer_cust_name", user.getRealname());
		map.put("identity_type", "IDENTITY_CARD");
		map.put("identity_code", user.getIdCard());
		map.put("mobile_id", user.getMobileNumber());
		ReqData reqData;
		Map<String, String> resData = null;
		try {
			reqData = Mer2Plat_v40.makeReqDataByGet(map);
			log.debug(reqData);
			// 保存操作记录
			TrusteeshipOperation to = createTrusteeshipOperation(user.getId(),
					reqData.getUrl(), user.getId(),
					UmPayConstants.OperationType.MER_REGISTER_PERSON,
					reqData.getPlain());
			// 创建一个get直连连接
			String responseBodyAsString = HttpClientUtil
					.getResponseBodyAsString(reqData.getUrl());
			// 获取返回数据code为0000则开户成功
			resData = Plat2Mer_v40.getResData(responseBodyAsString);
			if ("0000".equals(resData.get("ret_code"))) {
				log.debug(user.getId() + "开户成功信息：" + resData);
				// 修改操作记录
				accountSuccess(to, resData);
				// 保存开户信息
				addTrusteeshipAccount(user, resData);
				// 刷新本地登陆权限
				userService.realNameCertification(user);
				return null;
			} else {
				log.debug(user.getId() + "开户失败原因：" + resData);
				throw new UmPayOperationException("开户失败:"
						+ resData.get("ret_msg"));
			}
		} catch (ReqDataException | RetDataException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
	}

	/**
	 * 处理开户成功
	 * 
	 * @param user
	 */
	@Transactional(rollbackFor = Exception.class)
	public void accountSuccess(TrusteeshipOperation to,
			Map<String, String> resData) {
		to.setResponseData(resData.toString());
		to.setResponseTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.PASSED);
		ht.update(to);
	}

	/**
	 * 往托管表里面插入数据
	 * 
	 * @param args
	 *            每次都用不到account_id而且一直在用loan_user_id,为了简化代码以后可以直接将这个字段替换回去
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addTrusteeshipAccount(User user, Map<String, String> resData) {
		TrusteeshipAccount ta = ht.get(TrusteeshipAccount.class,
				resData.get("user_id").toString());
		if (ta == null) {
			ta = new TrusteeshipAccount();
			ta.setId(resData.get("user_id").toString());
			ta.setUser(user);
		}
		ta.setAccountId(resData.get("account_id").toString());
		ta.setCreateTime(new Date());
		ta.setTrusteeship(UmPayConstants.OperationType.UMPAY);
		ta.setStatus(TrusteeshipConstants.Status.PASSED);
		ht.saveOrUpdate(ta);
	}
}

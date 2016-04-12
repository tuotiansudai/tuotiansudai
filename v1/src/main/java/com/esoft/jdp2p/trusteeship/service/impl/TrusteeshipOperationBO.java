package com.esoft.jdp2p.trusteeship.service.impl;

import com.esoft.core.util.HtmlElementUtil;
import com.esoft.core.util.MapUtil;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.umpay.trusteeship.UmPayConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 资金托管，关联操作 BO
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-4 上午10:12:42
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-4 wangzhi 1.0
 */
@Service("trusteeshipOperationBO")
public class TrusteeshipOperationBO {

	@Resource
	HibernateTemplate ht;

	/**
	 * 获取minute分钟之前发出的且到目前为止尚未收到回调的所有请求
	 * 
	 * @param minute
	 *            请求距离目前的时间间隔（分钟）
	 * @return
	 */
	public List<TrusteeshipOperation> getUnCallbackOperations(int minute) {
		Date date = DateUtils.addMinutes(new Date(), -minute);
		String hql = "from TrusteeshipOperation to where to.status=? and to.requestTime<?";
		return ht.find(hql, new Object[] { TrusteeshipConstants.Status.SENDED,
				date });
	}

	/**
	 * 操作类型（type） 操作的唯一标识（markId） 托管方(trusteeship) 上述三者一致，则认为是同一条数据。
	 */
	public void save(TrusteeshipOperation to) {
		String sqlTemplate = "delete from trusteeship_operation where type = ''{0}'' and mark_id = ''{1}'' and trusteeship = ''{2}''";
		if (StringUtils.isEmpty(to.getType())) {
			throw new IllegalArgumentException(
					"trusteshipOperation.type can not be empty!");
		}
		if (StringUtils.isEmpty(to.getMarkId())) {
			throw new IllegalArgumentException(
					"trusteshipOperation.markId can not be empty!");
		}
		if (StringUtils.isEmpty(to.getTrusteeship())) {
			throw new IllegalArgumentException(
					"trusteshipOperation.trusteeship can not be empty!");
		}

		ht.getSessionFactory().getCurrentSession().createSQLQuery(MessageFormat.format(sqlTemplate, to.getType(), to.getMarkId(), to.getTrusteeship()));
		ht.save(to);
	}

	/**
	 * 根据 操作类型（type） 操作的唯一标识（markId） 托管方(trusteeship)，查询，数据必须唯一。
	 * 联合主键：操作类型+操作唯一标识+托管方
	 * 
	 * @param type
	 *            操作类型
	 * @param markId
	 *            操作的唯一标识
	 * @param operator
	 *            操作者
	 * @param trusteeship
	 *            托管方
	 * 
	 * @return 操作对象
	 */
	public TrusteeshipOperation get(String type, String markId,
			String trusteeship) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TrusteeshipOperation.class);
		if (type == null) {
			criteria.add(Restrictions.isNull("type"));
		} else {
			criteria.add(Restrictions.eq("type", type));
		}
		if (markId == null) {
			criteria.add(Restrictions.isNull("markId"));
		} else {
			criteria.add(Restrictions.eq("markId", markId));
		}
		if (trusteeship == null) {
			criteria.add(Restrictions.isNull("trusteeship"));
		} else {
			criteria.add(Restrictions.eq("trusteeship", trusteeship));
		}
		List<TrusteeshipOperation> tos = ht.findByCriteria(criteria);

		if (tos.size() > 1) {
			// 找到多个，抛异常。
			throw new DuplicateKeyException("type:" + type + " markId:"
					+ markId + " trusteeship:" + trusteeship + "  duplication!");
		}
		if (tos.size() == 0) {
			return null;
		}
		TrusteeshipOperation to = tos.get(0);
		return to;
	}

	/**
	 * 根据 操作类型（type） 操作的唯一标识（markId） 操作者（operator） 托管方(trusteeship)，查询，数据必须唯一。
	 * 联合主键：操作类型+操作唯一标识+托管方
	 * 
	 * @param type
	 *            操作类型
	 * @param markId
	 *            操作的唯一标识
	 * @param operator
	 *            操作者
	 * @param trusteeship
	 *            托管方
	 * 
	 * 
	 * @return 操作对象
	 * 
	 * @deprecated
	 * @see get(String type, String markId, String trusteeship)
	 */
	@Deprecated
	public TrusteeshipOperation get(String type, String markId,
			String operator, String trusteeship) throws DuplicateKeyException{
		DetachedCriteria criteria = DetachedCriteria
				.forClass(TrusteeshipOperation.class);
		if (type == null) {
			criteria.add(Restrictions.isNull("type"));
		} else {
			criteria.add(Restrictions.eq("type", type));
		}
		if (markId == null) {
			criteria.add(Restrictions.isNull("markId"));
		} else {
			criteria.add(Restrictions.eq("markId", markId));
		}
		if (operator == null) {
			criteria.add(Restrictions.isNull("operator"));
		} else {
			criteria.add(Restrictions.eq("operator", operator));
		}
		if (trusteeship == null) {
			criteria.add(Restrictions.isNull("trusteeship"));
		} else {
			criteria.add(Restrictions.eq("trusteeship", trusteeship));
		}
		List<TrusteeshipOperation> tos = ht.findByCriteria(criteria);

		if (tos.size() > 1 && !type.equals(UmPayConstants.OperationType.PTP_MER_BIND_AGREEMENT)) {
			throw new DuplicateKeyException("type:" + type + " markId:"
					+ markId + " operator:" + operator + " trusteeship:"
					+ trusteeship + "  duplication!");
		}
		if (tos.size() == 0) {
			return null;
		}
		TrusteeshipOperation to = tos.get(0);
		return to;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void sendOperation(String trusteeshipOperationId, FacesContext fc)
			throws IOException {
		TrusteeshipOperation to = ht.get(TrusteeshipOperation.class,
				trusteeshipOperationId);
		// FIXME:验证，判空
		Map<String, String> params = MapUtil.stringToHashMap(to
				.getRequestData());
		String content = HtmlElementUtil.createAutoSubmitForm(params,
				to.getRequestUrl(), "utf-8");
		ExternalContext ec = fc.getExternalContext();
		ec.responseReset();
		ec.setResponseCharacterEncoding("utf-8");
		ec.setResponseContentType("text/html");
		ec.getResponseOutputWriter().write(content);
		fc.responseComplete();

		to.setRequestTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.SENDED);
		ht.update(to);
	}
}

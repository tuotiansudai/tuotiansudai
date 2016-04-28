package com.esoft.jdp2p.risk.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.risk.FeeConfigConstants.OperateMode;
import com.esoft.jdp2p.risk.exception.NotContinuityIntervalException;
import com.esoft.jdp2p.risk.model.FeeConfig;
import com.google.common.primitives.Doubles;

@Service(value = "FeeConfigBO")
public class FeeConfigBO {

	@Resource
	private HibernateTemplate ht;

	/**
	 * key：节点、费用类型、影响因子、影响因子值、是否为区间 如果不是区间，则上述key为联合主键； 如果是区间，则key+区间上限下限为联合主键
	 * 
	 * @param feePoint
	 *            费用节点
	 * @param feeType
	 *            费用类型
	 * @param factor
	 *            影响因子
	 * @param factorValue
	 *            影响因子的值
	 * @param value
	 *            返回该值的费用
	 * @return
	 * @since 2.0
	 */
	public double getFee(String feePoint, String feeType, String factor,
			String factorValue, double value) {
		DetachedCriteria criteria = createDetachedCriteriaByCompositeKeys(
				feePoint, feeType, factor, factorValue);
		// value大于等于下限，小于上限
		criteria.add(Restrictions.and(
				Restrictions.gt("intervalUpperLimit", value),
				Restrictions.le("intervalLowerLimit", value)));
		List<FeeConfig> fcs = ht.findByCriteria(criteria);

		if (fcs.size() > 1) {
			// 找到多个，抛异常。
			throw new DuplicateKeyException("feePoint:" + feePoint
					+ " feeType:" + feeType + " factor:" + factor
					+ " factorValue:" + factorValue + " value:" + value
					+ "  duplication!");
		}
		if (fcs.size() == 0) {
			throw new ObjectNotFoundException(FeeConfig.class, "feePoint:"
					+ feePoint + " feeType:" + feeType + " factor:" + factor
					+ " factorValue:" + factorValue + " value:" + value
					+ "  not found!");
		}
		FeeConfig fc = fcs.get(0);
		if (fc.getOperateMode().equals(OperateMode.FIXED)) {
			return fc.getFee();
		} else if (fc.getOperateMode().equals(OperateMode.RATE)) {
			double fee = ArithUtil.round(ArithUtil.mul(value, fc.getFee()), 2);
			return Doubles
					.min(fee,
							fc.getFeeUpperLimit() == null ? fee : fc
									.getFeeUpperLimit());
		} else {
			throw new RuntimeException("operateMode: " + fc.getOperateMode()
					+ "not found!");
		}
	}

	/**
	 * 通过四个业务上的联合主键，创建DetachedCriteria查询
	 * 
	 * @param feePoint
	 * @param feeType
	 * @param factor
	 * @param factorValue
	 * @return
	 */
	private DetachedCriteria createDetachedCriteriaByCompositeKeys(
			String feePoint, String feeType, String factor, String factorValue) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FeeConfig.class);
		criteria.add(Restrictions.eq("feePoint", feePoint));
		criteria.add(Restrictions.eq("feeType", feeType));
		if (factor == null) {
			criteria.add(Restrictions.isNull("factor"));
			criteria.add(Restrictions.isNull("factorValue"));
		} else {
			criteria.add(Restrictions.eq("factor", factor));
			criteria.add(Restrictions.eq("factorValue", factorValue));
		}
		return criteria;
	}

	/**
	 * key：节点、费用类型、影响因子、影响因子值 , key+区间上限下限为联合主键 根据key进行查询，有则修改，无则增加。
	 * 在新增或者改变时候，需判断区间是否连续
	 * 
	 * @param fc
	 *            需要保存或者更新的费用规则（FeeConfig）对象
	 * @throws NotContinuityIntervalException
	 *             区间不连续
	 * 
	 * @since 2.0
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveOrUpdate(FeeConfig fc)
			throws NotContinuityIntervalException {
		DetachedCriteria criteria = createDetachedCriteriaByCompositeKeys(
				fc.getFeePoint(), fc.getFeeType(), fc.getFactor(),
				fc.getFactorValue());
		List<FeeConfig> fcs = ht.findByCriteria(criteria);
		// 判断是新增还是修改，如果是修改，则需要找到并移除被修改的对象
		remove(fc.getId(), fcs);

		if (fcs.size() >= 1) {
			// 把已有的和要新增的FeeConfig，放到一个list中，根据区间下限排序，然后判断是否连续
			fcs.add(fc);
			sortByIntervalLowerLimitAsc(fcs);
			// 判断连续：第一个的积分上限==第二个的积分下限
			isContinuityInterival(fcs);
		}

		// 保存新对象
		ht.saveOrUpdate(fc);
	}

	/**
	 * 判断集合中的费率配置区间，是否连续：第一个的积分上限==第二个的积分下限
	 * 
	 * @param fcs
	 *            被判断的费率配置
	 * @throws NotContinuityIntervalException
	 *             区间不连续
	 */
	private void isContinuityInterival(List<FeeConfig> fcs)
			throws NotContinuityIntervalException {
		for (int i = 0; i <= fcs.size() - 2; i++) {
			if (fcs.get(i).getIntervalUpperLimit() != fcs.get(i + 1)
					.getIntervalLowerLimit()) {
				// 不连续，则抛异常
				throw new NotContinuityIntervalException();
			}
		}
	}

	/**
	 * 根据id，从一个集合中移除指定FeeConfig
	 * 
	 * @param fcId
	 *            被移除的对象id
	 * @param fcs
	 *            集合
	 * @return 如果找到并移除，则返回true，反之返回false
	 */
	private boolean remove(String fcId, List<FeeConfig> fcs) {
		for (Iterator iterator = fcs.iterator(); iterator.hasNext();) {
			FeeConfig fcTmp = (FeeConfig) iterator.next();
			if (fcTmp.getId().equals(fcId)) {
				// 修改
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除费率配置，需判断删除后，区间是否连续
	 * 
	 * @param fcId
	 *            需要被删除的费率规则（FeeConfig）id
	 * @throws NoMatchingObjectsException
	 *             对象未找到
	 * @throws NotContinuityIntervalException
	 *             删除后区间不连续
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void delete(String fcId) throws NoMatchingObjectsException,
			NotContinuityIntervalException {
		FeeConfig fc = ht.get(FeeConfig.class, fcId);
		if (fc == null) {
			throw new NoMatchingObjectsException(FeeConfig.class, fcId);
		}
		DetachedCriteria criteria = createDetachedCriteriaByCompositeKeys(
				fc.getFeePoint(), fc.getFeeType(), fc.getFactor(),
				fc.getFactorValue());
		List<FeeConfig> fcs = ht.findByCriteria(criteria);
		// 并移除需要删除的对象
		remove(fc.getId(), fcs);

		if (fcs.size() >= 2) {
			// 判断是否连续
			isContinuityInterival(fcs);
		}

		// 删除
		ht.delete(fc);
	}

	/**
	 * 根据区间下限，正序排列
	 * 
	 * @param fcs
	 */
	public void sortByIntervalLowerLimitAsc(List<FeeConfig> fcs) {
		Collections.sort(fcs, new Comparator<FeeConfig>() {
			@Override
			public int compare(FeeConfig o1, FeeConfig o2) {
				double result = o1.getIntervalLowerLimit()
						- o2.getIntervalLowerLimit();
				if (result > 0) {
					return 1;
				} else if (result < 0) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}

}

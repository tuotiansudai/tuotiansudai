package com.esoft.archer.user.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.LevelForUser;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserLevelHistory;
import com.esoft.archer.user.service.UserLevelService;
import com.esoft.archer.user.service.UserPointService;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;

@Service(value = "userLevelService")
public class UserLevelServiceImpl implements UserLevelService {

	@Resource
	private HibernateTemplate ht;
	
	@Resource
	private UserPointService userPointService;

	@Override
	public void change(String userId, String levelId, int validityPeriod,
			String description) {
		// 修改用户等级，保存用户
		User user = ht.get(User.class, userId);
		LevelForUser lfu = ht.get(LevelForUser.class, levelId);
		user.setLevel(lfu);
		ht.update(user);
		
		// 在等级历史（userLevelHistory）中，添加记录
		UserLevelHistory ulh = new UserLevelHistory();
		ulh.setDescription(description);
		ulh.setGrantTime(new Date());
		ulh.setExpirationTime(DateUtil.addSecond(ulh.getGrantTime(), validityPeriod));
		ulh.setId(IdGenerator.randomUUID());
		ulh.setLevelForUser(lfu);
		ulh.setUser(user);
		ulh.setValidityPeriod(validityPeriod);
		ht.save(ulh);
	}
	
	@Override
	public void refreshUserLevel(String userId){
		User user = ht.get(User.class, userId);
		//获取当前用户积分
		final int point = userPointService.getPointsByUserId(user.getId(), UserConstants.UserPointType.LEVEL);
		//遍历积分列表，一一对比，如果发现用户满足的最高积分等级与用户当前等级不同，则调用change方法。
		final String hql = "select lfu from LevelForUser lfu where lfu.minPointLimit <= ? order by lfu.seqNum desc";
		List<LevelForUser> resultList = ht.execute(new HibernateCallback<List<LevelForUser>>() {
			public List<LevelForUser> doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createQuery(hql).setFirstResult(0).setMaxResults(1).setParameter(0, point).list();
			}
		});
		if (resultList.size()==1) {
			LevelForUser lfu = resultList.get(0);
			if (!lfu.getId().equals(user.getLevel().getId())) {
				change(user.getId(), lfu.getId(), lfu.getValidityPeriod(), null);
			}
		}
	}

}

package com.esoft.archer.user.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.exception.MinPointLimitCannotMattchSeqNumException;
import com.esoft.archer.user.exception.SeqNumAlreadyExistException;
import com.esoft.archer.user.exception.UserExistInLevelException;
import com.esoft.archer.user.model.LevelForUser;
import com.esoft.archer.user.service.LevelService;

@Service(value = "levelService")
public class LevelServiceImpl implements LevelService {

	@Resource
	private HibernateTemplate ht;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveOrUpdate(LevelForUser levelForUser)
			throws SeqNumAlreadyExistException,
			MinPointLimitCannotMattchSeqNumException {
		List<LevelForUser> lfus = ht.loadAll(LevelForUser.class);
		for (Iterator iterator = lfus.iterator(); iterator.hasNext();) {
			LevelForUser lfu = (LevelForUser) iterator.next();
			if (lfu.getId().equals(levelForUser.getId())) {
				// 如果是修改，则需要先移除相同id的元素，然后添加修改后的
				iterator.remove();
			}
		}
		if (lfus.size() > 0) {
			lfus.add(levelForUser);
			// 根据序号升序排序
			Collections.sort(lfus, new Comparator<LevelForUser>() {
				@Override
				public int compare(LevelForUser o1, LevelForUser o2) {
					return o1.getSeqNum() - o2.getSeqNum();
				}
			});
			// 遍历，判断
			for (int i = 0; i <= lfus.size() - 2; i++) {
				LevelForUser currentLfu = lfus.get(i);
				LevelForUser nextLfu = lfus.get(i + 1);
				// 序号不能为空，且序号不能与之前已有的序号相同
				if (currentLfu.getSeqNum() == nextLfu.getSeqNum()) {
					throw new SeqNumAlreadyExistException();
				}
				// 等级积分下线，与等级序号排序是否相同
				if (currentLfu.getMinPointLimit() >= nextLfu.getMinPointLimit()) {
					throw new MinPointLimitCannotMattchSeqNumException();
				}
			}
		}
		ht.saveOrUpdate(levelForUser);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void delete(String levelForUserId) throws UserExistInLevelException {
		// 如果有用户处于当前等级，则不能删除
		String hql = "select count(user) from User user where user.level.id = ?";
		long n = (Long) ht.find(hql, levelForUserId).get(0);
		if (n > 0) {
			throw new UserExistInLevelException();
		}
		ht.bulkUpdate("delete from LevelForUser lfu where lfu.id = ?",
				levelForUserId);
	}

}

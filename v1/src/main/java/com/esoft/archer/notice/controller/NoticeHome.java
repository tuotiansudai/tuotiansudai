package com.esoft.archer.notice.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.notice.model.NoticePool;
import com.esoft.core.annotations.ScopeType;

/**
 * 后台管理员通知
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class NoticeHome {

	@Resource
	NoticePool noticePool;

	public NoticePool getNoticePool() {
		return noticePool;
	}
	
}

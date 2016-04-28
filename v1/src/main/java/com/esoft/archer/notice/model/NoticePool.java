package com.esoft.archer.notice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * 通知池
 * 
 * @author Administrator
 * 
 */
@Component
public class NoticePool {
	private Set<Notice> notices = new HashSet();

	public void add(Notice notice) {
		notices.add(notice);
	}

	public void remove(Notice notice) {
		notices.remove(notice);
	}

	public Collection getAll() {
		return notices;
	}

	public List getList() {
		return new ArrayList<Notice>(notices);
	}

	public NoticePool() {
		new Thread(new AutoClear()).start();
	}

	class AutoClear implements Runnable {

		// 十秒钟清理一次，清理十秒钟之前的notice
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long current = System.currentTimeMillis();
				for (Iterator iterator = notices.iterator(); iterator.hasNext();) {
					Notice notice = (Notice) iterator.next();
					if (current - notice.getOccurTime().getTime() > 9999) {
						iterator.remove();
					}
				}
			}
		}

	}

}

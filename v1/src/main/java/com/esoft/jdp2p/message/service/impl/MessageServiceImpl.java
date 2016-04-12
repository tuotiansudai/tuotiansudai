package com.esoft.jdp2p.message.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessage;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.MailService;
import com.esoft.jdp2p.message.service.MessageService;
import com.esoft.jdp2p.message.service.SmsService;

@Service
public class MessageServiceImpl implements MessageService {
	@Resource
	private HibernateTemplate ht;

	@Resource
	private MessageBO messageBO;
	
	@Resource
	MailService mailService;
	
	@Resource
	private SmsService smsService;

	@Logger
	private static Log log;

	@Override
	public void sendMobileMsg(String userId, String msg)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		if (messageBO.isMessageWayenable(MessageConstants.UserMessageWayId.SMS)) {
			if (log.isDebugEnabled())
				log.debug("Send mobile message to User ," + "id = " + userId
						+ ",msg = " + msg);
			smsService.send(msg, user.getMobileNumber());
		}
	}

	@Override
	public void sendEmailMsg(String userId, String title, String msg)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		if (log.isDebugEnabled())
			log.debug("Send email message to User ," + "id = " + userId
					+ ",title=" + title + ",msg = " + msg);
		if (messageBO
				.isMessageWayenable(MessageConstants.UserMessageWayId.EMAIL)) {
			mailService.sendMail(user.getEmail(), title, msg);
		}
	}

	@Override
	public void sendStationMsg(String reveiverId, String senderId,
			String title, String msg) throws UserNotFoundException {
		User receiver = ht.get(User.class, reveiverId);
		if (receiver == null) {
			throw new UserNotFoundException("user.id:" + reveiverId);
		}
		User sender = ht.get(User.class, senderId);
		if (sender == null) {
			throw new UserNotFoundException("user.id:" + sender);
		}
		if (messageBO
				.isMessageWayenable(MessageConstants.UserMessageWayId.LETTER)) {
			if (log.isDebugEnabled())
				log.debug("Send private message to User ," + "reveiverid = "
						+ reveiverId + ",senderId=" + senderId + ",title="
						+ title + ",msg = " + msg);
			messageBO.sendStationMsg(receiver, sender, title, msg);
		}
	}

	@Override
	public List<UserMessageTemplate> getUserMessagesTemplatesByUserId(
			String userId) {
		String hql = "select um.userMessageTemplate from UserMessage um where um.user.id=?";
		return ht.find(hql, userId);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveUserMessages(String userId, List<UserMessageTemplate> umts) {
		ht.bulkUpdate("delete from UserMessage um where um.user.id=?", userId);
		for (UserMessageTemplate umt : umts) {
			UserMessage um = new UserMessage();
			um.setId(IdGenerator.randomUUID());
			um.setUser(new User(userId));
			um.setUserMessageTemplate(umt);
			ht.save(um);
		}
	}

}

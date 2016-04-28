package com.esoft.archer.comment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.comment.model.Comment;
import com.esoft.archer.comment.service.CommentService;
import com.esoft.archer.common.service.impl.BaseServiceImpl;
import com.esoft.archer.node.model.Node;

@Service(value = "commentService")
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements
		CommentService {
	@Resource
	private HibernateTemplate ht;

	public Long getCommentNumberFL(Node node) {
		String hql = "select count(comment) from Comment comment where comment.node.id='"
				+ node.getId() + "' AND comment.parentComment = null";
		return (Long) ht.find(hql).get(0);
	}

	public Comment findById(String commentId) {
		String hql = "select comment from Comment comment where comment.id='"
				+ commentId + "'";
		List<Comment> comments = ht.find(hql);
		if (comments != null && comments.size() > 0) {
			return (Comment) ht.find(hql).get(0);
		}
		return null;
	}

	public Long getCommentNumberFC(Comment comment) {
		String hql = "select count(comment) from Comment comment where comment.parentComment.id='"
				+ comment.getId() + "'";
		return (Long) ht.find(hql).get(0);
	}
}

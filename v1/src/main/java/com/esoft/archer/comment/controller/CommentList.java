package com.esoft.archer.comment.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.comment.CommentConstants;
import com.esoft.archer.comment.model.Comment;
import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.Loan;

@Component
@Scope(ScopeType.VIEW)
public class CommentList extends EntityQuery<Comment> implements Serializable  {

	private Date searchcommitMinTime;
	private Date searchcommitMaxTime;
	private String loanId;
	public CommentList() {
		final String[] RESTRICTIONS = {
			"loan.id = #{commentList.loanId}",
			"loan.id = #{commentList.example.loan.id}",
			"userByCreator.id = #{commentList.example.userByCreator.id}",
			"loan.name like #{commentList.example.loan.name}",
			"createTime >= #{commentList.searchcommitMinTime}",
			"createTime <= #{commentList.searchcommitMaxTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	@Override
	public List<Comment> getLazyModelData() {
		setCountHql("select count(comment) from Comment comment where comment.parentComment is null");
		setHql("select comment from Comment comment where comment.parentComment is null");
		return super.getLazyModelData();
	}
	
	@Override
	protected void initExample() {
		Comment comment = new Comment();
		comment.setParentComment(new Comment());
		comment.setLoan(new Loan());
		comment.setUserByCreator(new User());
		setExample(comment);
	}
	
	public List<Comment> getComments(String nodeId, int maxResult) {
		return getComments(nodeId, 0, maxResult);
	}

	/**
	 * 按照thread进行排序，用于前台展示。排序规则:评论最早的（包含其回复）放在最前面
	 * 
	 * @param nodeId
	 * @param firstResult
	 * @param maxResult
	 *            这里是指一级评论数量
	 * @return
	 */
	public List<Comment> getComments(String nodeId, int firstResult,
			int maxResult) {
		List<Comment> allComments = new ArrayList<Comment>();
		List<Comment> flComments = getFLComments(nodeId, firstResult, maxResult);
		for (Comment comment : flComments) {
			allComments.addAll(getChildren(comment));
		}
		allComments.addAll(flComments);
		Collections.sort(allComments);
		// FIXME:url 直接访问评论，怎么处理？
		return allComments;
	}

	/**
	 * 获取第一级的评论，按照thread升序， 这里主要用于控制分页用。
	 * 
	 * @param nodeId
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	private List<Comment> getFLComments(String nodeId, int firstResult,
			int maxResult) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
		criteria.addOrder(Order.asc("thread"));
		criteria.add(Restrictions.isNull("parentComment"));
		criteria.add(Restrictions.eq("node.id", nodeId));
		getHt().setCacheQueries(true);

		return getHt().findByCriteria(criteria, 0, maxResult);
	}

	/**
	 * 获取该comment的所有孩子评论
	 * @return
	 */
	private List<Comment> getChildren(Comment comment) {
		String hql = "from Comment comment where comment.thread like ? AND comment.node.id =?";
		List<Comment> cComments = getHt().find(hql, comment.getThread() + "_%",
				comment.getNode().getId());
		if (cComments.size() != 0) {
			Collections.sort(cComments);
		}
		return cComments;
	}
	
	/**
	 * 获取该comment的所有孩子一级评论
	 * 
	 * @return
	 */
	public List<Comment> getLeveloneChildren(Comment comment,int maxResult) {
 		DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
		criteria.add(Restrictions.eq("parentComment", comment));
		criteria.addOrder(Order.desc("createTime"));
		getHt().setCacheQueries(true);
		return getHt().findByCriteria(criteria, 0, maxResult);
	}

	/**
	 * 获取文章最新评论
	 * 
	 * @param nodeId
	 * @param maxResults
	 * @return
	 */
	public List<Comment> getLastestCommentsByNodeId(String nodeId,int maxResults) {
		return getLastestComments("node.id",nodeId,maxResults);
	}

	/**
	 * 获取借款最新评论
	 * 
	 * @param nodeId
	 * @param maxResults
	 * @return
	 */
	public List<Comment> getLastestCommentsByLoanId(String loanId,int maxResults) {
		return getLastestComments("loan.id",loanId,maxResults);
	}
	
	/**
	 * 获取最新评论
	 * 
	 * @param maxResults
	 * @return
	 */
	public List<Comment> getLastestComments(int maxResults) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
		criteria.addOrder(Order.desc("createTime"));
		criteria.add(Restrictions.eq("status",
				CommentConstants.COMMENT_STATUS_ENABLE));
		getHt().setCacheQueries(true);

		return getHt().findByCriteria(criteria, 0, maxResults);
	}
	
	/**
	 * 根据对应实体去获取最新评论
	 * @param column
	 * @param objId
	 * @param maxResults
	 * @return
	 */
	public List<Comment> getLastestComments(String column,String objId,int maxResults){
		DetachedCriteria criteria = DetachedCriteria.forClass(Comment.class);
		criteria.addOrder(Order.desc("createTime"));
		criteria.add(Restrictions.eq("status",CommentConstants.COMMENT_STATUS_ENABLE));
		criteria.add(Restrictions.eq(column, objId));
		criteria.add(Restrictions.isNull("parentComment"));
		getHt().setCacheQueries(true);
		
		return getHt().findByCriteria(criteria, 0, maxResults);
	}

	public Date getSearchcommitMinTime() {
		return searchcommitMinTime;
	}

	public void setSearchcommitMinTime(Date searchcommitMinTime) {
		this.searchcommitMinTime = searchcommitMinTime;
	}

	public Date getSearchcommitMaxTime() {
		return searchcommitMaxTime;
	}

	public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
		this.searchcommitMaxTime = searchcommitMaxTime;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

}

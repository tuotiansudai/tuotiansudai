package com.esoft.archer.comment.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.comment.CommentConstants;
import com.esoft.archer.comment.model.Comment;
import com.esoft.archer.comment.service.CommentService;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;
import com.esoft.jdp2p.loan.model.Loan;

/**
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class CommentHome extends EntityHome<Comment> {

	private static StringManager sm = StringManager
			.getManager(CommentConstants.Package);
	@Logger
	static Log log;
	/**
	 * 父节点id，用于回复评论
	 */
	private String pid;

	private boolean savecommentLoan = false;

	private String randCode;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Resource
	private CommentService cService;

	@Resource
	private LoginUserInfo loginUserInfo;

	@Resource
	private HibernateTemplate ht;

	private Comment parentComment;

	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}

	public Comment getParentComment() {
		if (this.parentComment == null) {
			// FIXME:用户随便输入一个url，是不是要进行判断？
			if (pid != null && !pid.equals("")) {
				if (this.parentComment == null) {
					this.parentComment = cService.findById(pid);
				}
			}
		}
		return this.parentComment;
	}

	public CommentHome() {
		setUpdateView(FacesUtil.redirect(CommentConstants.View.COMMENT_LIST));
		setDeleteView(FacesUtil.redirect(CommentConstants.View.COMMENT_LIST));
	}

	@Override
	protected Comment createInstance() {
		Comment comment = new Comment();
		comment.setNode(new Node());
		// 为了回复评论的时候方便
		comment.setParentComment(new Comment());
		return comment;
	}

	@Transactional(readOnly = false)
	public String deleteNode() {
		Comment comm = this.getBaseService().get(getEntityClass(),
				getInstance().getId());
		comm.setStatus(CommentConstants.COMMENT_STATUS_DISABLE);
		getBaseService().save(comm);
		try {
			FacesUtil.getHttpServletResponse().sendRedirect(
					FacesUtil.getCurrentAppUrl() + "/node/"
							+ getInstance().getNode().getId() + "#comment-"
							+ getInstance().getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 删除后台评论
	 */
	@Transactional(readOnly = false)
	public String delete() {
		return super.delete();
	}

	/**
	 * 保存前台用户评论
	 * @param loanId
	 * @param commentUserId
	 */
	@Transactional(readOnly = false)
	public void saveComment(String loanId, String commentUserId) {
		Loan loan = getBaseService().get(Loan.class, loanId);
		// FIXME:验证
		getInstance().setId(IdGenerator.randomUUID());
		getInstance().setLoan(loan);
		getInstance().setParentComment(null);
		getInstance().setCreateTime(new Date());
		getInstance().setNode(null);
		getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
		getInstance().setUserByCreator(new User(commentUserId));
		getInstance().setUpdateTime(new Date());
		getInstance().setIp(FacesUtil.getHttpServletRequest().getRemoteHost());
		getBaseService().save(getInstance());
		FacesUtil.addInfoMessage("留言成功！");
	}

	/**
	 * 保存前台用户评论
	 * @param loan
	 */
	@Transactional(readOnly = false)
	public String saveComment(Loan loan) {

		getInstance().setId(IdGenerator.randomUUID());
		getInstance().setLoan(loan);
		getInstance().setCreateTime(new Date());
		getInstance().setNode(null);
		Comment com = ht.get(Comment.class, pid);
		if (com != null) {
			getInstance().setBody(
					getInstance().getBody().replace(
							"@" + com.getUserByCreator().getUsername() + ":",
							""));
		}
		getInstance().setParentComment(com);
		getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);

		User user = getBaseService().get(User.class,
				loginUserInfo.getLoginUserId());
		getInstance().setUserByCreator(user);
		getInstance().setUpdateTime(new Date());
		getInstance().setIp(FacesUtil.getHttpServletRequest().getRemoteHost());
		super.save(false);
		savecommentLoan = true;
		return null;
	}

	/**
	 * 后台保存/编辑评论
	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {
		boolean isEdit = false;
		if (getInstance().getId() != null && !getInstance().getId().equals("")) {
			// 编辑状态
			Comment comm = this.getBaseService().get(getEntityClass(),
					getInstance().getId());
			comm.setTitle(getInstance().getTitle());
			comm.setBody(getInstance().getBody());
			this.setInstance(comm);
			isEdit = true;
		} else {
			getInstance().setId(IdGenerator.randomUUID());
			if (getParentComment() == null) {
				Long index = cService.getCommentNumberFL(getInstance()
						.getNode()) + 1;
				getInstance().setThread(index.toString());
				getInstance().setParentComment(null);
			} else {
				Long index = cService.getCommentNumberFC(parentComment) + 1;
				getInstance().setThread(
						parentComment.getThread() + "." + index.toString());
				getInstance().setParentComment(parentComment);
			}
			// setUpdateView(FacesUtil.redirect("pretty:/node/structure"));
			getInstance().setCreateTime(new Date());
			getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
			// 这个添加评论时候得到用户信息，从spring-security上下文对象中得出
			User user = getBaseService().get(User.class,
					loginUserInfo.getLoginUserId());
			// System.out.println(getInstance().getNode());
			getInstance().setUserByCreator(user);

			if (log.isInfoEnabled()) {
				log.info(sm.getString("log.addComment", getInstance()
						.getCreateTime(), user.getUsername(), getInstance()
						.getNode().getId(), getInstance().getId()));
			}
			getInstance().setUpdateTime(new Date());
			getInstance().setIp(
					FacesUtil.getHttpServletRequest().getRemoteHost());

		}
		return super.save();
	}

	public boolean hasInvestRole(User user) {
		for (Role role : user.getRoles()) {
			if (role.getId().equals("INVESTOR")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证注册验证码
	 */
	public boolean checkvalidateCode() {
		String systemValidatecode = (String) FacesUtil
				.getSessionAttribute("rand");
		if (StringUtils.isEmpty(getRandCode())) {
			return true;
		}
		return systemValidatecode.equals(getRandCode().toUpperCase()) ? false
				: true;
	}

	public boolean isSavecommentLoan() {
		return savecommentLoan;
	}

	public void setSavecommentLoan(boolean savecommentLoan) {
		this.savecommentLoan = savecommentLoan;
	}

	public String getRandCode() {
		return randCode;
	}

	public void setRandCode(String randCode) {
		this.randCode = randCode;
	}

}

package com.esoft.archer.node.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.esoft.archer.comment.model.Comment;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.user.model.User;

/**
 * Node entity. 节点表，该节点可以是文章，也可以使论坛主题，也可以是商品展示，等等。
 */
@Entity
@Table(name = "node")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
		@NamedQuery(name = "Node.findNodeByTermOrderByCreateTime", query = "Select node from Node node, CategoryTerm term "
				+ " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"
				+ " order by node.createTime desc"),
		@NamedQuery(name = "Node.findNodeByTermOrderBySeqNumAndCreateTime", query = "Select node from Node node, CategoryTerm term "
				+ " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"
				+ " order by node.seqNum desc, node.createTime desc"),
		@NamedQuery(name = "Node.findNodeByTermOrderByUpdateTime", query = "Select node from Node node, CategoryTerm term "
				+ " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"
				+ " order by node.updateTime desc"),
		@NamedQuery(name = "Node.findNodeByTermOrderBySeqNumAndUpdateTime", query = "Select node from Node node, CategoryTerm term "
				+ " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"
				+ " order by node.seqNum desc, node.updateTime desc"),
		@NamedQuery(name = "Node.getNodeCountByTermId", query = "Select count(node) from Node node, CategoryTerm term "
				+ " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"),
		@NamedQuery(name = "Node.getNodeCount", query = "Select count(node) from Node node where node.status = 1 and node.nodeType.id =:nodeTypeId"),
		@NamedQuery(name = "Node.findNodeOrderByUpdateTime", query = "from Node node where node.nodeType.id =:nodeTypeId order by updateTime desc"),
		@NamedQuery(name = "Node.findNodeOrderBySeqNumAndUpdateTime", query = "from Node node where node.nodeType.id =:nodeTypeId order by seqNum, updateTime desc"),
		@NamedQuery(name = "Node.findAllNodeAndNodeBody", query = "Select distinct n from Node n left join fetch n.nodeBody") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
// @NamedNativeQuery(name="",query
// ="Select * from node where id in (Select node_type_id from node_type_category_term where category_term_id = ? )")
public class Node implements java.io.Serializable {

	// Fields

	private String id;
	private User userByLastModifyUser;
	private NodeType nodeType;
	private NodeBody nodeBody;
	private User userByCreator;
	private String title;
	private String subtitle;
	private String language;
	/** 缩略图 */
	private String thumb;
	/**
	 * 1 = 已发表；0 = 未发表
	 */
	private String status;
	private String keywords;
	private String description;
	private Date createTime;
	private Date updateTime;
	private Double version;
	private Integer seqNum;
	private List<NodeBodyHistory> nodeBodyHistories = new ArrayList<NodeBodyHistory>(
			0);
	private List<NodeAttr> nodeAttrs = new ArrayList<NodeAttr>(0);
	private List<Comment> comments = new ArrayList<Comment>(0);
	private List<CategoryTerm> categoryTerms = new ArrayList<CategoryTerm>(0);

	// Constructors

	/** default constructor */
	public Node() {
	}

	public Node(String id) {
		this.id = id;
	}

	/** minimal constructor */
	public Node(String id, User userByLastModifyUser, NodeType nodeType,
			User userByCreator, String status) {
		this.id = id;
		this.userByLastModifyUser = userByLastModifyUser;
		this.nodeType = nodeType;
		this.userByCreator = userByCreator;
		this.status = status;
	}

	/** full constructor */
	public Node(String id, User userByLastModifyUser, NodeType nodeType,
			NodeBody nodeBody, User userByCreator, String title,
			String language, String status, String keywords,
			String description, Date createTime, Date updateTime,
			Double version, Integer seqNum,
			List<NodeBodyHistory> nodeBodyHistories, List<NodeAttr> nodeAttrs,
			List<Comment> comments, List<CategoryTerm> categoryTerms) {
		this.id = id;
		this.userByLastModifyUser = userByLastModifyUser;
		this.nodeType = nodeType;
		this.nodeBody = nodeBody;
		this.userByCreator = userByCreator;
		this.title = title;
		this.language = language;
		this.status = status;
		this.keywords = keywords;
		this.description = description;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.version = version;
		this.seqNum = seqNum;
		this.nodeBodyHistories = nodeBodyHistories;
		this.nodeAttrs = nodeAttrs;
		this.comments = comments;
		this.categoryTerms = categoryTerms;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", title=" + title + "]";
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_modify_user")
	public User getUserByLastModifyUser() {
		return this.userByLastModifyUser;
	}

	public void setUserByLastModifyUser(User userByLastModifyUser) {
		this.userByLastModifyUser = userByLastModifyUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "node_type", nullable = false)
	public NodeType getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	// @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "body")
	public NodeBody getNodeBody() {
		return this.nodeBody;
	}

	public void setNodeBody(NodeBody nodeBody) {
		this.nodeBody = nodeBody;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator", nullable = false)
	public User getUserByCreator() {
		return this.userByCreator;
	}

	public void setUserByCreator(User userByCreator) {
		this.userByCreator = userByCreator;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "subtitle", length = 100)
	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Column(name = "language", length = 10, nullable = false)
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Column(name = "thumb", length = 80)
	public String getThumb() {
		return thumb;
	}

	/**
	 * <li>1 = 已经发表 （default）</li> <li>0 = 未发表</li> <li>2 = 已经删除</li>
	 * 
	 * @return
	 */
	@Column(name = "status", nullable = false, length = 20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Lob 
	@Column(name = "keywords", columnDefinition="CLOB")
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "create_time", length = 19, nullable = false,updatable=false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "update_time", length = 19, nullable = false)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "version", precision = 22, scale = 0)
	public Double getVersion() {
		return this.version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "node")
	public List<NodeBodyHistory> getNodeBodyHistories() {
		return this.nodeBodyHistories;
	}

	public void setNodeBodyHistories(List<NodeBodyHistory> nodeBodyHistories) {
		this.nodeBodyHistories = nodeBodyHistories;
	}

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "node_node_attr", joinColumns = { @JoinColumn(name = "node_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "node_attr_id", nullable = false, updatable = false) })
	public List<NodeAttr> getNodeAttrs() {
		return this.nodeAttrs;
	}

	public void setNodeAttrs(List<NodeAttr> nodeAttrs) {
		this.nodeAttrs = nodeAttrs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "node")
	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "category_term_node", joinColumns = { @JoinColumn(name = "node_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "term_id", nullable = false, updatable = false) })
	public List<CategoryTerm> getCategoryTerms() {
		return this.categoryTerms;
	}

	public void setCategoryTerms(List<CategoryTerm> categoryTerms) {
		this.categoryTerms = categoryTerms;
	}

}
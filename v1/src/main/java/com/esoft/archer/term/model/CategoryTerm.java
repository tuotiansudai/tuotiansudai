package com.esoft.archer.term.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.esoft.archer.node.model.Node;

/**
 * CategoryTerm entity. 分类术语，node的分类
 * 
 */
@Entity
@Table(name = "category_term")
@NamedQueries({
		@NamedQuery(name = "CategoryTerm.findByType", query = "select c from CategoryTerm c where c.categoryTermType.id = ?"),
		@NamedQuery(name = "CategoryTerm.findByParentId", query = "select c from CategoryTerm c where c.parent.id = ?"),
		@NamedQuery(name = "CategoryTerm.getTermCountByParentId", query = "select count(c) from CategoryTerm c where c.parent.id =:pId"),
		@NamedQuery(name = "CategoryTerm.findByParentIdOrderBySeqNum", query = "select c from CategoryTerm c where c.parent.id =:pId order by c.seqNum asc") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CategoryTerm implements java.io.Serializable {

	private static final long serialVersionUID = -4573430309029721341L;
	// Fields
	private String id;
	private CategoryTermType categoryTermType;
	private String name;
	private String thumb;
	private CategoryTerm parent;
	private String description;
	private Integer seqNum = 0 ;
	private List<CategoryTerm> children = new ArrayList<CategoryTerm>(0);
	private Set<Node> nodes = new HashSet<Node>(0);

	// Constructors

	/** default constructor */
	public CategoryTerm() {
	}

	public CategoryTerm(String id) {
		super();
		this.id = id;
	}

	public CategoryTerm(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** minimal constructor */
	public CategoryTerm(String id, CategoryTermType categoryTermType) {
		this.id = id;
		this.categoryTermType = categoryTermType;
	}

	/** full constructor */
	public CategoryTerm(String id, CategoryTermType categoryTermType,
			String name, String description, Integer seqNum, Set<Node> nodes) {
		this.id = id;
		this.categoryTermType = categoryTermType;
		this.name = name;
		this.description = description;
		this.seqNum = seqNum;
		this.nodes = nodes;
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
	@JoinColumn(name = "type", nullable = false)
	public CategoryTermType getCategoryTermType() {
		return this.categoryTermType;
	}

	public void setCategoryTermType(CategoryTermType categoryTermType) {
		this.categoryTermType = categoryTermType;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	@Column(name = "thumb", length = 80)
	public String getThumb() {
		return thumb;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public CategoryTerm getParent() {
		return parent;
	}

	public void setParent(CategoryTerm parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	public List<CategoryTerm> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryTerm> children) {
		this.children = children;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "categoryTerms")
	public Set<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

}
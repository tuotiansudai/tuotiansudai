package com.esoft.archer.lucene.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.lucene.LuceneConstants;
import com.esoft.archer.lucene.model.Indexing;
import com.esoft.archer.lucene.model.ResultBean;
import com.esoft.archer.lucene.service.LuceneService;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.user.model.User;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.FilterCharUtil;
import com.esoft.core.util.StringManager;

@Service
public class LuceneServiceImpl implements LuceneService {
	static Log log = LogFactory.getLog(LuceneServiceImpl.class);
	private static final StringManager sm = StringManager
			.getManager(LuceneConstants.Package);
	@Resource
	private HibernateTemplate ht;

	/**
	 * 搜索结果分页
	 */
	public PageModel<ResultBean> paginationResult(String key, String indexPath,
			int pageNo, int pageSize) {
		PageModel<ResultBean> pageModel = new PageModel<ResultBean>();
		if (StringUtils.isEmpty(key)) {
			return pageModel;
		}

		List<ResultBean> list = new ArrayList<ResultBean>();
		list = search(key, indexPath);
		int begin = pageSize * (pageNo - 1);// 本页显示的起始位置
		int end = Math.min(begin + pageSize, list.size());// 本页的结束位置
		if (begin == end) {
			pageModel.setCount(list.size());
			pageModel.setPage(pageNo);
			pageModel.setPageSize(pageSize);
			pageModel.setData(list);
			return pageModel;
		}
		List<ResultBean> result = new ArrayList<ResultBean>();
		for (int i = begin; i < end; i++) {
			result.add(list.get(i));
		}
		pageModel.setCount(list.size());
		pageModel.setPage(pageNo);
		pageModel.setPageSize(pageSize);
		pageModel.setData(result);
		return pageModel;
	}

	/**
	 * 搜索
	 */
	public List<ResultBean> search(String param, String indexPath) {
		List<ResultBean> list = new ArrayList<ResultBean>();
		File indexDir = new File(indexPath);
		Directory directory = null;
		IndexReader indexReader = null;
		try {
			directory = FSDirectory.open(indexDir);
			indexReader = IndexReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Analyzer analyzer = new IKAnalyzer(true);

			QueryParser qp = new QueryParser(Version.LUCENE_40,
					LuceneConstants.Lucene.CONTENT, analyzer);
			qp.setDefaultOperator(QueryParser.AND_OPERATOR);
			Sort sort = new Sort(new SortField(LuceneConstants.Lucene.CONTENT,
					SortField.Type.SCORE, false));
			Query query = qp.parse(param);
			TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE,
					sort);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				// 封装一个结果bean并放入list中
				Document targetDoc = indexSearcher.doc(scoreDocs[i].doc);
				ResultBean resultBean = createResultBean(targetDoc, query,
						analyzer);
				list.add(resultBean);
			}
			indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(sm.getString("log.search.error", param, e));
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(sm.getString("log.search.error", param, e));
		}
		return list;
	}

	/**
	 * 创建索引
	 */
	public void createNewIndex(Indexing indexing, String indexPath, int weight) {
		/*IndexWriter indexWriter = null;
		try {
			indexWriter = getIndexWriter(indexPath);
			Document doc = getDocument(indexing, weight);
			// 以索引的ID域为“主键”创建索引，若索引已经存在，则删除原有的索引再重新创建
			indexWriter.updateDocument(new Term(LuceneConstants.Lucene.FIELDID,
					indexing.getId()), doc);
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(sm.getString("log.createIndex.error", indexing.getId(), e));
		}*/
	}

	/**
	 * 高亮设置
	 * 
	 * @param query
	 * @param doc
	 * @param field
	 *            指定显示高亮内容的域
	 * @return
	 */
	private String toHighlighter(Query query, Document doc, String field,
			Analyzer analyzer) {
		try {
			String str = doc.get(field);
			SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter(
					"<font color=\"red\">", "</font>");
			Highlighter highlighter = new Highlighter(simpleHtmlFormatter,
					new QueryScorer(query));
			// 设置显示高亮文本的长度 str.length()指定长度为该文本的长度
			highlighter.setTextFragmenter(new SimpleFragmenter(100));
			String highlighterStr = highlighter.getBestFragment(analyzer,
					field, str);
			return highlighterStr;
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (InvalidTokenOffsetsException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 封装查询结果
	 */
	private ResultBean createResultBean(Document targetDoc, Query query,
			Analyzer analyzer) {
		ResultBean resultBean = new ResultBean();
		resultBean.setAuthor(targetDoc.get(LuceneConstants.Lucene.AUTHOR));
		String highLightStr = toHighlighter(query, targetDoc,
				LuceneConstants.Lucene.CONTENT, analyzer);
		if (StringUtils.isNotEmpty(highLightStr)) {
			resultBean.setContent(highLightStr + "...");
		} else {
			resultBean
					.setContent(targetDoc.get(LuceneConstants.Lucene.CONTENT));
		}
		resultBean.setId(targetDoc.get(LuceneConstants.Lucene.FIELDID));
		String highLighttitle = toHighlighter(query, targetDoc,
				LuceneConstants.Lucene.TITLE, analyzer);
		if (StringUtils.isNotEmpty(highLighttitle)) {
			resultBean.setTitle(highLighttitle);
		} else {
			resultBean.setTitle(targetDoc.get(LuceneConstants.Lucene.TITLE));
		}
		String date = targetDoc.get(LuceneConstants.Lucene.INDEXDATE);
		SimpleDateFormat sdf = getSimpleDateFormat();
		Date newDate = null;
		try {
			newDate = sdf.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			log.error(sm.getString("log.dateFormat.error", String.class,
					Date.class));
		}
		resultBean.setCreateTime(newDate);
		return resultBean;
	}

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 重置索引
	 */
	public void rebuildIndex(String indexPath) {
		try {
			IndexWriter indexWriter = getIndexWriter(indexPath);
			List<Node> nodeList = new ArrayList<Node>();
			int times = 0; // 用于记录每次循环次数
			do {
				final int limit = 1000 * times;
				nodeList = ht.executeFind(new HibernateCallback<List<Node>>() {
					public List<Node> doInHibernate(Session session)
							throws HibernateException, SQLException {
						org.hibernate.Query query = session
								.getNamedQuery("Node.findAllNodeAndNodeBody");
						query.setFirstResult(limit);
						query.setMaxResults(1000);
						return query.list();
					}

				});

				if (nodeList != null && nodeList.size() > 0) {
					for (int i = 0; i < nodeList.size(); i++) {
						Indexing indexing = new Indexing();
						indexing.setId(nodeList.get(i).getId());
						indexing.setTitle(nodeList.get(i).getTitle());
						indexing.setAuthor(nodeList.get(i).getUserByCreator()
								.getUsername());
						indexing.setContent(nodeList.get(i).getNodeBody()
								.getBody());
						indexing.setCreateTime(nodeList.get(i).getCreateTime());

						Document doc = getDocument(indexing, 1);
						// 以索引的ID域为“主键”创建索引，若索引已经存在，则删除原有的索引再重新创建
						indexWriter.updateDocument(
								new Term(LuceneConstants.Lucene.FIELDID,
										indexing.getId()), doc);
					}
				}
				times = times + 1;
			} while (nodeList != null && nodeList.size() > 0);
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sm.getString("log.rebuildIndex.error", e));
		}
	}

	/**
	 * 获得IndexWriter对象
	 * 
	 * @param request
	 * @return
	 */
	private IndexWriter getIndexWriter(String indexPath) {
		Directory indexDictory = null;
		IndexWriter indexWriter = null;
		try {
			indexDictory = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new IKAnalyzer(true);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,
					analyzer);
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			iwc.setMaxBufferedDocs(1000);
			if (IndexWriter.isLocked(indexDictory)) {
				IndexWriter.unlock(indexDictory);
			}
			indexWriter = new IndexWriter(indexDictory, iwc);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(sm.getString("log.rebuildIndex.error", e));
		}
		return indexWriter;
	}

	private Document getDocument(Indexing indexing, int weight) {
		Document doc = new Document();
		// 创建索引的文本域
		Field contentField = new Field(LuceneConstants.Lucene.CONTENT,
				FilterCharUtil.Html2Text(indexing.getContent()), Store.YES,
				Index.ANALYZED);
		// contentField.setBoost(10.0f); //创建索引时设置关键字的权重，数值越大，排名越靠前
		// 创建索引的标题域
		Field titleField = new Field(LuceneConstants.Lucene.TITLE,
				indexing.getTitle(), Store.YES, Index.ANALYZED);
		// titleField.setBoost(10.0f);
		// 创建索引的作者域
		Field authorFiled = new Field(LuceneConstants.Lucene.AUTHOR,
				indexing.getAuthor(), Store.YES, Index.ANALYZED);
		// 创建索引的ID域
		Field idField = new Field(LuceneConstants.Lucene.FIELDID,
				indexing.getId(), Store.YES, Index.ANALYZED);
		/**
		 * 创建索引的时间域
		 */
		SimpleDateFormat sdf = getSimpleDateFormat();
		String now = sdf.format(indexing.getCreateTime());
		Field dateField = new Field(LuceneConstants.Lucene.INDEXDATE, now,
				Store.YES, Index.ANALYZED);

		doc.add(contentField);
		doc.add(titleField);
		doc.add(authorFiled);
		doc.add(idField);
		doc.add(dateField);
		return doc;
	}

	/**
	 * 设置文章的权重已确定其在搜索结果中显示的位置
	 */
	public void setWeight(int weight) {
		// TODO 设置文章权重的实现

	}

	public void deleteIndex(String id, String indexPath) {
		IndexWriter indexWriter = null;
		try {
			indexWriter = getIndexWriter(indexPath);
			indexWriter.deleteDocuments(new Term(
					LuceneConstants.Lucene.FIELDID, id));
			indexWriter.close();
			if (log.isInfoEnabled()) {
				log.info(sm.getString("log.info.deleteIndex", FacesUtil
						.getExpressionValue("#{loginUserInfo.loginUserId}"), id));
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(sm.getString("log.error.deleteIndex", id, e));
		}
	}

}

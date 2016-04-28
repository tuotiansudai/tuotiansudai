package com.esoft.archer.lucene;

import com.esoft.core.jsf.util.FacesUtil;

public class LuceneConstants {
	public final static String Package = "com.esoft.archer.lucene";
	
	public final static String LUCENE_INDEX_PATH = FacesUtil.getAppRealPath() + "/lucene/index";

	public static class Lucene {
		/**
		 * 索引对应节点的内容
		 */
		public final static String CONTENT = "content";
		/**
		 * 索引对应节点的标题
		 */
		public final static String TITLE = "title";
		/**
		 * 索引对应节点的作者
		 */
		public final static String AUTHOR = "author";
		/**
		 * 索引对应节点的ID
		 */
		public final static String FIELDID = "fileid";
		/**
		 * 索引的创建时间
		 */
		public final static String INDEXDATE = "indexDate";
		/**
		 * 数据库中索引文件所在的目录的ID，相对的项目，可在项目中配置
		 */
		public final static String INDEXABSOLUTLYPATHID = "index_path";
	}
}

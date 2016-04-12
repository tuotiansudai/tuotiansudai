package com.esoft.archer.lucene.service;

import java.util.List;

import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.lucene.model.Indexing;
import com.esoft.archer.lucene.model.ResultBean;

public interface LuceneService {
	/**
	 * 对搜索结果分页
	 * 
	 * @param param 查询的关键字
	 * @param pageNo 页码号
	 * @param indexPath 索引文件所在的路径
	 * @param pageSize 每页显示的数据条数
	 * @return
	 */
	public PageModel<ResultBean> paginationResult(String param,String indexPath,int pageNo, int pageSize);

	/**
	 * 查询
	 * 
	 * @param param
	 *            查询的关键字
	 * @return
	 */
	public List<ResultBean> search(String param, String indexPath);

	/**
	 * 创建索引
	 * 
	 * @param index
	 */
	public void createNewIndex(Indexing indexing, String indexPath, int weight);

	/**
	 * 重建索引
	 */
	public void rebuildIndex(String indexPath);

	/**
	 * 设置文章的索引权重
	 * 
	 * @param weight
	 */
	public void setWeight(int weight);
	/**
	 * 根据Id删除相应的的索引
	 * @param id
	 * @param indexPath
	 */
	public void deleteIndex(String id,String indexPath);
}

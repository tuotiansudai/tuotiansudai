package com.esoft.archer.term;

import com.esoft.archer.menu.model.Menu;


/**
 * Menu constants.
 * @author wanghm
 *
 */
public class TermConstants {
	
	/**
	 * Package name. com.esoft.archer.term
	 */
	public static final String Package = "com.esoft.archer.term";
	
	/**
	 * 有关分类术语（term）页面
	 * @author wanghm
	 *
	 */
	public static class View{
		public final static String VIEW_DIR = "/admin/term";
		
		/**
		 * 分类术语列表页面
		 */
		public static final String TERM_LIST = VIEW_DIR+"/categoryTermList";
		
		/**
		 * 分类术语编辑页面
		 */
		public static final String TERM_EDIT= "/admin/menu/categoryTermEdit";
		
		/**
		 * 分类术语类型列表页面
		 */
		public static final String TERM_TYPE_LIST = VIEW_DIR+"/categoryTermTypeList";
		
		/**
		 * 分类术语类型编辑页面
		 */
		public static final String TERM_TYPE_EDIT = "/admin/menu/categoryTermTypeEdit";
		
		
	}
	
	/**
	 * Term 模块所有的命名查询
	 */
	public static class NamedQuery{
		
		
	}
	
}

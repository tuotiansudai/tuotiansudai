package com.esoft.archer.product;

/**
 * Product constants.
 * 
 */
public class ProductConstants {

	/**
	 * Package name. com.esoft.archer.product
	 */
	public static final String Package = "com.esoft.archer.product";

	public static final String NodeType = "PRODUCT";

	/**
	 * 产品模块（Product）一些前台展示页面
	 * 
	 */
	public static class View {
		public final static String VIEW_DIR = "/admin/product";

		/**
		 * 产品列表页面
		 */
		public static final String PRODUCT_LIST = VIEW_DIR + "/productList";

		/**
		 * 产品编辑页面
		 */
		public static final String PRODUCT_EDIT = VIEW_DIR + "/productEdit";

	}

}

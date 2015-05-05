package com.esoft.archer.menu;

import com.esoft.archer.menu.model.Menu;


/**
 * Menu constants.
 * @author wanghm
 *
 */
public class MenuConstants {
	
	/**
	 * Package name. com.esoft.archer.menu
	 */
	public static final String Package = "com.esoft.archer.menu";
	
	/**
	 * 有关菜单（Menu）模块的一些展示页面
	 * @author wanghm
	 *
	 */
	public static class View{
		
		/**
		 * 菜单列表页面
		 */
		public static final String MENU_LIST = "/admin/menu/menuList";
		
		/**
		 * 菜单编辑页面
		 */
		public static final String EDIT_MENU = "/admin/menu/menuEdit";
		
		/**
		 * 菜单类型编辑页面
		 */
		public static final String EDIT_MENU_TYPE = "/admin/menu/menuTypeEdit";
		
		/**
		 * 菜单类型列表页面
		 */
		public static final String MENU_TYPE_LIST = "/admin/menu/menuTypeList";
		
	}
	
	/**
	 * 菜单类型
	 */
	public static class MenuType{
		/**
		 * 主菜单
		 */
		public static final String MAIN_MENU = "MainMenu";
		
		/**
		 * 管理菜单
		 */
		public static final String MANAGEMENT = "Management";
		
		/**
		 * 导航菜单
		 */
		public static final String NAVIGATION = "Navigation";
	}
	
	/** 如果该菜单有子项，该菜单将始终以展开方式显示。 */
	public final static String EXPANDED_ENABLE = "1";
	/** 即时该菜单有子项，也不显示在项。 */
	public final static String EXPANDED_DISABLE = "0";
	
	/**
	 * Menu 模块所有的命名查询
	 */
	public static class NamedQuery{
		/**
		 * 通过菜单类型查询菜单 
		 * {@link Menu}
		 */
		public static final String MENU_FIND_MENU_BY_TYPE = "Menu.findMenuByType";
		
	}
	
}

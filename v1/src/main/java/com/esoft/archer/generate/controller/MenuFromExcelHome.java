package com.esoft.archer.generate.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.generate.log.GenerateLog;
import com.esoft.archer.generate.model.MenuFromExcel;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.term.model.CategoryTermType;
import com.esoft.core.annotations.ScopeType;

/**
 * beta 0.2 尚在测试 。。。自动生成menu和term
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class MenuFromExcelHome {
	@Resource
	private HibernateTemplate ht;

	public static void main(String[] args) throws FileNotFoundException {
		String templateUrl = MenuFromExcelHome.class.getResource("contextTemplate.xlsx")
				.getPath();
		templateUrl = URLDecoder.decode(templateUrl);
		MenuFromExcelHome test = new MenuFromExcelHome();
		File f = new File(templateUrl);
		InputStream is = new FileInputStream(f);
		test.generateMenu(is);
	}

	public void generateMenu(InputStream is) {
		GenerateLog log;
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		XSSFSheet typeSheet;
		// 所有的分类类型
		List<TinyClass> termTypes = new ArrayList<TinyClass>();
		List<TinyClass> nodeTypes = new ArrayList<TinyClass>();
		List<TinyClass> urlTypes = new ArrayList<TinyClass>();
		List<TinyClass> menuTypes = new ArrayList<TinyClass>();
		try {
			workbook = new XSSFWorkbook(is);
			sheet = workbook.getSheetAt(0);
			typeSheet = workbook.getSheetAt(1);
			log = new GenerateLog();
			collectTypes(typeSheet, termTypes, nodeTypes, urlTypes, menuTypes);
			List<MenuFromExcel> menus = createMenus(sheet);
			validateAndFillDefault(menus, log, termTypes, nodeTypes, urlTypes,
					menuTypes);
			Map<String, MenuFromExcel> menusMap = buildTree(menus, log);
			changeMenuUrl(menus);
			if (!log.isEmpty()) {
				// 有错误，返回显示错误
				for (String error : log.getErrorLogs()) {
					System.out.println(error);
				}
			} else {
				// 创建相应term 插入menu和term 返回sql和结果
				List<CategoryTerm> terms = createTerms(menus);
				menus = sortMenus(menus);
				insertTermTypes(termTypes);
				insertNodeTypes(nodeTypes);
				insertMenuTypes(menuTypes);
				insertMenus(menus);
				insertTerms(terms);
				insertNodeTerm(terms);
				insertSingleNode(menus);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果菜单的UrlType是node，则自动生成该node
	 * 
	 * @param menus
	 */
	private void insertSingleNode(List<MenuFromExcel> menus) {
		for (MenuFromExcel menu : menus) {
			if (menu.getUrlType().equals("node")) {
				String hqlNodeBody = "INSERT INTO `node_body` (id,body) VALUES ('"
						+ menu.getId()
						+ "_node_body"
						+ "', '"
						+ menu.getLabel() + "');";
				String hqlNode = "INSERT INTO `node` (id, node_type, title, subtitle, body, language, status, keywords, description,create_time, update_time, version, creator, last_modify_user, seq_num,thumb) VALUES ('"
						+ menu.getId()
						+ "', 'article', '"
						+ menu.getLabel()
						+ "', '', '"
						+ menu.getId()
						+ "_node_body"
						+ "', '*', '1', '', '"
						+ menu.getLabel()
						+ "', '2013-07-13 18:18:01', '2013-07-24 17:45:45', null, 'admin', null, null, '/upload/test_node.jpg');";
				System.out.println(hqlNodeBody);
				System.out.println(hqlNode);
				if (menu.getParent() == null) {
					// 所有属于单页的node，都归属于single_page分类
					String sql = "INSERT INTO category_term_node "
							+ "(node_id, term_id) " + "VALUES " + "('"
							+ menu.getId() + "', 'single_page');";
					System.out.println(sql);
				} else {
					String sql = "INSERT INTO category_term_node "
							+ "(node_id, term_id) " + "VALUES " + "('"
							+ menu.getId() + "', '" + menu.getParent().getId()
							+ "');";
					System.out.println(sql);
				}
			}
		}
	}

	/**
	 * 修改主菜单的Url，如果有孩子节点，默认为第一个孩子节点的url
	 * 
	 * @param menus
	 */
	private void changeMenuUrl(List<MenuFromExcel> menus) {
		for (MenuFromExcel menu : menus) {
			// url
			if (StringUtils.isEmpty(menu.getUrl())) {
				if (menu.getUrlType().equals("term")) {
					menu.setUrl("/" + menu.getUrlType() + "/" + menu.getId());
				} else if (menu.getUrlType().equals("node")) {
					if (menu.getParent() != null) {
						// 有父菜单，即该node有所属的term
						menu.setUrl("/" + menu.getUrlType() + "/"
								+ menu.getParent().getId() + "/" + menu.getId());
					} else {
						// 没有父菜单，即隶属于主菜单但是却是指向一个node，例如关于我们之类。。
						menu.setUrl("/snode/" + menu.getId());
					}
				} else if (menu.getUrlType().equals("spage")) {
					menu.setUrl("/" + menu.getUrlType() + "/" + menu.getId());
				}
			}
		}
		// 替换主菜单的url
		for (MenuFromExcel menu : menus) {
			if (menu.isParent()) {
				// 一级一级递归遍历，找到seqNum最小的叶子节点。
				menu.setUrl(sortChildrenAndGetFirst(menu).getUrl());
			}
		}
	}

	private MenuFromExcel sortChildrenAndGetFirst(MenuFromExcel menu) {
		if (menu.isParent()) {
			List<MenuFromExcel> children = sortBySeqNum(menu.getChildren());
			MenuFromExcel mc = children.get(0);
			return sortChildrenAndGetFirst(mc);
		} else {
			return menu;
		}
	}

	private List<MenuFromExcel> sortBySeqNum(List<MenuFromExcel> menus) {
		Collections.sort(menus, new Comparator<MenuFromExcel>() {
			public int compare(MenuFromExcel o1, MenuFromExcel o2) {
				return Integer.parseInt(o1.getSeqNum())
						- Integer.parseInt(o2.getSeqNum());
			}
		});
		return menus;
	}

	private void insertNodeTerm(List<CategoryTerm> terms) {
		for (CategoryTerm term : terms) {
			String sql = "INSERT INTO category_term_node "
					+ "(node_id, term_id) " + "VALUES " + "('test', '"
					+ term.getId() + "');";
			System.out.println(sql);
		}
	}

	private void insertMenuTypes(List<TinyClass> menuTypes) {
		for (TinyClass menuType : menuTypes) {
			String sql = "INSERT INTO menu_type "
					+ "(id, name, description, enable) " + "VALUES " + "('"
					+ menuType.getId() + "', '" + menuType.getName()
					+ "', null, '1');";
			System.out.println(sql);
		}
	}

	private void insertNodeTypes(List<TinyClass> nodeTypes) {
		for (TinyClass nodeType : nodeTypes) {
			String sql = "INSERT INTO node_type " + "(id, name, description) "
					+ "VALUES " + "('" + nodeType.getId() + "', '"
					+ nodeType.getName() + "', null);";
			System.out.println(sql);
		}
	}

	private void insertTermTypes(List<TinyClass> termTypes) {
		for (TinyClass termType : termTypes) {
			String sql = "INSERT INTO category_term_type "
					+ "(id, name, description) " + "VALUES " + "('"
					+ termType.getId() + "', '" + termType.getName()
					+ "', null);";
			System.out.println(sql);
		}
	}

	/**
	 * 收集Types
	 * 
	 * @param typeSheet
	 * @param menuTypes
	 * @param urlTypes
	 * @param nodeTypes
	 * @param termTypes
	 * @throws Exception
	 */
	private void collectTypes(XSSFSheet typeSheet, List<TinyClass> termTypes,
			List<TinyClass> nodeTypes, List<TinyClass> urlTypes,
			List<TinyClass> menuTypes) throws Exception {
		for (int i = 2; i <= typeSheet.getLastRowNum(); i++) {
			XSSFRow row = typeSheet.getRow(i);
			if (StringUtils.isNotEmpty(getCellStringValue(row.getCell(0)))) {
				TinyClass termType = new TinyClass(
						getCellStringValue(row.getCell(0)),
						getCellStringValue(row.getCell(1)));
				if (termTypes.contains(termType)) {
					throw new Exception("分类类型，行" + (i + 1) + "有错误，编号重复。");
				} else {
					termTypes.add(termType);
				}
			}
			if (StringUtils.isNotEmpty(getCellStringValue(row.getCell(2)))) {
				TinyClass nodeType = new TinyClass(
						getCellStringValue(row.getCell(2)),
						getCellStringValue(row.getCell(3)));
				if (nodeTypes.contains(nodeType)) {
					throw new Exception("文章类型，行" + (i + 1) + "有错误，编号重复。");
				} else {
					nodeTypes.add(nodeType);
				}
			}
			if (StringUtils.isNotEmpty(getCellStringValue(row.getCell(4)))) {
				TinyClass urlType = new TinyClass(
						getCellStringValue(row.getCell(4)),
						getCellStringValue(row.getCell(5)));
				if (urlTypes.contains(urlType)) {
					throw new Exception("url类型，行" + (i + 1) + "有错误，编号重复。");
				} else {
					urlTypes.add(urlType);
				}
			}
			if (StringUtils.isNotEmpty(getCellStringValue(row.getCell(6)))) {
				TinyClass menuType = new TinyClass(
						getCellStringValue(row.getCell(6)),
						getCellStringValue(row.getCell(7)));
				if (menuTypes.contains(menuType)) {
					throw new Exception("菜单类型，行" + (i + 1) + "有错误，编号重复。");
				} else {
					menuTypes.add(menuType);
				}
			}
		}
	}

	private void insertTerms(List<CategoryTerm> terms) {
		for (CategoryTerm term : terms) {
			String sql = "INSERT INTO category_term "
					+ "(id, name, seq_num, type, pid) " + "VALUES " + "('"
					+ term.getId() + "', '" + term.getName() + "', "
					+ term.getSeqNum() + ", '"
					+ term.getCategoryTermType().getId() + "', #{pid});";
			if (term.getParent() == null) {
				sql = sql.replace("#{pid}", "null");
			} else {
				sql = sql.replace("#{pid}", "'" + term.getParent().getId()
						+ "'");
			}
			System.out.println(sql);
		}
		// 插入单页分类
		String sql = "INSERT INTO category_term "
				+ "(id, name, seq_num, type, pid) " + "VALUES "
				+ "('single_page', '单页', 1024, 'article', null);";
		System.out.println(sql);
	}

	private void insertMenus(List<MenuFromExcel> menus) {
		for (MenuFromExcel menu : menus) {
			String sql = "INSERT INTO menu "
					+ "(id, description, enable, expanded, label, seq_num, url, pid, type, permission, target) "
					+ "VALUES " + "('" + menu.getId() + "', #{description}, '"
					+ menu.getEnable() + "', '" + menu.getExpanded() + "', "
					+ "'" + menu.getLabel() + "', " + menu.getSeqNum() + ", '"
					+ menu.getUrl() + "', #{pid}, '" + menu.getType()
					+ "', #{permission}, '" + menu.getTarget() + "');";
			if (menu.getParent() == null) {
				sql = sql.replace("#{pid}", "null");
			} else {
				sql = sql.replace("#{pid}", "'" + menu.getParent().getId()
						+ "'");
			}
			if (StringUtils.isEmpty(menu.getPermission())) {
				sql = sql.replace("#{permission}", "'MENU_EDIT'");
			} else {
				sql = sql.replace("#{permission}", "'" + menu.getPermission()
						+ "'");
			}
			if (StringUtils.isEmpty(menu.getDescription())) {
				sql = sql.replace("#{description}", "null");
			} else {
				sql = sql.replace("#{description}", "'" + menu.getDescription()
						+ "'");
			}
			System.out.println(sql);
		}
	}

	/**
	 * 排序menus，先父后子
	 * 
	 * @return
	 */
	private List<MenuFromExcel> sortMenus(List<MenuFromExcel> menus) {
		List<MenuFromExcel> newMenus = new ArrayList<MenuFromExcel>();
		for (int i = 1; i <= 3; i++) {// 遍历三级
			for (MenuFromExcel menu : menus) {
				if (menu.getLevel() == i) {
					newMenus.add(menu);
				}
			}
		}
		return newMenus;
	}

	/**
	 * 创建相应的terms，按照先父后子的顺序排序
	 */
	private List<CategoryTerm> createTerms(List<MenuFromExcel> menus) {
		List<CategoryTerm> terms = new ArrayList<CategoryTerm>();
		Map<String, CategoryTerm> termsMap = new HashMap<String, CategoryTerm>();
		// 先创建第一级（parent 为空的）
		for (int i = 1; i <= 3; i++) {// 一共创建三级
			for (MenuFromExcel menu : menus) {
				if (menu.getLevel() == i) {
					if (menu.isParent() || menu.getUrlType().equals("term")) {
						createTerm(menu, termsMap, terms);
					}
				}
			}
		}
		return terms;
	}

	private void createTerm(MenuFromExcel menu,
			Map<String, CategoryTerm> termsMap, List<CategoryTerm> terms) {
		CategoryTerm term = new CategoryTerm();
		term.setId(menu.getId());
		term.setCategoryTermType(new CategoryTermType(menu.getTermType()));
		term.setName(menu.getLabel());
		term.setSeqNum(Integer.valueOf(menu.getSeqNum()));
		if (menu.getLevel() != 1) {
			// pTerm 不可能为空
			CategoryTerm pTerm = termsMap.get(menu.getParent().getId());
			term.setParent(pTerm);
		}
		termsMap.put(term.getId(), term);
		terms.add(term);
	}

	/**
	 * 创建menu树
	 * 
	 * @param menus
	 * @param log
	 */
	private Map<String, MenuFromExcel> buildTree(List<MenuFromExcel> menus,
			GenerateLog log) {
		Map<String, MenuFromExcel> menusMap = new HashMap<String, MenuFromExcel>();
		// id判断重复
		for (MenuFromExcel menu : menus) {
			if (menusMap.containsKey(menu.getId())) {
				log.addErrorLog(menu.getLineNumber() + "与"
						+ menusMap.get(menu.getId()).getLineNumber() + "id重复");
			} else {
				menusMap.put(menu.getId(), menu);
			}
		}
		// 串成树
		for (MenuFromExcel menu : menus) {
			if (StringUtils.isNotEmpty(menu.getPid())) {
				MenuFromExcel parent = menusMap.get(menu.getPid());
				if (parent != null) {
					menu.setParent(parent);
					parent.getChildren().add(menu);
				} else {
					log.addErrorLog(menu.getLineNumber() + "找不到pid"
							+ menu.getPid());
				}
			}
		}
		// 设置在树中的级别
		for (MenuFromExcel menu : menus) {
			menu.setLevel(getLevel(1, menu));
		}
		return menusMap;
	}

	private int getLevel(int level, MenuFromExcel menu) {
		if (menu.getParent() != null) {
			return getLevel((level + 1), menu.getParent());
		} else {
			return level;
		}
	}

	/**
	 * 验证必填信息，填充默认数据
	 * 
	 * @param menus
	 * @param menuTypes
	 * @param urlTypes
	 * @param nodeTypes
	 * @param termTypes
	 */
	private void validateAndFillDefault(List<MenuFromExcel> menus,
			GenerateLog log, List<TinyClass> termTypes,
			List<TinyClass> nodeTypes, List<TinyClass> urlTypes,
			List<TinyClass> menuTypes) {
		for (MenuFromExcel menu : menus) {
			// 验证Id
			if (StringUtils.isEmpty(menu.getId())) {
				log.addErrorLog(menu.getLineNumber() + ": id为空");
			}
			// label
			if (menu.getLabel() == null) {
				menu.setLabel("");
			}
			// urlType
			if (StringUtils.isNotEmpty(menu.getUrlType())) {
				if (!urlTypes.contains(new TinyClass(menu.getUrlType()))) {
					log.addErrorLog(menu.getLineNumber() + ": urlType错误");
				}
			} else {
				menu.setUrlType("spage");
			}
			// termType
			if (StringUtils.isEmpty(menu.getTermType())) {
				menu.setTermType("article");
			} else if (!termTypes.contains(new TinyClass(menu.getTermType()))) {
				log.addErrorLog(menu.getLineNumber() + ": termType错误");
			}
			// type
			if (StringUtils.isEmpty(menu.getType())) {
				menu.setType("MainMenu");
			} else if (!menuTypes.contains(new TinyClass(menu.getType()))) {
				log.addErrorLog(menu.getLineNumber() + ": menuType错误");
			}
			// enable
			if (StringUtils.isEmpty(menu.getEnable())) {
				menu.setEnable("1");
			} else if (!menu.getEnable().equals("1")
					&& !menu.getEnable().equals("0")) {
				log.addErrorLog(menu.getLineNumber() + ": enable错误");
			}
			// seq_num
			if (StringUtils.isEmpty(menu.getSeqNum())) {
				menu.setSeqNum("0");
			}
			// expanded
			if (StringUtils.isEmpty(menu.getExpanded())) {
				menu.setExpanded("0");
			} else if (!menu.getExpanded().equals("1")
					&& !menu.getExpanded().equals("0")) {
				log.addErrorLog(menu.getLineNumber() + ": expanded错误");
			}
			// language
			if (StringUtils.isEmpty(menu.getLanguage())) {
				menu.setLanguage("*");
			}
			// target
			if (StringUtils.isEmpty(menu.getTarget())) {
				menu.setTarget("_self");
			}
		}
	}

	/**
	 * 读取数据，生成menus
	 * 
	 * @param sheet
	 * @return
	 */
	private List<MenuFromExcel> createMenus(XSSFSheet sheet) throws Exception {
		List<MenuFromExcel> menus = new ArrayList<MenuFromExcel>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			MenuFromExcel menu = new MenuFromExcel();
			menu.setLineNumber(String.valueOf(i + 1));
			int cellIndex = 0;
			menu.setId(getCellStringValue(row.getCell(cellIndex++)));
			menu.setLabel(getCellStringValue(row.getCell(cellIndex++)));
			menu.setUrlType(getCellStringValue(row.getCell(cellIndex++)));
			menu.setPid(getCellStringValue(row.getCell(cellIndex++)));
			menu.setTermType(getCellStringValue(row.getCell(cellIndex++)));
			menu.setType(getCellStringValue(row.getCell(cellIndex++)));
			menu.setUrl(getCellStringValue(row.getCell(cellIndex++)));
			menu.setEnable(getCellStringValue(row.getCell(cellIndex++)));
			menu.setPermission(getCellStringValue(row.getCell(cellIndex++)));
			menu.setSeqNum(getCellStringValue(row.getCell(cellIndex++)));
			menu.setDescription(getCellStringValue(row.getCell(cellIndex++)));
			menu.setExpanded(getCellStringValue(row.getCell(cellIndex++)));
			menu.setLanguage(getCellStringValue(row.getCell(cellIndex++)));
			menu.setTarget(getCellStringValue(row.getCell(cellIndex++)));
			menus.add(menu);
		}
		return menus;
	}

	private String getCellStringValue(XSSFCell cell) throws Exception {
		if (cell != null) {
			// 每个单元格必须都是文本格式
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return String.valueOf((int) Math.floor(cell
						.getNumericCellValue()));
			} else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == cell.CELL_TYPE_BLANK) {
				return null;
			} else {
				throw new Exception("单元格只支持数字和文本格式，请检查"
						+ cell.getRow().getSheet().getSheetName() + " 行："
						+ (cell.getRowIndex() + 1) + "  列："
						+ (cell.getColumnIndex() + 1));
			}
		} else {
			return null;
		}
	}

}

class TinyClass {
	private String id;
	private String name;

	public TinyClass(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public TinyClass(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			TinyClass o2 = (TinyClass) obj;
			if (o2.getId() == this.getId() || o2.getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}
}

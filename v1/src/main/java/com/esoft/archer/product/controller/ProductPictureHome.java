package com.esoft.archer.product.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.lucene.model.Indexing;
import com.esoft.archer.lucene.service.LuceneService;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeBody;
import com.esoft.archer.node.model.NodeType;
import com.esoft.archer.product.ProductConstants;
import com.esoft.archer.product.model.Product;
import com.esoft.archer.product.model.ProductPicture;
import com.esoft.archer.product.service.ProductService;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class ProductPictureHome {

	@Logger
	static Log log;

	@Resource
	private ProductService productService;
	
	private static final StringManager sm = StringManager
	.getManager(ProductConstants.Package);

	private List<ProductPicture> productPictures;
	private ProductPicture selectedPicture;

//	private String productId;

	/**
	 * 删除选中的图片
	 * 
	 */
	public void deleteSelectedPicture() {
		if (this.selectedPicture != null) {
			//FIXME:删除以后，不删除上传的图片?
			productService.deleteProductPicture(selectedPicture);
			this.productPictures.remove(selectedPicture);
		}
	}

//	public String getProductId() {
//		return productId;
//	}

	public List<ProductPicture> getProductPictures() {
		return productPictures;
	}

	public ProductPicture getSelectedPicture() {
		return selectedPicture;
	}

	/**
	 * 处理产品图片上传
	 * 
	 * @param event
	 */
	public void handleProductPicturesUpload(FileUploadEvent event) {
		//FIXME:如何对每个产品的图片进行区分？
		UploadedFile uploadFile = event.getFile();
//		String folderPath = "upload" + File.separator + this.getProductId();
		String folderPath = "upload" + File.separator;
//		File folder = new File(FacesUtil.getAppRealPath() + folderPath);
//		if (!folder.exists()) {
//			folder.mkdirs();
//		}
		String filePath = folderPath + File.separator
				+ IdGenerator.randomUUID() + ".jpg";
		String fileRealPath = FacesUtil.getAppRealPath() + filePath;
		try {
			InputStream iptS = uploadFile.getInputstream();
			FileOutputStream foptS = new FileOutputStream(fileRealPath);
			OutputStream optS = (OutputStream) foptS;

			int c;
			while ((c = iptS.read()) != -1) {
				optS.write(c);
			}
			optS.flush();
			ProductPicture pp = new ProductPicture();
			pp.setId(IdGenerator.randomUUID());
			pp.setPicture(filePath.replace('\\', '/'));
			this.getProductPictures().add(pp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FacesUtil.addInfoMessage(event.getFile().getFileName() + sm.getString("uploadSuccess"));
	}

	public void initProductPictures(List value) {
		this.productPictures = new ArrayList<ProductPicture>();
		this.productPictures.addAll(value);
	}

	/**
	 * 下一张
	 * 
	 */
	public void nextPicture() {
		int index = productPictures.indexOf(selectedPicture);
		if (index == productPictures.size() - 1) {
			index = 0;
		} else {
			index++;
		}
		this.selectedPicture = productPictures.get(index);
	}

	/**
	 * 上一张
	 * 
	 */
	public void previousPicture() {
		int index = productPictures.indexOf(selectedPicture);
		if (index == 0) {
			index = productPictures.size() - 1;
		} else {
			index--;
		}
		this.selectedPicture = productPictures.get(index);
	}

//	public void setProductId(String productId) {
//		this.productId = productId;
//	}

	public void setProductPictures(List<ProductPicture> productPictures) {
		this.productPictures = productPictures;
	}

	public void setSelectedPicture(ProductPicture selectedPicture) {
		this.selectedPicture = selectedPicture;
	}
}

package com.esoft.archer.product.service;

import com.esoft.archer.product.model.Product;
import com.esoft.archer.product.model.ProductPicture;

public interface ProductService {
	public void save(Product product);
	public void deleteProductPicture(ProductPicture productPicture);

	public ProductPicture addProductPicture(String productId, String picture);
	public void saveProductPicture(ProductPicture pp);

	public void deleteProductPicture(String productPictureId);
}

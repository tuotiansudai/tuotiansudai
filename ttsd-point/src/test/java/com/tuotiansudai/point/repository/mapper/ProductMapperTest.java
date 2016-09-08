package com.tuotiansudai.point.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ProductMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;


    @Test
    public void shouldCreateProductModel() throws Exception {
        UserModel fakeUserModel = this.createFakeUserModel();

        ProductModel productModel = new ProductModel(GoodsType.VIRTUAL, "50元充值卡", 1, "upload/images/11.png", "50yuan", 100, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, fakeUserModel.getLoginName(), new Date());

        productMapper.create(productModel);

        List<ProductModel> productModelList = productMapper.findAllProducts(GoodsType.VIRTUAL, 0, 10);

        assertThat(productModelList.size(), is(1));

        long productCount = productMapper.findAllProductsCount(GoodsType.VIRTUAL);

        assertThat(productCount, is(1L));

    }

    private UserModel createFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("productCreateUser");
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }

    @Test
    public void shouldFindAllProductsByGoodsTypeIsOk(){
        UserModel fakeUserModel = this.createFakeUserModel();
        ProductModel productModel1 = new ProductModel(GoodsType.VIRTUAL, "50元充值卡", 1, "upload/images/11.png", "50yuan", 100, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, fakeUserModel.getLoginName(), new Date());
        ProductModel productModel2 = new ProductModel(GoodsType.COUPON, "50元充值卡", 1, "upload/images/11.png", "50yuan", 100, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, fakeUserModel.getLoginName(), new Date());
        ProductModel productModel3 = new ProductModel(GoodsType.PHYSICAL, "50元充值卡", 1, "upload/images/11.png", "50yuan", 100, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, fakeUserModel.getLoginName(), new Date());
        productMapper.create(productModel1);
        productMapper.create(productModel2);
        productMapper.create(productModel3);
        List<ProductModel> virtualList = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.COUPON, GoodsType.VIRTUAL), 0, 10);
        List<ProductModel> physicalList= productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.PHYSICAL), 0, 10);
        assertEquals(virtualList.size(),2);
        assertEquals(physicalList.size(),1);
    }

}

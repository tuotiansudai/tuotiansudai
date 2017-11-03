package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.ProductOrderModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ProductOrderMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;


    @Test
    public void shouldCreateProductOrderModel() throws Exception {
        String fakeLoginName = "productorderCreateUser";

        ProductModel productModel = this.createFakeProductModel(fakeLoginName);

        ProductOrderModel productOrderModel = new ProductOrderModel();
        productOrderModel.setId(10001);
        productOrderModel.setProductId(productModel.getId());
        productOrderModel.setPoints(2000);
        productOrderModel.setNum(2);
        productOrderModel.setTotalPoints(4000);
        productOrderModel.setConsignment(false);
        productOrderModel.setContact("张山");
        productOrderModel.setMobile("13999999999");
        productOrderModel.setAddress("北京市北京市");
        productOrderModel.setConsignment(true);
        productOrderModel.setCreatedTime(new Date());
        productOrderModel.setCreatedBy(fakeLoginName);
        productOrderModel.setCreatedBy(fakeLoginName);

        productOrderMapper.create(productOrderModel);

        List<ProductOrderModel> productOrderModelList = productOrderMapper.findProductOrderList(productModel.getId(), null, 0, 10);

        assertThat(productOrderModelList.size() > 0, is(true));
    }

    private ProductModel createFakeProductModel(String loginName) {
        Date laterDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));
        ProductModel fakeProductModel = new ProductModel();
        fakeProductModel.setId(100001);
        fakeProductModel.setType(GoodsType.VIRTUAL);
        fakeProductModel.setName("虚拟商品");
        fakeProductModel.setDescription("充值卡");
        fakeProductModel.setActive(true);
        fakeProductModel.setCreatedBy(loginName);
        fakeProductModel.setCreatedTime(new Date());
        fakeProductModel.setStartTime(new Date());
        fakeProductModel.setEndTime(laterDate);
        fakeProductModel.setTotalCount(100);
        fakeProductModel.setUsedCount(0);
        fakeProductModel.setImageUrl("upload/images/a.png");
        fakeProductModel.setPoints(2000);
        fakeProductModel.setSeq(1);
        productMapper.create(fakeProductModel);
        return fakeProductModel;
    }


}

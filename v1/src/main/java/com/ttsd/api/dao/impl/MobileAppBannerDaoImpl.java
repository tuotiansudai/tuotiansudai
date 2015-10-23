package com.ttsd.api.dao.impl;

import com.esoft.archer.banner.model.BannerPicture;
import com.ttsd.api.dao.MobileAppBannerDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Repository(value = "MobileAppBannerDaoImpl")
public class MobileAppBannerDaoImpl implements MobileAppBannerDao {
    @Resource
    private HibernateTemplate ht;

    @Override
    public List<BannerPicture> getAppBanner() {
        String hql = "select * from banner_picture where banner_id = ? order by seq_num asc";
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        sqlQuery.addEntity(BannerPicture.class);
        sqlQuery.setParameter(0, "index_mobile");
        List<BannerPicture> bannerPictureList = sqlQuery.list();
        return bannerPictureList;
    }

}

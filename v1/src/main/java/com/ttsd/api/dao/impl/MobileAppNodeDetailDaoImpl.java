package com.ttsd.api.dao.impl;

import com.esoft.archer.node.model.Node;
import com.ttsd.api.dao.MobileAppNodeDetailDao;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MobileAppNodeDetailDaoImpl implements MobileAppNodeDetailDao {

    @Resource
    private HibernateTemplate ht;
    @Override
    public Node getNodeById(String nodeId) {
        return ht.get(Node.class, nodeId);
    }
}

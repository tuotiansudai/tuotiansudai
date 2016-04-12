package com.ttsd.api.dao;

import com.esoft.archer.node.model.Node;

import java.util.List;

public interface MobileAppNodeListDao {

    Integer getTotalCount(String termId);

    List<Node> getNodeList(Integer index, Integer pageSize, String termId);
}

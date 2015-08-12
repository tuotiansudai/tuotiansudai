package com.ttsd.api.dao.impl;

import com.esoft.archer.node.model.Node;
import com.ttsd.api.dao.MobileAppNodeListDao;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppNodeListDaoImpl implements MobileAppNodeListDao {

    @Resource
    private HibernateTemplate ht;

    @Override
    public Integer getTotalCount(String termId) {
        String sql = "select count(*) from node " +
                "inner join category_term_node tn on tn.node_id = node.id " +
                "inner join category_term term on tn.term_id = term.id " +
                "where term_id = ?";

        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, termId);
        Integer count = ((Number) sqlQuery.uniqueResult()).intValue();
        return count;
    }

    @Override
    public List<Node> getNodeList(Integer index, Integer pageSize, String termId) {
        String sql = "Select node from Node node, CategoryTerm term "
                + " where node.status = 1 and node in elements(term.nodes) and term.id=:termId"
                + " order by node.createTime desc";
        Query query = ht.getSessionFactory().getCurrentSession().createQuery(sql);
        query.setParameter("termId", termId);
        query.setMaxResults(pageSize);
        query.setFirstResult((index - 1) * pageSize);
        List<Node> nodeList = query.list();
        return nodeList;
    }
}

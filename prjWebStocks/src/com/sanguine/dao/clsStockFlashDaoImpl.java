package com.sanguine.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("clsStockFlashDao")
public class clsStockFlashDaoImpl implements clsStockFlashDao {
	@Autowired
	private SessionFactory sessionFactory;

	public List funGetStockFlashData(String sql, String clientCode, String userCode) {
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setParameter("clientCode", clientCode);
		query.setParameter("userCode", userCode);
		List list = query.list();
		return list;
	}

}

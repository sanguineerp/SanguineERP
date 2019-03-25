package com.sanguine.webbooks.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbooks.model.clsACSubGroupMasterModel;
import com.sanguine.webbooks.model.clsACSubGroupMasterModel_ID;

@Repository("clsACSubGroupMasterDao")
public class clsACSubGroupMasterDaoImpl implements clsACSubGroupMasterDao{

	@Autowired
	private SessionFactory webBooksSessionFactory;

	@Override
	public void funAddUpdateACSubGroupMaster(clsACSubGroupMasterModel objMaster){
		webBooksSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	public clsACSubGroupMasterModel funGetACSubGroupMaster(String docCode,String clientCode){
		return (clsACSubGroupMasterModel) webBooksSessionFactory.getCurrentSession().get(clsACSubGroupMasterModel.class,new clsACSubGroupMasterModel_ID(docCode,clientCode));
	}

	@Override
	public List funGetWebBooksSubGroupMaster(String subGroupCode, String clientCode) {
		List list = null;
		try {
			Query query = webBooksSessionFactory.getCurrentSession().createQuery("from clsACSubGroupMasterModel a,clsACGroupMasterModel b " + "where a.strGroupCode=b.strGroupCode  and a.strClientCode=b.strClientCode and a.strClientCode=:clientCode " + "and a.strSubGroupCode=:subGroupCode ");

			query.setParameter("subGroupCode", subGroupCode);
			query.setParameter("clientCode", clientCode);

			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


}

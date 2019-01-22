package com.sanguine.webbooks.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbooks.model.clsJVHdModel;
import com.sanguine.webbooks.model.clsPettyCashEntryHdModel;
@Repository("clsPettyCashDao")
public class clsPettyCashDaoImpl implements clsPettyCashDao {

	@Autowired
	private SessionFactory webBooksSessionFactory;

	@Override
	@Transactional(value = "WebBooksTransactionManager")
	public void funAddUpdatePettyHd(clsPettyCashEntryHdModel objHdModel) {
		webBooksSessionFactory.getCurrentSession().saveOrUpdate(objHdModel);
	}
	
	@Override
	public clsPettyCashEntryHdModel funGetPettyList(String vouchNo, String clientCode, String propertyCode) {
		Criteria cr = webBooksSessionFactory.getCurrentSession().createCriteria(clsPettyCashEntryHdModel.class);
		cr.add(Restrictions.eq("strVouchNo", vouchNo));
		cr.add(Restrictions.eq("strClientCode", clientCode));
		
		List list = cr.list();

		clsPettyCashEntryHdModel objModel = null;
		if (list.size() > 0) {
			objModel = (clsPettyCashEntryHdModel) list.get(0);
		}

		return objModel;
	}
}

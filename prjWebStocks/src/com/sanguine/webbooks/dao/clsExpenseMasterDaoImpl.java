package com.sanguine.webbooks.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.webbooks.model.clsAccountHolderMasterModel;
import com.sanguine.webbooks.model.clsAccountHolderMasterModel_ID;
import com.sanguine.webbooks.model.clsExpenseMasterModel;
import com.sanguine.webbooks.model.clsExpenseMasterModel_ID;


@Repository("clsExpenseMasterDaoImpl")
public class clsExpenseMasterDaoImpl implements clsExpensMasterDao{

	@Autowired
	private SessionFactory webBooksSessionFactory;
	@Override
	public void funAddExpense(clsExpenseMasterModel exp) {

		webBooksSessionFactory.getCurrentSession().saveOrUpdate(exp);
	}
	
	
	@Override
	public clsExpenseMasterModel funGetExpense(String expCode, String clientCode) {
		return (clsExpenseMasterModel) webBooksSessionFactory.getCurrentSession().get(clsExpenseMasterModel.class, new clsExpenseMasterModel_ID(expCode, clientCode));
	}


}

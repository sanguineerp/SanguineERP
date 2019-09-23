package com.sanguine.webclub.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel;
import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel_ID;

@Repository("clsWebClubBusinessSourceMasterDao")
@Transactional(value = "WebClubTransactionManager")
public class clsWebClubBusinessSourceMasterDaoImpl implements clsWebClubBusinessSourceMasterDao{

	@Autowired
	private SessionFactory WebClubSessionFactory;

	@Override
	public void funAddUpdateWebClubBusinessSourceMaster(clsWebClubBusinessSourceMasterModel objMaster){
		WebClubSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	public clsWebClubBusinessSourceMasterModel funGetWebClubBusinessSourceMaster(String docCode,String clientCode){
		return (clsWebClubBusinessSourceMasterModel) WebClubSessionFactory.getCurrentSession().get(clsWebClubBusinessSourceMasterModel.class,new clsWebClubBusinessSourceMasterModel_ID(docCode,clientCode));
	}


}

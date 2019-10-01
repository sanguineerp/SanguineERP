package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsFunctionMasterModel;
import com.sanguine.webbanquets.model.clsFunctionMasterModel_ID;

@Repository("clsFunctionMasterDao")
public class clsFunctionMasterDaoImpl implements clsFunctionMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateFunctionMaster(clsFunctionMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsFunctionMasterModel funGetFunctionMaster(String docCode,String clientCode){
		return (clsFunctionMasterModel) webPMSSessionFactory.getCurrentSession().get(clsFunctionMasterModel.class,new clsFunctionMasterModel_ID(docCode,clientCode));
	}

	

}

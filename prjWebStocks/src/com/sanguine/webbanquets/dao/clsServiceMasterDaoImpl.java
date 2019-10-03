package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsServiceMasterModel;
import com.sanguine.webbanquets.model.clsServiceMasterModel_ID;

@Repository("clsServiceMasterDao")
public class clsServiceMasterDaoImpl implements clsServiceMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateServiceMaster(clsServiceMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsServiceMasterModel funGetServiceMaster(String docCode,String clientCode){
		return (clsServiceMasterModel) webPMSSessionFactory.getCurrentSession().get(clsServiceMasterModel.class,new clsServiceMasterModel_ID(docCode,clientCode));
	}




}

package com.sanguine.webpms.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.model.clsHouseKeepingMasterModel;
import com.sanguine.webpms.model.clsHouseKeepingMasterModel_ID;

@Repository("clsHouseKeepingMasterDao")
public class clsHouseKeepingMasterDaoImpl implements clsHouseKeepingMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateHouseKeepingMaster(clsHouseKeepingMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsHouseKeepingMasterModel funGetHouseKeepingMaster(String docCode,String clientCode){
		return (clsHouseKeepingMasterModel) webPMSSessionFactory.getCurrentSession().get(clsHouseKeepingMasterModel.class,new clsHouseKeepingMasterModel_ID(docCode,clientCode));
	}


}

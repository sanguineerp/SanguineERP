package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel_ID;

@Repository("clsMenuHeadMasterDao")
public class clsMenuHeadMasterDaoImpl implements clsMenuHeadMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdatefrmMenuHeadMaster(clsMenuHeadMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsMenuHeadMasterModel funGetfrmMenuHeadMaster(String docCode,String clientCode){
		return (clsMenuHeadMasterModel) webPMSSessionFactory.getCurrentSession().get(clsMenuHeadMasterModel.class,new clsMenuHeadMasterModel_ID(docCode,clientCode));
	}


}

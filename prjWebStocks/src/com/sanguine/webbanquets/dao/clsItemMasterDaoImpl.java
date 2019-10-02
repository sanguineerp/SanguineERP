package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsItemMasterModel;
import com.sanguine.webbanquets.model.clsItemMasterModel_ID;

@Repository("clsItemMasterDao")
public class clsItemMasterDaoImpl implements clsItemMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateItemMaster(clsItemMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsItemMasterModel funGetItemMaster(String docCode,String clientCode){
		return (clsItemMasterModel) webPMSSessionFactory.getCurrentSession().get(clsItemMasterModel.class,new clsItemMasterModel_ID(docCode,clientCode));
	}


}

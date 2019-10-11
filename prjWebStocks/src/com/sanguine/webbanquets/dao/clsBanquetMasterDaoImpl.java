package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetMasterModel;
import com.sanguine.webbanquets.model.clsBanquetMasterModel_ID;

@Repository("clsBanquetMasterDao")
public class clsBanquetMasterDaoImpl implements clsBanquetMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetMaster(clsBanquetMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetMasterModel funGetBanquetMaster(String docCode,String clientCode){
		return (clsBanquetMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetMasterModel.class,new clsBanquetMasterModel_ID(docCode,clientCode));
	}


}

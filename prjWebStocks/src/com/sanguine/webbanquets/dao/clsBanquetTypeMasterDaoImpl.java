package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel;
import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel_ID;

@Repository("clsBanquetTypeMasterDao")
public class clsBanquetTypeMasterDaoImpl implements clsBanquetTypeMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetTypeMaster(clsBanquetTypeMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetTypeMasterModel funGetBanquetTypeMaster(String docCode,String clientCode){
		return (clsBanquetTypeMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetTypeMasterModel.class,new clsBanquetTypeMasterModel_ID(docCode,clientCode));
	}


}

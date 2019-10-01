package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsCostCenterMasterModel;
import com.sanguine.webbanquets.model.clsCostCenterMasterModel_ID;

@Repository("clsCostCenterMasterDao")
public class clsCostCenterMasterDaoImpl implements clsCostCenterMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateCostCenterMaster(clsCostCenterMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsCostCenterMasterModel funGetCostCenterMaster(String docCode,String clientCode){
		return (clsCostCenterMasterModel) webPMSSessionFactory.getCurrentSession().get(clsCostCenterMasterModel.class,new clsCostCenterMasterModel_ID(docCode,clientCode));
	}


}

package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.model.clsLocationMasterModel_ID;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel_ID;

@Repository("clsBanquetStaffMasterDao")
public class clsBanquetStaffMasterDaoImpl implements clsBanquetStaffMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetStaffMaster(clsBanquetStaffMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetStaffMasterModel funGetBanquetStaffMaster(String docCode,String clientCode){
		return (clsBanquetStaffMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetStaffMasterModel.class,new clsBanquetStaffMasterModel_ID(docCode,clientCode));
	}

	public clsBanquetStaffMasterModel funGetObject(String code, String clientCode) {
		return (clsBanquetStaffMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetStaffMasterModel.class, new clsBanquetStaffMasterModel_ID(code, clientCode));
	}
}

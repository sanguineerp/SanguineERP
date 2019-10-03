package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel_ID;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel_ID;

@Repository("clsBanquetStaffCategeoryMasterDao")
public class clsBanquetStaffCategeoryMasterDaoImpl implements clsBanquetStaffCategeoryMasterDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetStaffCategeoryMaster(clsBanquetStaffCategeoryMasterModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetStaffCategeoryMasterModel funGetBanquetStaffCategeoryMaster(String docCode,String clientCode){
		return (clsBanquetStaffCategeoryMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetStaffCategeoryMasterModel.class,new clsBanquetStaffCategeoryMasterModel_ID(docCode,clientCode));
	}

	public clsBanquetStaffCategeoryMasterModel funGetObject(String code, String clientCode) {
		return (clsBanquetStaffCategeoryMasterModel) webPMSSessionFactory.getCurrentSession().get(clsBanquetStaffCategeoryMasterModel.class, new clsBanquetStaffCategeoryMasterModel_ID(code, clientCode));
	}
}

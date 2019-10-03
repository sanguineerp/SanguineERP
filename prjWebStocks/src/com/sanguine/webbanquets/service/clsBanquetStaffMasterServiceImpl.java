package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.webbanquets.dao.clsBanquetStaffMasterDao;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;

@Service("clsBanquetStaffMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetStaffMasterServiceImpl implements clsBanquetStaffMasterService{
	@Autowired
	private clsBanquetStaffMasterDao objBanquetStaffMasterDao;

	@Override
	public void funAddUpdateBanquetStaffMaster(clsBanquetStaffMasterModel objMaster){
		objBanquetStaffMasterDao.funAddUpdateBanquetStaffMaster(objMaster);
	}

	@Override
	public clsBanquetStaffMasterModel funGetBanquetStaffMaster(String docCode,String clientCode){
		return objBanquetStaffMasterDao.funGetBanquetStaffMaster(docCode,clientCode);
	}

	
	public clsBanquetStaffMasterModel funGetObject(String code, String clientCode) {
		return objBanquetStaffMasterDao.funGetObject(code, clientCode);
	}



}

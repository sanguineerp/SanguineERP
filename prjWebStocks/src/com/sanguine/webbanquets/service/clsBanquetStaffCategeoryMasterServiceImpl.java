package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsBanquetStaffCategeoryMasterDao;
import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;

@Service("clsBanquetStaffCategeoryMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetStaffCategeoryMasterServiceImpl implements clsBanquetStaffCategeoryMasterService{
	@Autowired
	private clsBanquetStaffCategeoryMasterDao objBanquetStaffCategeoryMasterDao;

	@Override
	public void funAddUpdateBanquetStaffCategeoryMaster(clsBanquetStaffCategeoryMasterModel objMaster){
		objBanquetStaffCategeoryMasterDao.funAddUpdateBanquetStaffCategeoryMaster(objMaster);
	}

	@Override
	public clsBanquetStaffCategeoryMasterModel funGetBanquetStaffCategeoryMaster(String docCode,String clientCode){
		return objBanquetStaffCategeoryMasterDao.funGetBanquetStaffCategeoryMaster(docCode,clientCode);
	}

	public clsBanquetStaffCategeoryMasterModel funGetObject(String code, String clientCode) {
		return objBanquetStaffCategeoryMasterDao.funGetObject(code, clientCode);
	}

}

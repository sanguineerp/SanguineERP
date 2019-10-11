package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsBanquetMasterDao;
import com.sanguine.webbanquets.model.clsBanquetMasterModel;
import org.springframework.transaction.annotation.Propagation;

@Service("clsBanquetMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetMasterServiceImpl implements clsBanquetMasterService{
	@Autowired
	private clsBanquetMasterDao objBanquetMasterDao;

	@Override
	public void funAddUpdateBanquetMaster(clsBanquetMasterModel objMaster){
		objBanquetMasterDao.funAddUpdateBanquetMaster(objMaster);
	}

	@Override
	public clsBanquetMasterModel funGetBanquetMaster(String docCode,String clientCode){
		return objBanquetMasterDao.funGetBanquetMaster(docCode,clientCode);
	}



}

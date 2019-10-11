package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsBanquetTypeMasterDao;
import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel;
import org.springframework.transaction.annotation.Propagation;


@Service("clsBanquetTypeMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetTypeMasterServiceImpl implements clsBanquetTypeMasterService{
	@Autowired
	private clsBanquetTypeMasterDao objBanquetTypeMasterDao;

	@Override
	public void funAddUpdateBanquetTypeMaster(clsBanquetTypeMasterModel objMaster){
		objBanquetTypeMasterDao.funAddUpdateBanquetTypeMaster(objMaster);
	}

	@Override
	public clsBanquetTypeMasterModel funGetBanquetTypeMaster(String docCode,String clientCode){
		return objBanquetTypeMasterDao.funGetBanquetTypeMaster(docCode,clientCode);
	}



}

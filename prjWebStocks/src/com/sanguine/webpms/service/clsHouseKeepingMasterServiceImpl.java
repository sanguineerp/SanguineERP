package com.sanguine.webpms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.dao.clsHouseKeepingMasterDao;
import com.sanguine.webpms.model.clsHouseKeepingMasterModel;
import org.springframework.transaction.annotation.Propagation;


@Service("clsHouseKeepingMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsHouseKeepingMasterServiceImpl implements clsHouseKeepingMasterService{
	@Autowired
	private clsHouseKeepingMasterDao objHouseKeepingMasterDao;

	@Override
	public void funAddUpdateHouseKeepingMaster(clsHouseKeepingMasterModel objMaster){
		objHouseKeepingMasterDao.funAddUpdateHouseKeepingMaster(objMaster);
	}

	@Override
	public clsHouseKeepingMasterModel funGetHouseKeepingMaster(String docCode,String clientCode){
		return objHouseKeepingMasterDao.funGetHouseKeepingMaster(docCode,clientCode);
	}



}

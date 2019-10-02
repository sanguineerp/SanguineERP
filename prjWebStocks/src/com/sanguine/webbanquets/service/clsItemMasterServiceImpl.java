package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.sanguine.webbanquets.dao.clsItemMasterDao;
import com.sanguine.webbanquets.model.clsItemMasterModel;

@Service("clsItemMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsItemMasterServiceImpl implements clsItemMasterService{
	@Autowired
	private clsItemMasterDao objItemMasterDao;

	@Override
	public void funAddUpdateItemMaster(clsItemMasterModel objMaster){
		objItemMasterDao.funAddUpdateItemMaster(objMaster);
	}

	@Override
	public clsItemMasterModel funGetItemMaster(String docCode,String clientCode){
		return objItemMasterDao.funGetItemMaster(docCode,clientCode);
	}



}

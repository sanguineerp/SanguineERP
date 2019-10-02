package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.sanguine.webbanquets.dao.clsMenuHeadMasterDao;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;

@Service("clsMenuHeadMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsMenuHeadMasterServiceImpl implements clsMenuHeadMasterService{
	@Autowired
	private clsMenuHeadMasterDao objfrmMenuHeadMasterDao;

	@Override
	public void funAddUpdatefrmMenuHeadMaster(clsMenuHeadMasterModel objMaster){
		objfrmMenuHeadMasterDao.funAddUpdatefrmMenuHeadMaster(objMaster);
	}

	@Override
	public clsMenuHeadMasterModel funGetfrmMenuHeadMaster(String docCode,String clientCode){
		return objfrmMenuHeadMasterDao.funGetfrmMenuHeadMaster(docCode,clientCode);
	}



}

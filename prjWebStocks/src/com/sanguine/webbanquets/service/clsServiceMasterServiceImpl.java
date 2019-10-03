package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsServiceMasterDao;
import com.sanguine.webbanquets.model.clsServiceMasterModel;

@Service("clsServiceMasterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class clsServiceMasterServiceImpl implements clsServiceMasterService{
	@Autowired
	private clsServiceMasterDao objServiceMasterDao;

	@Override
	public void funAddUpdateServiceMaster(clsServiceMasterModel objMaster){
		objServiceMasterDao.funAddUpdateServiceMaster(objMaster);
	}

	@Override
	public clsServiceMasterModel funGetServiceMaster(String docCode,String clientCode){
		return objServiceMasterDao.funGetServiceMaster(docCode,clientCode);
	}

	


}

package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanguine.webbanquets.dao.clsFunctionMasterDao;
import com.sanguine.webbanquets.model.clsFunctionMasterModel;

@Service("clsFunctionMasterService")
//@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "OtherTransactionManager")
public class clsFunctionMasterServiceImpl implements clsFunctionMasterService{
	@Autowired
	private clsFunctionMasterDao objFunctionMasterDao;

	@Override
	public void funAddUpdateFunctionMaster(clsFunctionMasterModel objMaster){
		objFunctionMasterDao.funAddUpdateFunctionMaster(objMaster);
	}

	@Override
	public clsFunctionMasterModel funGetFunctionMaster(String docCode,String clientCode){
		return objFunctionMasterDao.funGetFunctionMaster(docCode,clientCode);
	}

	


}

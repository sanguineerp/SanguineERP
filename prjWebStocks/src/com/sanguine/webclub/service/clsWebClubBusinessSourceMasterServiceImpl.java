package com.sanguine.webclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.dao.clsWebClubBusinessSourceMasterDao;
import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel;

@Service("clsWebClubBusinessSourceMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebClubTransactionManager")
public class clsWebClubBusinessSourceMasterServiceImpl implements clsWebClubBusinessSourceMasterService{
	@Autowired
	private clsWebClubBusinessSourceMasterDao objWebClubBusinessSourceMasterDao;

	@Override
	public void funAddUpdateWebClubBusinessSourceMaster(clsWebClubBusinessSourceMasterModel objMaster){
		objWebClubBusinessSourceMasterDao.funAddUpdateWebClubBusinessSourceMaster(objMaster);
	}

	@Override
	public clsWebClubBusinessSourceMasterModel funGetWebClubBusinessSourceMaster(String docCode,String clientCode){
		return objWebClubBusinessSourceMasterDao.funGetWebClubBusinessSourceMaster(docCode,clientCode);
	}



}

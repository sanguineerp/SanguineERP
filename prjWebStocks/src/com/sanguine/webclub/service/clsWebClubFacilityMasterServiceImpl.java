package com.sanguine.webclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.dao.clsWebClubFacilityMasterDao;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;

@Service("clsWebClubFacilityMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebClubTransactionManager")
public class clsWebClubFacilityMasterServiceImpl implements clsWebClubFacilityMasterService{
	@Autowired
	private clsWebClubFacilityMasterDao objWebClubFacilityMasterDao;

	@Override
	public void funAddUpdateWebClubFacilityMaster(clsWebClubFacilityMasterModel objMaster){
		objWebClubFacilityMasterDao.funAddUpdateWebClubFacilityMaster(objMaster);
	}

	@Override
	public clsWebClubFacilityMasterModel funGetWebClubFacilityMaster(String docCode,String clientCode){
		return objWebClubFacilityMasterDao.funGetWebClubFacilityMaster(docCode,clientCode);
	}



}

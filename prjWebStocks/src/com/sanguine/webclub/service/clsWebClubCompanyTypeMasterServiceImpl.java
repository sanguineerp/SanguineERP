package com.sanguine.webclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.dao.clsWebClubCompanyTypeMasterDao;
import com.sanguine.webclub.model.clsWebClubCompanyTypeMasterModel;

@Service("clsWebClubCompanyTypeMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebClubTransactionManager")
public class clsWebClubCompanyTypeMasterServiceImpl implements clsWebClubCompanyTypeMasterService {
	@Autowired
	private clsWebClubCompanyTypeMasterDao objWebClubCompanyTypeMasterDao;

	@Override
	public void funAddUpdateWebClubCompanyTypeMaster(clsWebClubCompanyTypeMasterModel objMaster) {
		objWebClubCompanyTypeMasterDao.funAddUpdateWebClubCompanyTypeMaster(objMaster);
	}

	@Override
	public clsWebClubCompanyTypeMasterModel funGetWebClubCompanyTypeMaster(String docCode, String clientCode) {
		return objWebClubCompanyTypeMasterDao.funGetWebClubCompanyTypeMaster(docCode, clientCode);
	}

}

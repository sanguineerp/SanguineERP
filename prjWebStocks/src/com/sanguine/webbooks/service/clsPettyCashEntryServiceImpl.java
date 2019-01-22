package com.sanguine.webbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbooks.dao.clsPettyCashDao;
import com.sanguine.webbooks.model.clsPettyCashEntryHdModel;

@Service("clsPettyCashEntryService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebBooksTransactionManager")
public class clsPettyCashEntryServiceImpl implements clsPettyCashEntryService{

	@Autowired
	private clsPettyCashDao objDao;

	@Override
	public void funAddUpdatePettyHd(clsPettyCashEntryHdModel objHdModel) {
		objDao.funAddUpdatePettyHd(objHdModel);
	}
	
	
	@Override
	public clsPettyCashEntryHdModel funGetPettyList(String vouchNo, String clientCode, String propertyCode) {
		return objDao.funGetPettyList(vouchNo, clientCode, propertyCode);
	}
		
	
}

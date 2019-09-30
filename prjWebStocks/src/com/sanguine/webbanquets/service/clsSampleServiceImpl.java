package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsSampleDao;

@Service("clsSampleService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class clsSampleServiceImpl implements clsSampleService{

	@Autowired
	clsSampleDao objSampleDao; 
	@Override
	public void funSaveData(String data) {
		// TODO Auto-generated method stub
		
		
		objSampleDao.funSaveData(data);
	}

}

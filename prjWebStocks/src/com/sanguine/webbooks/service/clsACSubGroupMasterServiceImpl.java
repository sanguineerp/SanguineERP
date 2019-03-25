package com.sanguine.webbooks.service;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbooks.dao.clsACSubGroupMasterDao;
import com.sanguine.webbooks.model.clsACSubGroupMasterModel;

@Service("clsACSubGroupMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebBooksTransactionManager")
public class clsACSubGroupMasterServiceImpl implements clsACSubGroupMasterService{
	@Autowired
	private clsACSubGroupMasterDao objACSubGroupMasterDao;

	@Override
	public void funAddUpdateACSubGroupMaster(clsACSubGroupMasterModel objMaster){
		objACSubGroupMasterDao.funAddUpdateACSubGroupMaster(objMaster);
	}

	@Override
	public clsACSubGroupMasterModel funGetACSubGroupMaster(String docCode,String clientCode){
		return objACSubGroupMasterDao.funGetACSubGroupMaster(docCode,clientCode);
	}

	@Override
	public List funGetWebBooksSubGroupMaster(String subGroupCode, String clientCode){
		return objACSubGroupMasterDao.funGetWebBooksSubGroupMaster( subGroupCode,  clientCode);
	}


}

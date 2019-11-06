package com.sanguine.webclub.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.bean.clsWebClubPDCBean;
import com.sanguine.webclub.dao.clsWebClubFacilityMasterDao;
import com.sanguine.webclub.dao.clsWebClubPDCDao;
import com.sanguine.webclub.model.clsWebClubPDCModel;

@Service("clsWebClubPDCService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebClubTransactionManager")
public class clsWebClubPDCServiceImpl implements clsWebClubPDCService{
	
	@Autowired
	private clsWebClubPDCDao objWebClubPDCDao;


	@Override
	public void funAddUpdateWebClubPDC(clsWebClubPDCModel objMaster){
		objWebClubPDCDao.funAddUpdateWebClubPDC(objMaster);
	}
	
	@Override
	public void funUpdateWebClubPDC(clsWebClubPDCModel objMaster){
		objWebClubPDCDao.funUpdateWebClubPDC(objMaster);
	}

	@Override
	public List funGetWebClubPDC(String memCode,String clientCode){
		return objWebClubPDCDao.funGetWebClubPDC(memCode, clientCode);
	}

	
	@Override
	public void funDeleteDtlRecieved(String memCode, String clientCode){
		objWebClubPDCDao.funDeleteDtlRecieved(memCode, clientCode);
	}

	@Override
	public void funDeleteDtlIssued(String memCode, String clientCode){
		objWebClubPDCDao.funDeleteDtlIssued(memCode, clientCode);
	}
}

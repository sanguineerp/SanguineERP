package com.sanguine.webclub.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.dao.clsWebClubMemberCategoryMasterDao;
import com.sanguine.webclub.model.clsWebClubCategeoryWiseFacilityModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel;

@Service("clsWebClubMemberCategoryMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebClubTransactionManager")
public class clsWebClubMemberCategoryMasterServiceImpl implements clsWebClubMemberCategoryMasterService {
	@Autowired
	private clsWebClubMemberCategoryMasterDao objWebClubMemberCategoryMasterDao;

	@Autowired
	private SessionFactory WebClubSessionFactory;
	
	@Override
	public void funAddUpdateWebClubMemberCategoryMaster(clsWebClubMemberCategoryMasterModel objMaster) {
		objWebClubMemberCategoryMasterDao.funAddUpdateWebClubMemberCategoryMaster(objMaster);
	}
	
	@Override	
	public clsWebClubMemberCategoryMasterModel funGetWebClubMemberCategoryMaster(String docCode, String clientCode) {
		return objWebClubMemberCategoryMasterDao.funGetWebClubMemberCategoryMaster(docCode, clientCode);
	}

	@Override
	public void funGetCategoryWiseFacilityDtl(String strCatCode,String strClientCode) {
		 objWebClubMemberCategoryMasterDao.funGetCategoryWiseFacilityDtl(strCatCode,strClientCode);

	}
	
	
	@Override
	public void funAddUpdateWebClubCategeoryWiseFacility(clsWebClubCategeoryWiseFacilityModel objMaster) {
		objWebClubMemberCategoryMasterDao.funAddUpdateWebClubCategeoryWiseFacility(objMaster);
	}
	
	
	@Override	
	public clsWebClubFacilityMasterModel funGetWebClubMemberFacilityMaster(String strCatCode,String clientCode) {
		return objWebClubMemberCategoryMasterDao.funGetWebClubMemberFacilityMaster(strCatCode,clientCode);
	}
	
	@Override	
	public List<String> funGetCategoryWiseFacilityDtlList(String strCatCode,String strClientCode) {
		return  objWebClubMemberCategoryMasterDao.funGetCategoryWiseFacilityDtlList(strCatCode,strClientCode);
	}
	  
	

	


}

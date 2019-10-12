package com.sanguine.webclub.service;

import java.util.List;

import com.sanguine.webclub.model.clsWebClubCategeoryWiseFacilityModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel;

public interface clsWebClubMemberCategoryMasterService {
	
	  public void funAddUpdateWebClubMemberCategoryMaster(clsWebClubMemberCategoryMasterModel objMaster);
	
	  public  clsWebClubMemberCategoryMasterModel funGetWebClubMemberCategoryMaster(String docCode, String clientCode);
	
	  public void  funGetCategoryWiseFacilityDtl(String strCatCode,String strClientCode);
	  
	  public void funAddUpdateWebClubCategeoryWiseFacility(clsWebClubCategeoryWiseFacilityModel objMaster);
	  
	  public  clsWebClubFacilityMasterModel funGetWebClubMemberFacilityMaster(String strCatCode,String clientCode);
		
	  public  List<String> funGetCategoryWiseFacilityDtlList(String strCatCode,String strClientCode);
		
	  
}

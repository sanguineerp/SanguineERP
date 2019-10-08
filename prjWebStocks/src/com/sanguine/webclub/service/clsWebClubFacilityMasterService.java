package com.sanguine.webclub.service;

import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;

public interface clsWebClubFacilityMasterService{

	public void funAddUpdateWebClubFacilityMaster(clsWebClubFacilityMasterModel objMaster);

	public clsWebClubFacilityMasterModel funGetWebClubFacilityMaster(String docCode,String clientCode);

}

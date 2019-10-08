package com.sanguine.webclub.dao;

import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;

public interface clsWebClubFacilityMasterDao{

	public void funAddUpdateWebClubFacilityMaster(clsWebClubFacilityMasterModel objMaster);

	public clsWebClubFacilityMasterModel funGetWebClubFacilityMaster(String docCode,String clientCode);

}

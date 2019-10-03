package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;

public interface clsBanquetStaffCategeoryMasterService{

	public void funAddUpdateBanquetStaffCategeoryMaster(clsBanquetStaffCategeoryMasterModel objMaster);

	public clsBanquetStaffCategeoryMasterModel funGetBanquetStaffCategeoryMaster(String docCode,String clientCode);

	public clsBanquetStaffCategeoryMasterModel funGetObject(String code, String clientCode);
	
	
}

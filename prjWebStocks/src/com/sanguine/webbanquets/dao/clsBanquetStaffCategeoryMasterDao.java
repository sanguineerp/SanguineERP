package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;

public interface clsBanquetStaffCategeoryMasterDao{

	public void funAddUpdateBanquetStaffCategeoryMaster(clsBanquetStaffCategeoryMasterModel objMaster);

	public clsBanquetStaffCategeoryMasterModel funGetBanquetStaffCategeoryMaster(String docCode,String clientCode);

	public clsBanquetStaffCategeoryMasterModel funGetObject(String code,String clientCode);

}

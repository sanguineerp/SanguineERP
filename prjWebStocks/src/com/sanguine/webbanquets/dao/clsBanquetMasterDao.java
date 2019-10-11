package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsBanquetMasterModel;

public interface clsBanquetMasterDao{

	public void funAddUpdateBanquetMaster(clsBanquetMasterModel objMaster);

	public clsBanquetMasterModel funGetBanquetMaster(String docCode,String clientCode);

}

package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel;

public interface clsBanquetTypeMasterDao{

	public void funAddUpdateBanquetTypeMaster(clsBanquetTypeMasterModel objMaster);

	public clsBanquetTypeMasterModel funGetBanquetTypeMaster(String docCode,String clientCode);

}

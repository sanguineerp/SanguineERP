package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsCostCenterMasterModel;

public interface clsCostCenterMasterDao{

	public void funAddUpdateCostCenterMaster(clsCostCenterMasterModel objMaster);

	public clsCostCenterMasterModel funGetCostCenterMaster(String docCode,String clientCode);

}

package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsServiceMasterModel;

public interface clsServiceMasterDao{

	public void funAddUpdateServiceMaster(clsServiceMasterModel objMaster);

	public clsServiceMasterModel funGetServiceMaster(String docCode,String clientCode);

}

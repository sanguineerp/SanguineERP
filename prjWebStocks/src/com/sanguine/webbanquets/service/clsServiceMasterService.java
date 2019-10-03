package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsServiceMasterModel;

public interface clsServiceMasterService{

	public void funAddUpdateServiceMaster(clsServiceMasterModel objMaster);

	public clsServiceMasterModel funGetServiceMaster(String docCode,String clientCode);

}

package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;

public interface clsMenuHeadMasterService{

	public void funAddUpdatefrmMenuHeadMaster(clsMenuHeadMasterModel objMaster);

	public clsMenuHeadMasterModel funGetfrmMenuHeadMaster(String docCode,String clientCode);
	
	

}

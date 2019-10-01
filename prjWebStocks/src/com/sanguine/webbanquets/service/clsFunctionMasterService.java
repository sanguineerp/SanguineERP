package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsFunctionMasterModel;

public interface clsFunctionMasterService{

	public void funAddUpdateFunctionMaster(clsFunctionMasterModel objMaster);

	public clsFunctionMasterModel funGetFunctionMaster(String docCode,String clientCode);

}

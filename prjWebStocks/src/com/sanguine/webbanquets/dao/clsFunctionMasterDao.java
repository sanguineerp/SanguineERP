package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsFunctionMasterModel;

public interface clsFunctionMasterDao{

	public void funAddUpdateFunctionMaster(clsFunctionMasterModel objMaster);

	public clsFunctionMasterModel funGetFunctionMaster(String docCode,String clientCode);

}

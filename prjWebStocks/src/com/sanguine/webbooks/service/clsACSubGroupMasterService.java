package com.sanguine.webbooks.service;

import java.util.List;

import com.sanguine.webbooks.model.clsACSubGroupMasterModel;

public interface clsACSubGroupMasterService{

	public void funAddUpdateACSubGroupMaster(clsACSubGroupMasterModel objMaster);

	public clsACSubGroupMasterModel funGetACSubGroupMaster(String docCode,String clientCode);

	public List funGetWebBooksSubGroupMaster(String subGroupCode, String clientCode);

}

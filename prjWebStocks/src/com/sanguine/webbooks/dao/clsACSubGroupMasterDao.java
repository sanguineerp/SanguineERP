package com.sanguine.webbooks.dao;

import java.util.List;

import com.sanguine.webbooks.model.clsACSubGroupMasterModel;

public interface clsACSubGroupMasterDao{

	public void funAddUpdateACSubGroupMaster(clsACSubGroupMasterModel objMaster);

	public clsACSubGroupMasterModel funGetACSubGroupMaster(String docCode,String clientCode);

	public List funGetWebBooksSubGroupMaster(String subGroupCode, String clientCode);

}

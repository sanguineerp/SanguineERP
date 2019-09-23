package com.sanguine.webclub.service;

import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel;

public interface clsWebClubBusinessSourceMasterService{

	public void funAddUpdateWebClubBusinessSourceMaster(clsWebClubBusinessSourceMasterModel objMaster);

	public clsWebClubBusinessSourceMasterModel funGetWebClubBusinessSourceMaster(String docCode,String clientCode);

}

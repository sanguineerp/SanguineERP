package com.sanguine.webclub.dao;

import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel;



public interface clsWebClubBusinessSourceMasterDao{

	public void funAddUpdateWebClubBusinessSourceMaster(clsWebClubBusinessSourceMasterModel objMaster);

	public clsWebClubBusinessSourceMasterModel funGetWebClubBusinessSourceMaster(String docCode,String clientCode);

}

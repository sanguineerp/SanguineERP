package com.sanguine.webclub.service;

import java.util.List;

import com.sanguine.webclub.model.clsWebClubAreaMasterModel;

public interface clsWebClubAreaMasterService {

	public void funAddUpdateWebClubAreaMaster(clsWebClubAreaMasterModel objMaster);

	public clsWebClubAreaMasterModel funGetWebClubAreaMaster(String docCode, String clientCode);

	public List funGetWebClubAllAreaData(String areaCode, String clientCode);

}

package com.sanguine.dao;

import java.util.List;

import com.sanguine.model.clsTCMasterModel;

public interface clsTCMasterDao {
	public void funAddUpdate(clsTCMasterModel objTCMaster);

	public clsTCMasterModel funGetTCMaster(String tcCode, String clientCode);

	public List<clsTCMasterModel> funGetTCMasterList(String clientCode);
}

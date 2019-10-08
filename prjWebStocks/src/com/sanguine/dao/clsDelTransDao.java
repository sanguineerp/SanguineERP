package com.sanguine.dao;

import com.sanguine.model.clsDeleteTransModel;

public interface clsDelTransDao {
	public void funInsertRecord(clsDeleteTransModel objModel);

	public void funDeleteRecord(String sql, String queryType);
}

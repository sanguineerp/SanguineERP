package com.sanguine.excise.dao;

public interface clsExciseStructureUpdateDao {

	public void funExciseUpdateStructure(String clientCode);

	public void funExciseClearTransaction(String clientCode, String[] str);

	public void funExciseClearMaster(String clientCode, String[] str);

	// public void funExciseClearTransactionByPropertyAndLoction(String
	// clientCode,String[] str,String propName,String locName);

}

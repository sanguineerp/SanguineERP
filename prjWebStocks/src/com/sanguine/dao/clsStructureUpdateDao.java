package com.sanguine.dao;

import javax.servlet.http.HttpServletRequest;

public interface clsStructureUpdateDao {
	public void funUpdateStructure(String clientCode, HttpServletRequest req);

	public void funClearTransaction(String clientCode, String[] str);

	public void funClearMaster(String clientCode, String[] str);

	public void funClearTransactionByProperty(String clientCode, String[] str, String propName);
	
	public void funUpdateWebBooksStructure(String clientCode, HttpServletRequest req);
	
	public void funClearWebBooksMaster(String clientCode, String[] str);

	public void funClearWebBooksTransactionByProperty(String clientCode, String[] str, String propName);
	
	public void funClearWebBooksTransaction(String clientCode, String[] str);

}

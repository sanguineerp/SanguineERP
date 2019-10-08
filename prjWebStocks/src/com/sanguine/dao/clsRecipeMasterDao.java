package com.sanguine.dao;

import java.util.List;

import com.sanguine.model.clsBomDtlModel;
import com.sanguine.model.clsBomHdModel;


public interface clsRecipeMasterDao {
	public void funAddUpdate(clsBomHdModel object);

	public void funAddUpdateDtl(clsBomDtlModel object);

	public List<clsBomHdModel> funGetList(String bomCode, String clientCode);

	public clsBomHdModel funGetObject(String bomCode, String clientCode);

	public long funGetLastNo(String tableName, String masterName, String columnName);

	public List funGetProductList(String sql);

	public void funDeleteDtl(String bomCode, String clientCode);

	public List funGetDtlList(String bomCode, String clientCode);

	public List funGetBOMCode(String strParentCode, String strClientCode);

	public List funGetBOMDtl(String strClientCode, String BOMCode);

}

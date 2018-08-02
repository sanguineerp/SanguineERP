package com.sanguine.dao;

import java.util.List;

import com.sanguine.model.clsProductionOrderDtlModel;
import com.sanguine.model.clsProductionOrderHdModel;

public interface clsProductionOrderDao {

	public void funAddUpdateProductionHd(clsProductionOrderHdModel ProductionOrderHdModel);

	public void funAddUpdateProductionDtl(clsProductionOrderDtlModel ProductionOrderDtlModel);

	public void funDeleteProductionOrderDtl(String OPCode);

	public clsProductionOrderHdModel funGetObject(String OPCode, String clientCode);

	public List<clsProductionOrderDtlModel> funGetDtlList(String OPCode, String clientCode);

	public List funListSOforProductionOrder(String strlocCode, String dtFullFilled, String clientCode, String orderType);

	public boolean funUpdateProductionOrderAginstMaterialProcution(String strOPCode, String clientCode);

}

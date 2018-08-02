package com.sanguine.webbooks.bean;

import java.util.ArrayList;
import java.util.List;


public class clsUserDefineReportBean 
{

	//Variable Declaration
	private String strReportId;
	
	private String strReportName;
	
	private String dteUserDefDate;
	
	private String dteFDate;
	
	private String dteTDate;
	
	private List<clsUserDefinedReportDtlBean> listUserDefRptDtlBean = new ArrayList<clsUserDefinedReportDtlBean>();

	public String getStrReportId() {
		return strReportId;
	}

	public void setStrReportId(String strReportId) {
		this.strReportId = strReportId;
	}

	public String getStrReportName() {
		return strReportName;
	}

	public void setStrReportName(String strReportName) {
		this.strReportName = strReportName;
	}

	public String getDteUserDefDate() {
		return dteUserDefDate;
	}

	public void setDteUserDefDate(String dteUserDefDate) {
		this.dteUserDefDate = dteUserDefDate;
	}

	public List<clsUserDefinedReportDtlBean> getListUserDefRptDtlBean() {
		return listUserDefRptDtlBean;
	}

	public void setListUserDefRptDtlBean(
			List<clsUserDefinedReportDtlBean> listUserDefRptDtlBean) {
		this.listUserDefRptDtlBean = listUserDefRptDtlBean;
	}

	public String getDteFDate() {
		return dteFDate;
	}

	public void setDteFDate(String dteFDate) {
		this.dteFDate = dteFDate;
	}

	public String getDteTDate() {
		return dteTDate;
	}

	public void setDteTDate(String dteTDate) {
		this.dteTDate = dteTDate;
	}
	
	
	
	
}

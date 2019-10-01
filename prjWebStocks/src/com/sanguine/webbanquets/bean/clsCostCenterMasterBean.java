package com.sanguine.webbanquets.bean;

public class clsCostCenterMasterBean{
//Variable Declaration
	private String strCostCenterCode;

	private String strCostCenterName;

	private long intId;

	private String strUserCreated;

	private String strUserEdited;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strOperational;

	private String strClientCode;

//Setter-Getter Methods
	public String getStrCostCenterCode(){
		return strCostCenterCode;
	}
	public void setStrCostCenterCode(String strCostCenterCode){
		this.strCostCenterCode=strCostCenterCode;
	}

	public String getStrCostCenterName(){
		return strCostCenterName;
	}
	public void setStrCostCenterName(String strCostCenterName){
		this.strCostCenterName=strCostCenterName;
	}

	public long getIntId(){
		return intId;
	}
	public void setIntId(long intId){
		this.intId=intId;
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this.strUserCreated=strUserCreated;
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this.strUserEdited=strUserEdited;
	}

	public String getDteDateCreated(){
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated){
		this.dteDateCreated=dteDateCreated;
	}

	public String getDteDateEdited(){
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited){
		this.dteDateEdited=dteDateEdited;
	}

	public String getStrOperational(){
		return strOperational;
	}
	public void setStrOperational(String strOperational){
		this.strOperational=strOperational;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}



}

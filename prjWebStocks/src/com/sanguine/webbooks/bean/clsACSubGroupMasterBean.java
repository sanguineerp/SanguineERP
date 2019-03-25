package com.sanguine.webbooks.bean;

public class clsACSubGroupMasterBean{
//Variable Declaration
	private long intSGCode;

	private String strSubGroupCode;

	private String strSubGroupName;

	private String strGroupCode;

	private String strUnderSubGroup;

	private String strClientCode;

	private String strGroupName;
//Setter-Getter Methods
	
	public String getStrGroupName() {
		return strGroupName;
	}

	public void setStrGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

	public long getIntSGCode(){
		return intSGCode;
	}
	public void setIntSGCode(long intSGCode){
		this.intSGCode=intSGCode;
	}

	public String getStrSubGroupCode(){
		return strSubGroupCode;
	}
	public void setStrSubGroupCode(String strSubGroupCode){
		this.strSubGroupCode=strSubGroupCode;
	}

	public String getStrSubGroupName(){
		return strSubGroupName;
	}
	public void setStrSubGroupName(String strSubGroupName){
		this.strSubGroupName=strSubGroupName;
	}

	public String getStrGroupCode(){
		return strGroupCode;
	}
	public void setStrGroupCode(String strGroupCode){
		this.strGroupCode=strGroupCode;
	}

	public String getStrUnderSubGroup(){
		return strUnderSubGroup;
	}
	public void setStrUnderSubGroup(String strUnderSubGroup){
		this.strUnderSubGroup=strUnderSubGroup;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}



}

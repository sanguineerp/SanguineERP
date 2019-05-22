package com.sanguine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.document.AbstractExcelView;

@Controller
public class clsExcelBuilderForAccountReports extends AbstractExcelView {

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container

		List listExcelData = (List) model.get("excelDataList");
		
		String reportName = (String) listExcelData.get(0);
		List listHeaderLevelInfo=(List)listExcelData.get(3);
		List listdate = new ArrayList();
		try {
			listdate = (List) listExcelData.get(2);
		} catch (Exception e) {
			listdate = new ArrayList();
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + reportName.trim() + ".xls");

		List listData = new ArrayList();
		listData=(List)listExcelData.get(4);
		
	// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet("Sheet");
		sheet.setDefaultColumnWidth(80);

	// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		
		CellStyle style2 = workbook.createCellStyle();
		Font font2 = workbook.createFont();
		font2.setFontName("Arial");
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		
		
		/*CellStyle style3 = workbook.createCellStyle();
		Font font3 = workbook.createFont();
		font3.setFontName("Arial");
		style3.setFillForegroundColor(HSSFColor.YELLOW.index);
		style3.setFillPattern(CellStyle.ALIGN_FILL);
		//style3.setBorderBottom(arg0);
		font3.setColor(HSSFColor.BLACK.index);
		style3.setFont(font3);
		
		CellStyle style4 = workbook.createCellStyle();
		Font font4 = workbook.createFont();
		font4.setFontName("Arial");
		style4.setFillForegroundColor(HSSFColor.YELLOW.index);
		style4.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font4.setColor(HSSFColor.BLACK.index);
		style4.setFont(font4);*/
		// create header row
		
		int excelRowCount=4;
		HSSFRow header = sheet.createRow(excelRowCount);
		for (int rowCount = 0; rowCount < listHeaderLevelInfo.size(); rowCount++) {
			header.createCell(rowCount).setCellValue(listHeaderLevelInfo.get(rowCount).toString());
			header.getCell(rowCount).setCellStyle(style);
		}

		HSSFRow fittler = sheet.createRow(excelRowCount);
		excelRowCount++;
		for (int rowfitter = 0; rowfitter < listdate.size(); rowfitter++) {
			fittler.createCell(rowfitter).setCellValue(listdate.get(rowfitter).toString());
			fittler.getCell(rowfitter).setCellStyle(style2);
		}
				
		for (int rowtitile = 0; rowtitile < 1; rowtitile++) {
			HSSFRow blank = sheet.createRow(excelRowCount);
			excelRowCount++;
			blank.createCell(rowtitile).setCellValue("");
			// titile.getCell(rowtitile).setCellStyle(style);
		}
 
	// create data rows
	// aRow is add Row
		
		Map<String,Object> hmGrpSubGrpAcc=(Map) listData.get(0);
		List<List<String>> list1=new ArrayList<>(); 
		List<String> listLiabilities=new ArrayList<>(); 
		List<String> listAssets=new ArrayList<>(); 
		for(String groupAcc:hmGrpSubGrpAcc.keySet()){
			List accList=new ArrayList<>();
			if(hmGrpSubGrpAcc.get(groupAcc) instanceof Map){
				if(groupAcc.startsWith("LIABILITY")){//Checking category
					listLiabilities.add("LIABILITY");
					groupAcc=groupAcc.replace("LIABILITY-",""); //Removed category name
					String[] arrGrpSubGrp=groupAcc.split("!"); // [0] =Group NAme, [1]= Subgroup Name
					listLiabilities.add(arrGrpSubGrp[0]); //Grp Added
					listLiabilities.add(arrGrpSubGrp[1]); //Sub Grp Added
					
					//Get Sub Grp map 
					Map<String,Object> mapSubGrpAcc=(Map) hmGrpSubGrpAcc.get("LIABILITY-"+groupAcc);
					//Check SubGroupAccs
					String strSubGrpMapKey[]=arrGrpSubGrp[1].toString().split("_");
					List listSubGrpAcc=new ArrayList();
					listSubGrpAcc=funCheckMapKey(mapSubGrpAcc,strSubGrpMapKey[0]+"!"+strSubGrpMapKey[1],listSubGrpAcc);// SubGrp code!name
					if(listSubGrpAcc!=null){
						listLiabilities.addAll(listSubGrpAcc); //All Accounts of Subgroup
					}
					if(listSubGrpAcc.size()==0 &&listSubGrpAcc!=null){
						//check Under SubGroup
						listSubGrpAcc=new ArrayList(); 
						listSubGrpAcc=funIterateMap(mapSubGrpAcc,listSubGrpAcc);
						listLiabilities.addAll(listSubGrpAcc);
					}
					
				}else{
					
					//Checking category
					listAssets.add("ASSET");
					groupAcc=groupAcc.replace("ASSET-",""); //Removed category name
					String[] arrGrpSubGrp=groupAcc.split("!"); // [0] =Group NAme, [1]= Subgroup Name
					listAssets.add(arrGrpSubGrp[0]); //Grp Added
					listAssets.add(arrGrpSubGrp[1]); //Sub Grp Added
					
					//Get Sub Grp map 
					Map<String,Object> mapSubGrpAcc=(Map) hmGrpSubGrpAcc.get("ASSET-"+groupAcc);
					//Check SubGroupAccs
					String strSubGrpMapKey[]=arrGrpSubGrp[1].toString().split("_");
					List listSubGrpAcc=new ArrayList();
					listSubGrpAcc=funCheckMapKey(mapSubGrpAcc,strSubGrpMapKey[0]+"!"+strSubGrpMapKey[1],listSubGrpAcc);// SubGrp code!name
					if(listSubGrpAcc!=null){
						listAssets.addAll(listSubGrpAcc); //All Accounts of Subgroup
					}
					if(listSubGrpAcc.size()==0 &&listSubGrpAcc!=null){
						//check Under SubGroup
						listSubGrpAcc=new ArrayList(); 
						listSubGrpAcc=funIterateMap(mapSubGrpAcc,listSubGrpAcc);
						listAssets.addAll(listSubGrpAcc);
					}
				}
				
			}

		}
		
		list1.add(listAssets);
	//	list1.add(listLiabilities);
		excelRowCount++;
		int maxListSize=(listAssets.size()>=listLiabilities.size())?listAssets.size():listLiabilities.size(); 
		HSSFRow aRowHeader = sheet.createRow(excelRowCount);
		aRowHeader.createCell(0).setCellValue("ASSET");
		aRowHeader.createCell(1).setCellValue("LIABILITY");
		aRowHeader.getCell(0).setCellStyle(style);
		aRowHeader.getCell(1).setCellStyle(style);
		
		excelRowCount++;
		for(int rowCount = 0; rowCount <maxListSize; rowCount++ ){
			
			HSSFRow aRow = sheet.createRow(excelRowCount);
			if(rowCount<listAssets.size()){
				String rowData=(String)listAssets.get(rowCount);
				if(!rowData.equals("ASSET")){ //Sub Group
					if(rowData.contains("_")){
						aRow.createCell(0).setCellValue(rowData.replace("_","  "));
							
					}else{
						aRow.createCell(0).setCellValue("      "+rowData);
					//	aRow.getCell(0).setCellStyle(style3);
					}
				}
			}
			if(rowCount<listLiabilities.size()){
				String rowData=(String)listLiabilities.get(rowCount);
				if(!rowData.equals("LIABILITY")){
					
					if(rowData.contains("_")){ //sub Group 
						aRow.createCell(1).setCellValue(rowData.replace("_","  "));
							
					}else{
						aRow.createCell(1).setCellValue("      "+rowData);
					//	aRow.getCell(1).setCellStyle(style3);
					}
					
				}
			}
			excelRowCount++;
			
		}
		
		
		
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	private List funIterateMap(Map<String,Object> map,List accList){
		for(String groupAcc:map.keySet()){
			if(map.get(groupAcc) instanceof Map){
				Map map1=(Map)map.get(groupAcc);
				accList.add(groupAcc);
				accList=funIterateMap(map1,accList);
			}else if(map.get(groupAcc) instanceof List){
				accList.add(groupAcc);
				accList=(List) map.get(groupAcc);
			}	
		}
		
		return accList;
	}
	
	private List funCheckMapKey(Map<String,Object> mapSubGrp,String key,List AccList){
		//List AccList=null;
		if(mapSubGrp.containsKey(key)){
			if(mapSubGrp.get(key) instanceof List){
				AccList=(List)mapSubGrp.get(key);
			}else if(mapSubGrp.get(key) instanceof Map){
				AccList.add(key);
				funCheckMapKey((Map<String,Object>)mapSubGrp.get(key),key,AccList);
			}
		}
		return AccList;
	}
	
}

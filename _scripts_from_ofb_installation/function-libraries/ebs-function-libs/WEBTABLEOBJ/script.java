import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMDocument;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import oracle.oats.utilities.FileUtility;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;

	WebTable webTable= null;
	
	public void initialize() throws Exception {
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {

	}

	public void finish() throws Exception {
	}
	
	public void initializeWebTable() throws Exception {

		WebTable.web = web;
		WebTable.javaScriptFilePath = getScriptPackage().getScriptPath().replace(getScriptPackage().getScriptName()+".jwg", "")+"work_around.js";
		WebTable.itv = this; 
	}
	
	
	public String findDynamicEmptyRowStart(String tableName,String colName, String compType, String compSequence) throws Exception{
		
		int rowNum = -1; 
		
		WebTable webTable = getWebTableObject(tableName);
		
		int colNum = webTable.getColumnNumber(colName);
		
		int rowCount = webTable.getRowCount();
		
		int rowStartIndex = webTable.getRowStartIndex();
		
		rowCount = rowCount-rowStartIndex+1;
		
		for(int i =1; i< rowCount;i++){
			
			List<String> compList = webTable.getCellData(i, colNum, compType,compSequence);
			info("Data :"+compList.toString());
			
			if(compList.size() == 0){
				
				rowNum = i;
				
				break;
			}
		}
		
		return ""+rowNum;
	}
	
	public String findEmptyRowStart(String tableName,String colName) throws Exception{
		
		String[] compTypes = {"IMG","INPUT","span","div"};
		
		int rowNum = -1;
		
		WebTable webTable = getWebTableObject(tableName);
		
		int colNum = webTable.getColumnNumber(colName);
		
		int rowCount = webTable.getRowCount();
		info("Row Count :"+rowCount);
		
		int rowStartIndex = webTable.getRowStartIndex();
		info("Row Start Index :"+rowStartIndex);
		
		rowCount = rowCount-rowStartIndex+1;

		for(int index =1; index< rowCount;index++){
			
			String compNum = webTable.getComponentAvailability(index, colNum, "input");
			info("compExists with row num :"+compNum);
			
			if(toInt(compNum) >= 0){
				
				List<String> compList = webTable.getCellData(index, colNum, "input",compNum);
				info("cell Data in the specified row :"+index+" for input component is :"+compList);
				
				if(compList.size() == 0){
					rowNum = index-1; // decresing because
					break;
				}
			}
		}
		
		return ""+rowNum;
	}
	
	
	public void clearWebTableObject(){
		this.webTable = null;
	}
	

	/**
	 *  Gets Table Object
	 * @param tableName
	 * 			-- It looks like "Select" or "Select;1" or "Select;1,document=0,form=0"
	 * @return
	 * @throws Exception
	 */
	public WebTable getWebTableObject(String tableName) throws Exception {
		
		//System.out.println("webTable :"+webTable);
		if(webTable == null){
			
			int documentIndex = 0;
			int formIndex = 0;
			int tableIndex = 0;
			String listId = "";
			String listName = "";
			String expTableName = "";
			String listPath = "";

			String tableDetails[] = tableName.split(",");

			// Get the Table Details passed from User
			for (int index = 0; index <tableDetails.length; index++) {

				if (tableDetails[index].trim().startsWith("document=")) {

					documentIndex = Integer.parseInt(tableDetails[index].split("=")[1]);

				} else if (tableDetails[index].trim().startsWith("form=")) {

					formIndex = Integer.parseInt(tableDetails[index].split("=")[1]);
					
				} else if (tableDetails[index].toLowerCase().trim().startsWith("listid=")) {

					listId = tableDetails[index].split("=")[1];
					
				} else if (tableDetails[index].toLowerCase().trim().startsWith("listname=")) {

					listName = tableDetails[index].split("=")[1];
					
				} else if (tableDetails[index].toLowerCase().trim().startsWith("listpath=")) {

					String listDetails = tableDetails[index].split("=")[1];
					
					if(listDetails.equalsIgnoreCase("null")){
						listPath = null;
					}else{
						listPath = listDetails;
					}
					
					
				}else if (!tableDetails[index].contains("document=") && !tableDetails[index].contains("form=")) {

					String[] tableNameDetails = tableDetails[index].split(";");

					// Similar Table Index
					if (tableNameDetails.length > 1) {
						tableIndex = Integer.parseInt(tableNameDetails[1]);
					}

					// Table Name
					expTableName = tableNameDetails[0];

				} 
			}
			
			info("****** Table Details ********************* ");
			info("Table Column Name: \"" + expTableName+ "\"  Table Index: \"" + tableIndex+ "\"  Document Index: \""
					+ documentIndex+ "\"  Form Index: \"" + formIndex + "\"");
			
			
			// Initialize WebTable
			initializeWebTable();
			
			// Get Table Object
			//WebTable webTable = null;
			
			if(listPath == null){
				webTable = new WebTable(expTableName,tableIndex, documentIndex, formIndex,null,200);
				
			}else if("".equalsIgnoreCase(listPath)){
				
				String tableListPath = "";
				
				if(!"".equalsIgnoreCase(listId) && "".equalsIgnoreCase(listName)){
					tableListPath = "/web:select[(@id='"+listId+"') and multiple mod 'False']";
				}
				
				if("".equalsIgnoreCase(listId) && !"".equalsIgnoreCase(listName)){
					tableListPath = "/web:select[(@name='"+listName+"') and multiple mod 'False']";
				}
				
				webTable = new WebTable(expTableName,tableIndex, documentIndex, formIndex,tableListPath,200);
			
			}else
			{
				webTable = new WebTable(expTableName,tableIndex, documentIndex, formIndex,listPath,200);
			}
		}
		
		
		return webTable;
	}
	
	
	public void waitForColumn(String tableName) throws Exception{
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.waitForColumn(webTable.tableColName);
	}
	
	
	
	/**
	 *  Get Columns 
	 *  
	 * @param tableName
	 * @throws Exception
	 */
	public void getColumns(String tableName) throws Exception{
		
		WebTable webTable = getWebTableObject(tableName);
		
		String[] colNames = webTable.getColumns();
		
		for(int index=0; index<colNames.length; index++){
			System.out.println("ColNames:"+colNames[index]);
		}
		
	}
	
	public int getRowCount(String tableName) throws Exception{
		
		WebTable webTable = getWebTableObject(tableName);
		
		int rowCount = webTable.getRowCount();
		
		return rowCount;
	}
	
	
	
	public HashMap<String,String> getColumnDetails(HashMap<String,String> actions) throws Exception{
		
		String displayName = actions.get("displayname");
		
		return getColumnDetails(displayName);
	}
	
	
	public HashMap<String,String> getColumnDetails(String columnName) throws Exception{
		
		String simlilarColIndex = "1";
		String compSeq = "0";
		String colNameorIndex = "";
		
		String displayName = columnName;
		
		displayName = Pattern.compile("\\,",Pattern.LITERAL).matcher(displayName).replaceAll(Matcher.quoteReplacement("~~"));
		String details[] = displayName.split(",");
		
		for(int index=0; index < details.length; index++){
			details[index] = details[index].replaceAll("~~", ",");
		}
		
		if(details[0].trim().contains(";")){
			
			colNameorIndex = details[0].split(";")[0];
			compSeq =  details[0].split(";")[1];
			
		}else{
			colNameorIndex = details[0];
			compSeq = "0";
		}
		
		
		for (int index = 1; index < details.length; index++) {
			
			if (details[index].trim().startsWith("colIndex=")) {

				simlilarColIndex = details[index].split("=")[1];
				
			}
		}
		
		HashMap<String,String> colDetails = new HashMap<String,String>();
		colDetails.put("colName", colNameorIndex+";"+simlilarColIndex);
		colDetails.put("simlilarColIndex", simlilarColIndex);
		colDetails.put("compSeq", compSeq);
		
		return colDetails;
	}
	
	public boolean verifyTableWithExactRowData(String tableName,String[] colNames, HashMap<String,List<String>> expectedTableData , String[] sExpComponents) throws Exception{
		
		WebTable webTable = getWebTableObject(tableName);
		
		boolean verifyResult = webTable.verifyTableWithExactRowData(colNames, expectedTableData, sExpComponents);
		
		return verifyResult;
	}
	
	
	public List<String> getCellData(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

		info("WebTable1 :"+webTable);
		WebTable webTable = getWebTableObject(tableName);
		info("WebTable2 :"+webTable);
		String colNames [] = getKeysFromHashMap(searchColumns);
		String values [] =getValuesFromHashMap(searchColumns);
		
		return webTable.getCellData(colNames, values, actions.get("displayname"), "td");
		
		//return webTable.getCellData(colNames,values ,actions.get("displayname")) ;
	}
	
	public List<String> getCellData(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		
		WebTable webTable = getWebTableObject(tableName);
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		return webTable.getCellData(rowNum, colDetails.get("colName"), "td");
		//return webTable.getCellData(rowNum,actions.get("displayname")) ;
	}
	
		
	public void checkOn(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		webTable.selectCheckBox(rowNum, colDetails.get("colName"), true, toInt(colDetails.get("compSeq"))) ;
		
		/*String colDetials[] = actions.get("displayname").split(",");
		String colIndex[] = colDetials[0].split(";");
		 
		 if(colIndex.length>1)
		 { 
			 webTable.selectCheckBox(rowNum, colIndex[0], true, toInt(colIndex[0])) ;
		 }
		 
		 else{	
			 webTable.selectCheckBox(rowNum,actions.get("displayname")) ;
		 }*/
	}
	
	public void checkOFF(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		WebTable webTable = getWebTableObject(tableName);

		String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1)
		 { 
			 webTable.selectCheckBox(rowNum, colIndex[0], false, toInt(colIndex[0])) ;
		 }else{
			 webTable.selectCheckBox(rowNum,actions.get("displayname"),false) ;
		 }
	}
	
	
	public void radioOn(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.selectRadioButton(rowNum, colDetails.get("colName"), toInt(colDetails.get("compSeq"))) ;
		
		/*String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1)
		 { 
			 webTable.selectRadioButton(rowNum, colIndex[0], toInt(colIndex[0])) ;
		 } else{
			 webTable.selectRadioButton(rowNum,actions.get("displayname")) ;
		 }*/
	}
	
	
	public String getEditValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getText(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		// Target Column & index for the specific element in target column
		 String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1){
			 return webTable.getText(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1])); 
		 }else{
			 return webTable.getText(rowNum,webTable.getColumnNumber( colIndex[0]));
		 }*/	 
	}
	
	public String getLinkValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getLink(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		// Target Column & index for the specific element in target column
		 String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1){
			 return webTable.getLink(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1])); 
		 }else{
			 return webTable.getLink(rowNum,webTable.getColumnNumber( colIndex[0]));
		 } */
	}
	
	
	public String getImageValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getImage(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		// Target Column & index for the specific element in target column
		String colIndex[] = actions.get("displayname").split(";");
		
		if(colIndex.length>1){
			 return webTable.getImage(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1])); 
		 }else{ 
			 return webTable.getImage(rowNum,webTable.getColumnNumber( colIndex[0]));
		 }*/	 
	}
	
	
	public String getCheckBoxOption	(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

		return "";
	}

	public String getCheckBoxOption	(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getCheckBoxOption(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		// Target Column & index for the specific element in target column
		 String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1){
			 
			 return webTable.getCheckBoxOption	(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1]));
 
		 }else{ 
			 return webTable.getCheckBoxOption	(rowNum,webTable.getColumnNumber( colIndex[0]));
		 } */
	}
	
	public String getTextAreaValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getTextAreaValue(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		/*
		
		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			 return webTable.getTextAreaValue(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1]));
 
		 }else{
			 
			 return webTable.getTextAreaValue(rowNum,webTable.getColumnNumber( actions.get("displayname")));
		 } */
	}
	
	public String getListValue(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
		
		return "";
	}
	
	public String getListValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		return webTable.getSlectedOption(rowNum, webTable.getColumnNumber(colDetails.get("colName")), toInt(colDetails.get("compSeq"))) ;
		
		
	/*	// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			 return webTable.getSlectedOption(rowNum,webTable.getColumnNumber(colIndex[0]), toInt(colIndex[1]));
			 	 
		 }else{ 
			 return webTable.getSlectedOption(rowNum,webTable.getColumnNumber( actions.get("displayname")));
		 }*/
	}

	
////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setEditValue(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {

	}
	
	public void setEditValue(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.setText(rowNum, colDetails.get("colName"), actions.get("value1"), toInt(colDetails.get("compSeq"))) ;
		
		/*
		
		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			 
			  webTable.setText(rowNum, colIndex[0], actions.get("value1"), toInt(colIndex[1]));
			 
		 }else{
			 
			  webTable.setText(rowNum, colIndex[0],actions.get("value1"));
		 }*/
	}
	
	public void setTextArea(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
	
	}
	
	
	
	public void setTextArea(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.setTextArea(rowNum, colDetails.get("colName"), actions.get("value1"), toInt(colDetails.get("compSeq"))) ;
		
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			  webTable.setTextArea(rowNum, colIndex[0], actions.get("value1"), toInt(colIndex[1]));
			 
			 
		 }else{
			 
			  webTable.setTextArea(rowNum, actions.get("displayname"),actions.get("value1"));
		 }*/
	}

 
	public void clickImage(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
	
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		String colNames [] = getKeysFromHashMap(searchColumns);
		String values [] =getValuesFromHashMap(searchColumns);
		
		webTable.clickImage(colNames, values, colDetails.get("colName"), toInt(colDetails.get("compSeq")));
	}
	
	public void clickImage(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.clickImage(rowNum, colDetails.get("colName"), toInt(colDetails.get("compSeq"))) ;
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		//String imgName = actions.get("value1");
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			 
			  webTable.clickImage(rowNum, colIndex[0], toInt(colIndex[1]));
		 }else if(imgName != null){
			 
			  webTable.clickImage(rowNum, colIndex[0],imgName);
		 }else{
			 
			  webTable.clickImage(rowNum, actions.get("displayname"));
		 }*/
	}


	public void clickImageByName(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.clickImage(rowNum, colDetails.get("colName"), actions.get("value1")) ;
		/*
		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.clickImage(rowNum, actions.get("displayname"),actions.get("value1"));*/
	}


	public void clickImageByName(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
	}
	
	
	public void clickLink(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
		
		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		String colNames [] = getKeysFromHashMap(searchColumns);
		String values [] =getValuesFromHashMap(searchColumns);
		String lnkName = actions.get("value1");
		
		 String colIndex[] = actions.get("displayname").split(";");
		 
		 if(colIndex.length>1){
			 
			  webTable.clickLink(colNames,values, colIndex[0], toInt(colIndex[1]));
		 }else if(lnkName != null){
			 
			  webTable.clickLink(colNames,values, colIndex[0], lnkName);
		 }else{
			 
			  webTable.clickLink(colNames,values, colIndex[0]);
		 }
	}
	
	public void clickLink(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {

		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.clickLink(rowNum, colDetails.get("colName"), toInt(colDetails.get("compSeq"))) ;
		
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		//String lnkName = actions.get("value1"); -- getting issue for webselectlov
		String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			 
			  webTable.clickLink(rowNum, colIndex[0], toInt(colIndex[1]));
			 
		 }else if(lnkName != null){
			  webTable.clickLink(rowNum, colIndex[0], lnkName);
		 }else{
			 
			  webTable.clickLink(rowNum, colIndex[0]);
		 }*/
	}

	public void clickButton(String tableName, int rowNum, HashMap<String,String> actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.clickButton(rowNum, colDetails.get("colName"), toInt(colDetails.get("compSeq"))) ;
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		//String btnName = actions.get("value1");
		
		 String colIndex[] = actions.get("displayname").split(";");
		 if(colIndex.length>1){
			  webTable.clickButton(rowNum, colIndex[0], toInt(colIndex[1]));
		 }else if(btnName != null){
			  webTable.clickButton(rowNum, colIndex[0], btnName);
		 }else{
			  webTable.clickButton(rowNum, colIndex[0]);
		 }*/
	}

	public void selectList(String tableName, HashMap<String, String> searchColumns, HashMap<String,String> actions)throws Exception {
		
	}
	
	public void selectList(String tableName, int rowNum, HashMap<String,String>actions)throws Exception {
		
		HashMap<String,String> colDetails = getColumnDetails(actions);
		
		WebTable webTable = getWebTableObject(tableName);
		
		webTable.selectList(rowNum, colDetails.get("colName"), actions.get("value1"), toInt(colDetails.get("compSeq"))) ;
		
		/*// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		 String colIndex[] = actions.get("displayname").split(";");
		 info("Value to Select :"+actions.get("value1"));
		 
		 if(colIndex.length>1){
			 
			  webTable.selectList(rowNum, colIndex[0], actions.get("value1"), toInt(colIndex[1]));
			 
		 }else{
			 
			  webTable.selectList(rowNum, actions.get("displayname"),actions.get("value1"));
		 }*/
	}

	
	public boolean verifyRowData(String tableName, HashMap<String, String> searchColumns, String[] actions)throws Exception {

		// WebTable Object
		WebTable webTable = getWebTableObject(tableName);
		
		String colNames [] = getKeysFromHashMap(searchColumns);
		String values [] =getValuesFromHashMap(searchColumns);
		
		
		for(int index=0; index < colNames.length; index++){
			
			HashMap<String,String> colDetails = getColumnDetails(colNames[index]);
			
			colNames[index] = colDetails.get("colName");
		}
		
		return webTable.verifyRowData(colNames,values);
	}
	
	public int findRowNumber(HashMap<String, String> searchColumns, String tableName) throws Exception{
	
		this.info("*** Getting Row Number ***********");
		
		WebTable webTable = getWebTableObject(tableName);
		
		String colNames [] = getKeysFromHashMap(searchColumns);
		String values [] =getValuesFromHashMap(searchColumns);
		
		for(int index=0; index < colNames.length; index++){
			
			HashMap<String,String> colDetails = getColumnDetails(colNames[index]);
			
			colNames[index] = colDetails.get("colName");
		}

		return webTable.getRowNumber(colNames, values);

	}
	

	public String [] getKeysFromHashMapValuesAreList(HashMap< String,List<String>> searchColumns){
		
		String colVal [] =(String[])searchColumns.keySet().toArray(new String[searchColumns.keySet().size()]);
		 
		return colVal;
	}
	
	public String [] getKeysFromHashMap(HashMap<String, String> searchColumns){
		
		String colVal [] =(String[])searchColumns.keySet().toArray(new String[searchColumns.keySet().size()]);
		 
		return colVal;
	}
	
	public String [] getValuesFromHashMap(HashMap<String, String> searchColumns){
		return (String[])searchColumns.values().toArray(new String[searchColumns.keySet().size()]);
		
	}
	
	@SuppressWarnings("unchecked") 
	public List<String> [] getValuesFromHashMapValuesAreList(HashMap< String,List<String>> searchColumns){
		
		return ( List<String>[])searchColumns.values().toArray(new ArrayList[searchColumns.keySet().size()]);
		
	}

}

class WebTable {
	@ScriptService oracle.oats.scripting.modules.basic.api.IteratingVUserScript IteratingVUserScript;
	
	/* Variable initialization */
	public static String javaScriptFilePath = null;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;

	public static WebDomService web = null;
	public static IteratingVUserScript itv = null;
	int SYNCTIME = 600;
	
	DOMDocument doc 		= null;
	String docPath 			= null;
	String formPath 		= null;
	String tableColName 	= null;
	String tableIndex 		= "0";
	String[] colNames 		= null;
	String[] colNamesWithoutTrim 		= null;
	String tableListPath 	= null;
	String listPath			= null;
	int maxRowCount 		= 10;
	String js = null;
	
	String[] defComponents = {"input","checkbox","radio","img","a","select","span","div","textarea","td"};
	String[] defAttributes = {"id","name","value","text","innertext","type","summary","index","alt","title","src"};

	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;

	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;

	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	
	
	
	WebTable(String tableColName) throws Exception {
		
		this(tableColName, 0);
	}
	
	WebTable(String tableColName, int tableIndex) throws Exception {
		
		this(tableColName, tableIndex, 200);
	}
	
	WebTable(String tableColName, String listPath) throws Exception {
		
		this(tableColName, 0, listPath, 200);
	}
	
	WebTable(String tableColName, String listPath,int syncTime) throws Exception {
		
		this(tableColName, 0, listPath, syncTime);
	}
	
	
	WebTable(String tableColName, int tableIndex ,int syncTime) throws Exception {
		
		this(tableColName, tableIndex, "/web:select[(@id='M__Id*') and multiple mod 'False']", syncTime);
	}

	
	WebTable(String tableColName, int tableIndex, int documentIndex, int formIndex) throws Exception {
		
		
		this(tableColName, tableIndex, documentIndex, formIndex, "/web:select[(@id='M__Id*') and multiple mod 'False']", 200);
	}
	
	WebTable(String tableColName, int tableIndex, int documentIndex, int formIndex,String listPath, int syncTime) throws Exception {
		
		this.tableColName = tableColName;
		
		this.tableIndex = Integer.toString(tableIndex);
		
		this.listPath = listPath;
		
		/* Initialize Sync Time */
		this.SYNCTIME = syncTime;
		
		/* Document Path */
		String windowPath = getCurrentWindowXPath();
		
		System.out.println("windowPath :"+windowPath);
		/* Current Window XPath */
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		/* Doc Path */
		docPath = windowPath + "/web:document[@index='"+documentIndex+"']";
		
		/* Form Path*/
		formPath = docPath + "/web:form[@index='"+formIndex+"']";
		
		//System.out.println("formPath :"+formPath);
		//Initialize Table
		initialize();
		
	}
	
	WebTable(String tableColName, int tableIndex, String listPath, int syncTime) throws Exception {
		
		this(tableColName, tableIndex, 0, 0,listPath, syncTime);
	}
	
	public void initialize() throws Exception {
		
		// Example: "c:\\work_around.js"
		//System.out.println("java :"+WebTable.javaScriptFilePath);
		js = FileUtility.fileToString(WebTable.javaScriptFilePath);//Put the js on any path.
		
		/* Get webDocument */
		doc = web.document(docPath);
		
		/* Execute Java Script */
		doc.executeJavaScript(js);
		
		this.itv.info("Checking List Path");
		/* Table List Path */
		if("".equalsIgnoreCase(listPath)){
			
			String selectListBoxIds="javascript,M__Id,ASFNAVIGATESEL";
			System.out.println("Finding List Path");
			String[] select = doc.executeJsFunction("listBoxExists", this.tableColName, this.tableIndex, selectListBoxIds);
			
			String ListItem = select[0];
			
			System.out.println("ListItem -"+ListItem);
			
			if(ListItem.startsWith("javascript")){  // list box can be identified by javascript
				tableListPath = ""; 
				
			}else if(ListItem.startsWith("M__Id")){
				tableListPath = formPath + "/web:select[(@id='M__Id*') and multiple mod 'False']";
				
			}else if(ListItem.startsWith("ASFNAVIGATESEL")){
				tableListPath = formPath + "/web:select[(@id='*Tbl*OBJ*ASFNAVIGATESEL') and multiple mod 'False']";
				
			}else{
				tableListPath = "";
			}
			
		}else if(listPath == null){
			
			tableListPath = null;
			
		}else{
			
			tableListPath = formPath + listPath;
		}
		
		this.itv.info("tableListPath: "+tableListPath);
		
		this.itv.info(" ************* Waiting For Column ************* ");
		waitForColumn(this.tableColName);
		
		this.itv.info(" ************* Getting Columns ************* ");
		getColumns();
		
		
		// Printing Column Names
		List<String> cols = new ArrayList(Arrays.asList(colNames));
		itv.info("Column Names :"+cols.toString());
		
		
		
		//"/web:select[(@id='O*Tbl*.OBJ.ASFNAVIGATESEL') and multiple mod 'False']"
		//"/web:select[(@id='*Tbl*OBJ*ASFNAVIGATESEL') and multiple mod 'False'] -- This is for CRM Flows
		/*String[] xPaths = { "/web:select[(@id='M__Id*') and multiple mod 'False']"};
		
		for(int index=1; index < xPaths.length; index++){
			
			if(tableListBoxExists(formPath + xPaths[index])){
				tableListPath = formPath + xPaths[index];
				break;
			}	
		}*/
	}
	
	public static void setScriptPath(String filePath){
		
		WebTable.javaScriptFilePath = filePath;
	}
	
	public void setMaxRowCount(int maxRowCount){
		
		this.maxRowCount = maxRowCount;
	}
	
	
	public String[] getColumns() throws Exception
	{
		String[] actColNames = doc.executeJsFunction("getColumns", tableColName, tableIndex);
		String[] colNames = new String[actColNames.length];
		String[] colNamesWithoutTrim = new String[actColNames.length];
		
		itv.info("Column Count :"+actColNames.length);
		
		for(int i=0;i<actColNames.length;i++){
			colNames[i] = actColNames[i].trim();
			colNamesWithoutTrim[i] = actColNames[i];
			//itv.info("column"+i+":"+actColNames[i]);
		}
		
		this.colNames = colNames;
		this.colNamesWithoutTrim = 	colNamesWithoutTrim;
		
		return colNames;
	}
	
	public int getColumnNumber(String colName) throws Exception {
		
		String actColName = "";
		int similarColIndex =1;
		
		// Trimming column name
		colName= colName.trim();
		//itv.info("Column Name :"+colName);
		
		int colIndex = -2;
		
		// Get the colums, if colnames are null
		if(this.colNames == null){
			getColumns();
		}
		
		//columnWithIndexs[0] -- ColName
		//columnWithIndexs[1] -- ColIndex -- always start with 1
		String[] columnWithIndexs = colName.split(";");
		
		if(columnWithIndexs.length == 1){
			actColName = columnWithIndexs[0];
		}else{
			actColName = columnWithIndexs[0];
			similarColIndex = Integer.parseInt(columnWithIndexs[1]);
		}
		
		//System.out.println("actColName :"+actColName);
		
		// Checking if the passed column is number or not
		if(!isANumber(actColName))
		{
			// Convert columns to List<String>
			List<String> lColNames = Arrays.asList(this.colNames);
			
			for(int index=0, colMatchIndex=0; index<lColNames.size(); index++){
				
				//System.out.println("lColNames.get(index) :"+lColNames.get(index));
				
				if(lColNames.get(index).equalsIgnoreCase(actColName)){
					
					if(similarColIndex ==  (colMatchIndex+1)){
						colIndex = index;
						break;
					}
					colMatchIndex++;
				}
			}
			
			/*if(columnWithIndexs.length == 1){
				
				if(lColNames.contains(colName)){
					colIndex = lColNames.indexOf(colName);
				}
			}else{
				
				
			
			}*/
		}else{
			
			colIndex = Integer.parseInt(actColName)- 1;
		
		}
		return colIndex;
	}
	
	public int getRowStartIndex() throws Exception
	{
		String rowCount = doc.executeJsFunction("getRowStartIndex", tableColName, tableIndex)[0];
		
		return Integer.parseInt(rowCount);
	}
	
	
	public int getRowCount() throws Exception
	{ 
		//System.out.println("Before rowCount"+tableColName+tableIndex);
		
		
		String rowCount = doc.executeJsFunction("getRowCount", tableColName, tableIndex)[0];
		//System.out.println("rowCount"+rowCount);
		return Integer.parseInt(rowCount);
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 *  Clicks the image at specified  "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colNum
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum) throws Exception{
		
		/* Click Image with compSequence=0(first image in the cell)*/
		clickImage(rowNum, colNum, 0);
		
	}
	
	/**
	 * Clicks the image at specified "Row Number" and "Column Number"
	 * 
	 * @param rowNum
	 * @param colName
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Image with compSequence=0(first image in the cell)*/
		clickImage(rowNum, colNum, 0);
		
	}

	/**
	 *  Clicks the Specified image at the specified "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colName
	 * @param imageName
	 * 			imageName means "alt" tag
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName, String imageName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click thre Image having specefied Image Tag */
		clickImage(rowNum, colNum, imageName);
	}
	
	/**
	 * Clicks image at the specified "Row Number" and "Column Number based on Sequence number "
	 * 
	 * @param rowNum
	 * @param colName
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Image with compSequence= any number 0(first image = 0 , second image =1)*/
		clickImage(rowNum, colNum, compSequence);
	}
	
	/**
	 *  Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 *  
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		clickImage(sourcolNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, compSequence);
	}

	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickImage(rowNum, colNum, compSequence);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the Image Name of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(String sourcolName, String rowValue, String targetCol, String imageName) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickImage(colNames, rowValues , targetCol, imageName);
	}
	
	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the Image Name of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(String[] sourcolNames, String[] rowValues, String targetCol, String imageName) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickImage(rowNum, colNum, imageName);
	}
	
	/**
	 *  Clicks on image based on the RowNum , Column Number and Image Name
	 *  
	 * @param rowNum
	 * @param colNum
	 * @param imageName
	 * 			ImageName means alt tag of the component
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum, String imageName) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Image Name  :"+imageName);
		
		try{
			String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "img", imageName, "null")[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Click Image at "+rowNum+","+colNum);
			}else{
				itv.info("Clicked Image at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Clicks on image based on the RowNum , Column Number and Component Sequence Number
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param compSequence
	 * 			Sequence Number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickImage(int rowNum, int colNum, int compSequence) throws Exception{
		
			String errorMessage = "";
			
			System.out.println("Expected Row Num :"+rowNum);
			System.out.println("Expected Column Num :"+colNum);
			System.out.println("Expected compSequence Num  :"+compSequence);
			/*System.out.println("Expected tableColName  :"+tableColName);
			System.out.println("Expected tableIndex :"+tableIndex);*/
			
			/*System.out.println("Expected Integer.toString(rowNum):"+Integer.toString(rowNum));
			System.out.println("Expected Integer.toString(colNum) :"+Integer.toString(colNum));
			System.out.println("Expected Integer.toString(compSequence) :"+Integer.toString(compSequence));*/
			

			try{
				String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "img", "null", Integer.toString(compSequence))[0];
				
				if(ret.equals("False")){
					throw new Exception("Unable to Click Image at "+rowNum+","+colNum);
				}else{
					itv.info("Clicked Image at "+rowNum+","+colNum);
				}
				
			}catch(Exception e){
				itv.info("Got the Exception. Please validate whether it clicked the image or not as sometimes java script is clicking" +
				" on the image and throwing an exception.");
				//throw new Exception(e.getMessage());
			}
	}
	
	/**
	 *  Clicks the Lin at specified  "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colNum
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum) throws Exception{
		
		/* Click Link with compSequence=0(first link in the cell)*/
		clickLink(rowNum, colNum, 0);
		
	}
	
	/**
	 * Clicks the Link at specified "Row Number" and "Column Number"
	 * 
	 * @param rowNum
	 * @param colName
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Link with compSequence=0(first link in the cell)*/
		clickLink(rowNum, colNum, 0);
		
	}

	/**
	 *  Clicks the Specified link at the specified "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colName
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName, String linkName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click the Link having specefied Link Tag */
		clickLink(rowNum, colNum, linkName);
	}
	
	/**
	 * Clicks link at the specified "Row Number" and "Column Number based on Sequence number "
	 * 
	 * @param rowNum
	 * @param colName
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Link with compSequence= any number 0(first link = 0 , second link =1)*/
		clickLink(rowNum, colNum, compSequence);
	}
	
	/**
	 *  Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 *  
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolNames & rowValues
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		clickLink(sourcolNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, compSequence);
	}

	/**
	 * Clicks the image at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		clickLink(rowNum, colNum, compSequence);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(String sourcolName, String rowValue, String targetCol, String linkName) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickLink(colNames, rowValues , targetCol, linkName);
	}
	
	/**
	 * Clicks the link at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(String[] sourcolNames, String[] rowValues, String targetCol, String linkName) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Link baded on RowNumber , Column Number , linkName*/
		clickLink(rowNum, colNum, linkName);
	}
	
	
	/**
	 *  Clicks on link based on the RowNum , Column Number and Image Name
	 *  
	 * @param rowNum
	 * @param colNum
	 * @param linkName
	 * 			linkName means the text that appears
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum, String linkName) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Link Name  :"+linkName);
		
		try{
			String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "a", linkName, null)[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Click Link at "+rowNum+","+colNum);
			}else{
				itv.info("Clicked Link at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Clicks on link based on the RowNum , Column Number and Component Sequence Number
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param compSequence
	 * 			Sequence Number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickLink(int rowNum, int colNum, int compSequence) throws Exception{
		
			String errorMessage = "";
			
			System.out.println("Expected Row Num :"+rowNum);
			System.out.println("Expected Column Num :"+colNum);
			System.out.println("Expected compSequence Num  :"+compSequence);
			
			try{
				String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "a", "null", Integer.toString(compSequence))[0];
				
				if(ret.equalsIgnoreCase("false")){
					throw new Exception("Unable to Click Link at "+rowNum+","+colNum);
				}else{
					itv.info("Clicked Link at "+rowNum+","+colNum);
				}
				
			}catch(Exception e){
				itv.info(e.getMessage());
				itv.info("Got the Exception. Please validate whether it clicked the link or not as sometimes java script is clicking" +
						" on the link and throwing an exception.");
				//throw new Exception(e.getMessage());
			}
	}
	
	
	/**
	 *  Clicks the Button at specified  "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colNum
	 * @throws Exception
	 */
	public void clickButton(int rowNum, int colNum) throws Exception{
		
		/* Click Button with compSequence=0(first link in the cell)*/
		clickButton(rowNum, colNum, 0);
		
	}
	
	/**
	 * Clicks the Button at specified "Row Number" and "Column Number"
	 * 
	 * @param rowNum
	 * @param colName
	 * @throws Exception
	 */
	public void clickButton(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Button with compSequence=0(first Button in the cell)*/
		clickButton(rowNum, colNum, 0);
		
	}

	/**
	 *  Clicks the Specified Button at the specified "Row Number" and "Column Number"
	 *  
	 * @param rowNum
	 * @param colName
	 * @param btnName
	 * 			btnName means the text that appears
	 * @throws Exception
	 */
	public void clickButton(int rowNum, String colName, String btnName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click the Button having specefied Link Tag */
		clickButton(rowNum, colNum, btnName);
	}
	
	/**
	 * Clicks Button at the specified "Row Number" and "Column Number based on Sequence number "
	 * 
	 * @param rowNum
	 * @param colName
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickButton(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Click Button with compSequence= any number 0(first Button = 0 , second Button =1)*/
		clickButton(rowNum, colNum, compSequence);
	}
	
	/**
	 *  Clicks the Button at target column, based on the row number of specified sourcolName & rowValue
	 *  
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickButton(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickButton(colNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the Button at target column, based on the row number of specified sourcolNames & rowValues
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @throws Exception
	 */
	public void clickButton(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		clickButton(sourcolNames, rowValues , targetCol, 0);
	}
	
	/**
	 * Clicks the Button at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickButton(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickButton(colNames, rowValues , targetCol, compSequence);
	}

	/**
	 * Clicks the Button at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the sequence number of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param compSequence
	 * 			Sequence number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickButton(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Button baded on RowNumber , Column Number , ImageName*/
		clickButton(rowNum, colNum, compSequence);
	}
	
	/**
	 * Clicks the Button at target column, based on the row number of specified sourcolName & rowValue
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolName
	 * @param rowValue
	 * @param targetCol
	 * @param btnName
	 * 			btnName means the text that appears
	 * @throws Exception
	 */
	public void clickButton(String sourcolName, String rowValue, String targetCol, String btnName) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		clickButton(colNames, rowValues , targetCol, btnName);
	}
	
	/**
	 * Clicks the Button at target column, based on the row number of specified sourcolNames & rowValues
	 * And also depends on the Link Name of the component within the cell
	 * 
	 * @param sourcolNames
	 * @param rowValues
	 * @param targetCol
	 * @param btnName
	 * 			btnName means the text that appears
	 * @throws Exception
	 */
	public void clickButton(String[] sourcolNames, String[] rowValues, String targetCol, String btnName) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Button baded on RowNumber , Column Number , linkName*/
		clickButton(rowNum, colNum, btnName);
	}
	
	
	/**
	 *  Clicks on Button based on the RowNum , Column Number and Image Name
	 *  
	 * @param rowNum
	 * @param colNum
	 * @param btnName
	 * 			btnName means the text that appears
	 * @throws Exception
	 */
	public void clickButton(int rowNum, int colNum, String btnName) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Button Name  :"+btnName);
		
		try{
			String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "button", btnName, null)[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Click Button at "+rowNum+","+colNum);
			}else{
				itv.info("Clicked Button at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Clicks on Button based on the RowNum , Column Number and Component Sequence Number
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param compSequence
	 * 			Sequence Number may be 0 or 1 or 2 etc.
	 * @throws Exception
	 */
	public void clickButton(int rowNum, int colNum, int compSequence) throws Exception{
		
			String errorMessage = "";
			
			System.out.println("Expected Row Num :"+rowNum);
			System.out.println("Expected Column Num :"+colNum);
			System.out.println("Expected compSequence Num  :"+compSequence);
			
			try{
				String ret = doc.executeJsFunction("clickElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "button", "null", Integer.toString(compSequence))[0];
				
				if(ret.equals("False")){
					throw new Exception("Unable to Click Button at "+rowNum+","+colNum);
				}else{
					itv.info("Clicked Button at "+rowNum+","+colNum);
				}
				
			}catch(Exception e){
				throw new Exception(e.getMessage());
			}
	}
	
	
	
	public void setText(int rowNum, int colNum, String valueToEnter) throws Exception{
		
		/* set TextBox with default option set to true */
		setText(rowNum, colNum, valueToEnter, 0);
	
	}

	public void setText(int rowNum, String colName, String valueToEnter) throws Exception{
	
		/* Slect RadioButton with compSequence=0(first textbox in the cell)*/
		setText(rowNum, colName, valueToEnter, 0);
	
	}

	public void setText(int rowNum, String colName, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Slect RadioButton with compSequence=0(first textbox in the cell)*/
		setText(rowNum, colNum, valueToEnter, compSequence);
	
	}

	public void setText(String sourceColName, String rowValue, String targetCol, String valueToEnter) throws Exception{
	
		setText(sourceColName, rowValue , targetCol, valueToEnter, 0);
	}

	public void setText(String sourceColName, String rowValue, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		setText(colNames, rowValues , targetCol, valueToEnter, compSequence);
	}

	public void setText(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter) throws Exception{
	
		setText(sourceColNames, rowValues , targetCol, valueToEnter, 0);
	}

	public void setText(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		setText(rowNum, colNum, valueToEnter, compSequence);
	}

	public void setText(int rowNum, int colNum, String valueToEnter, int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Value to Enter  :"+valueToEnter);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "input", valueToEnter, Integer.toString(compSequence))[0];
			
			//doc.executeJavaScript(js);
			
			if(ret.equals("False")){
				throw new Exception("Unable to Set the Value in the Text Box at "+rowNum+","+colNum);
			}else{
				itv.info("Set the Value in the Text Box at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}

	public String getTextAreaValue(int rowNum, int colNum) throws Exception{
		
		return getTextAreaValue(rowNum ,colNum, 0 );
	}

	public String getTextAreaValue(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getTextAreaValue(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getTextAreaValue(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getTextAreaValue(colNames, rowValues, targetCol, 0 );
	}

	public String getTextAreaValue(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getTextAreaValue(rowNum ,colNum, compSequence );
	}
	
	public String getTextAreaValue(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"textarea",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("TextArea Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("TextArea Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
public String getLink(int rowNum, int colNum) throws Exception{
		
		return getLink(rowNum ,colNum, 0 );
	}

	public String getLink(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getLink(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getLink(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getLink(colNames, rowValues, targetCol, 0 );
	}

	public String getLink(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getLink(rowNum ,colNum, compSequence );
	}
	
	public String getLink(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"a",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("Link Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("Link Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
public String getImage(int rowNum, int colNum) throws Exception{
		
		return getImage(rowNum ,colNum, 0 );
	}

	public String getImage(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getImage(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getImage(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getImage(colNames, rowValues, targetCol, 0 );
	}

	public String getImage(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getImage(rowNum ,colNum, compSequence );
	}
	
	public String getImage(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> checkData = getCellData(rowNum, colNum,"img",Integer.toString(compSequence));
		
		itv.info("Img at "+rowNum+","+colNum+" is "+checkData.toString());
		
		return checkData.get(0);
	}
	
	public String getText(int rowNum, int colNum) throws Exception{
		
		return getText(rowNum ,colNum, 0 );
	}

	public String getText(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getText(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getText(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getText(colNames, rowValues, targetCol, 0 );
	}

	public String getText(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getText(rowNum ,colNum, compSequence );
	}
	
	public String getText(int rowNum, int colNum,  int compSequence) throws Exception{
		
		String value = "";
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> text = getCellData(rowNum, colNum,"input",Integer.toString(compSequence));
		
		if(text.size() == 0){
			itv.info("TextBox Value at "+rowNum+","+colNum+" is : empty");
		}else{
			value = text.get(0);
			itv.info("TextBox Value at "+rowNum+","+colNum+" is "+text.toString());
		}
		
		
		return value;
	}
	
	
	public void setTextArea(int rowNum, int colNum, String valueToEnter) throws Exception{
		
		/* set TextArea with default option set to true */
		setTextArea(rowNum, colNum, valueToEnter, 0);
	
	}

	public void setTextArea(int rowNum, String colName, String valueToEnter) throws Exception{
	
		/* Slect TextArea with compSequence=0(first TextArea in the cell)*/
		setTextArea(rowNum, colName, valueToEnter, 0);
	
	}

	public void setTextArea(int rowNum, String colName, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Set TextArea with compSequence=0(first TextArea in the cell)*/
		setTextArea(rowNum, colNum, valueToEnter, compSequence);
	
	}

	public void setTextArea(String sourceColName, String rowValue, String targetCol, String valueToEnter) throws Exception{
	
		setTextArea(sourceColName, rowValue , targetCol, valueToEnter, 0);
	}

	public void setTextArea(String sourceColName, String rowValue, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		setTextArea(colNames, rowValues , targetCol, valueToEnter, compSequence);
	}

	public void setTextArea(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter) throws Exception{
	
		setTextArea(sourceColNames, rowValues , targetCol, valueToEnter, 0);
	}

	public void setTextArea(String[] sourceColNames, String[] rowValues, String targetCol, String valueToEnter, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		setTextArea(rowNum, colNum, valueToEnter, compSequence);
	}

	public void setTextArea(int rowNum, int colNum, String valueToEnter, int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Value to Enter in Text Area  :"+valueToEnter);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "textarea", valueToEnter, Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Set the Value in the Text Area at "+rowNum+","+colNum);
			}else{
				itv.info("Set the Value in the Text Area at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	
	
	public void selectCheckBox(int rowNum, int colNum) throws Exception{
		
		/* Slect CheckBox with default option set to true */
		selectCheckBox(rowNum, colNum, true);
		
	}
	
	public void selectCheckBox(int rowNum, int colNum, boolean checkCheckBox) throws Exception{
		
		/* Select CheckBox based on compSequence = '0' with the specified option "checkCheckBox"(true or false) */
		selectCheckBox(rowNum, colNum, checkCheckBox, 0);
	}
	
	public void selectCheckBox(int rowNum, String colName) throws Exception{
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colName, true);
		
	}
	
	public void selectCheckBox(int rowNum, String colName, boolean checkCheckBox) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colNum, checkCheckBox,0);
		
	}

	public void selectCheckBox(int rowNum, String colName, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect CheckBox with compSequence=0(first checkbox in the cell)*/
		selectCheckBox(rowNum, colNum, checkCheckBox,compSequence);
		
	}

	public void selectCheckBox(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		selectCheckBox(sourceColName, rowValue , targetCol, true);
	}
	
	public void selectCheckBox(String sourceColName, String rowValue, String targetCol, boolean checkCheckBox) throws Exception{
		
		selectCheckBox(sourceColName, rowValue , targetCol, checkCheckBox,0);
	}
	
	public void selectCheckBox(String sourceColName, String rowValue, String targetCol, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
		
		selectCheckBox(colNames, rowValues , targetCol, checkCheckBox,compSequence);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol) throws Exception{
		
		selectCheckBox(sourceColNames, rowValues , targetCol, true);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol, boolean checkCheckBox) throws Exception{
		
		selectCheckBox(sourceColNames, rowValues , targetCol, checkCheckBox, 0);
	}
	
	public void selectCheckBox(String[] sourceColNames, String[] rowValues, String targetCol, boolean checkCheckBox, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Click Image baded on RowNumber , Column Number , ImageName*/
		selectCheckBox(rowNum, colNum, checkCheckBox, compSequence);
	}
	
	public void selectCheckBox(int rowNum, int colNum, boolean checkCheckBox, int compSequence) throws Exception{
		
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		itv.info("Expected Check Option :"+checkCheckBox);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "checkbox", Boolean.toString(checkCheckBox), Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to check the Check Box at "+rowNum+","+colNum);
			}else{
				itv.info("Checked the Check Box at "+rowNum+","+colNum);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	
	public String getCheckBoxOption(int rowNum, int colNum) throws Exception{
		
		return getCheckBoxOption(rowNum ,colNum, 0 );
	}

	public String getCheckBoxOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getCheckBoxOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getCheckBoxOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getCheckBoxOption(colNames, rowValues, targetCol, 0 );
	}

	public String getCheckBoxOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getCheckBoxOption(rowNum ,colNum, compSequence );
	}
	
	public String getCheckBoxOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> checkData = getCellData(rowNum, colNum,"checkbox",Integer.toString(compSequence));
		
		itv.info("Check Box Status at "+rowNum+","+colNum+" is "+checkData.toString());
		
		return checkData.get(0);
	}
	
	
	
	
	
	
	
	
	public void selectList(int rowNum, int colNum, String itemToSelect) throws Exception{
		
		/* Select SelectBox based on compSequence = '0' with the specified select option */
		selectList(rowNum, colNum, itemToSelect, 0);
	}
	
	
	public void selectList(int rowNum, String colName, String itemToSelect) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect List with compSequence=0(first listbox in the cell)*/
		selectList(rowNum, colNum, itemToSelect,0);
		
	}

	public void selectList(int rowNum, String colName,String itemToSelect, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		/* Slect List with compSequence=0(first selectBox in the cell)*/
		selectList(rowNum, colNum, itemToSelect,compSequence);
		
	}

	
	public void selectList(String sourceColName, String rowValue, String targetCol, String itemToSelect) throws Exception{
		
		selectList(sourceColName, rowValue , targetCol, itemToSelect,0);
	}
	
	public void selectList(String sourceColName, String rowValue, String targetCol, String itemToSelect, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
		
		selectList(colNames, rowValues , targetCol, itemToSelect,compSequence);
	}
	
	public void selectList(String[] sourceColNames, String[] rowValues, String targetCol, String itemToSelect) throws Exception{
		
		selectList(sourceColNames, rowValues , targetCol, itemToSelect, 0);
	}
	
	public void selectList(String[] sourceColNames, String[] rowValues, String targetCol, String itemToSelect, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Select list Item based on RowNumber , Column Number , ImageName*/
		selectList(rowNum, colNum, itemToSelect, compSequence);
	}
	
	public void selectList(int rowNum, int colNum,  String itemToSelect, int compSequence) throws Exception{

		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected Item to Select :"+itemToSelect);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		try{
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "select", itemToSelect, Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Select the Value in the ListBox at "+rowNum+","+colNum);
			}else{
				itv.info("Selected the Value in the ListBox at "+rowNum+","+colNum);
				
				waitForColumn(this.tableColName);
			}
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	public String getSlectedOption(int rowNum, int colNum) throws Exception{
		
		return getSlectedOption(rowNum, colNum, 0 );
	}

	public String getSlectedOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getSlectedOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getSlectedOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getSlectedOption(colNames, rowValues, targetCol, 0 );
	}

	public String getSlectedOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getSlectedOption(rowNum ,colNum, compSequence );
	}
	
	public String getSlectedOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> selectOption = getCellData(rowNum, colNum,"select",Integer.toString(compSequence));
		
		itv.info("Selected List Value at "+rowNum+","+colNum+" is "+selectOption.toString());
		
		return selectOption.get(0);
	}
	
	
	public void selectRadioButton(int rowNum, int colNum) throws Exception{
		
		/* Slect CheckBox with default option set to true */
		selectRadioButton(rowNum, colNum, 0);
	
	}

	public void selectRadioButton(int rowNum, String colName) throws Exception{
	
		/* Slect RadioButton with compSequence=0(first radiobutton in the cell)*/
		selectRadioButton(rowNum, colName, 0);
	
	}

	public void selectRadioButton(int rowNum, String colName, int compSequence) throws Exception{
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
	
		/* Slect RadioButton with compSequence=0(first RadioButton in the cell)*/
		selectRadioButton(rowNum, colNum, compSequence);
	
	}

	public void selectRadioButton(String sourceColName, String rowValue, String targetCol) throws Exception{
	
		selectRadioButton(sourceColName, rowValue , targetCol, 0);
	}

	public void selectRadioButton(String sourceColName, String rowValue, String targetCol, int compSequence) throws Exception{
	
		/* Convert to Array */
		String[] colNames = {sourceColName};
		String[] rowValues = {rowValue};
	
		selectRadioButton(colNames, rowValues , targetCol, compSequence);
	}

	public void selectRadioButton(String[] sourceColNames, String[] rowValues, String targetCol) throws Exception{
	
		selectRadioButton(sourceColNames, rowValues , targetCol, 0);
	}

	public void selectRadioButton(String[] sourceColNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
	
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Select the Radiobutton based on RowNumber , Column Number , ImageName*/
		selectRadioButton(rowNum, colNum, compSequence);
	}

	public void selectRadioButton(int rowNum, int colNum,  int compSequence) throws Exception{
	
		String errorMessage = "";
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		try{
			
			String ret = doc.executeJsFunction("setElement", tableColName, tableIndex, Integer.toString(rowNum), Integer.toString(colNum), "radio", Boolean.toString(true), Integer.toString(compSequence))[0];
			
			if(ret.equals("False")){
				throw new Exception("Unable to Select Radio button at "+rowNum+","+colNum);
			}else{
				itv.info("Selected Radio button at "+rowNum+","+colNum);
			}
			
			
			
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	public String getRadioOption(int rowNum, int colNum) throws Exception{
		
		return getRadioOption(rowNum, colNum, 0 );
	}

	public String getRadioOption(String sourceColName, String rowValue, String targetCol) throws Exception{
		
		return getRadioOption(sourceColName, rowValue, targetCol, 0 );
	}
	
	public String getRadioOption(String sourceColName, String rowValue, String targetCol,  int compSequence) throws Exception{
		
		String colNames[] = {sourceColName};
		String rowValues[] = {rowValue};
		
		return getRadioOption(colNames, rowValues, targetCol, 0 );
	}

	public String getRadioOption(String sourceColNames[], String rowValues[], String targetCol,  int compSequence) throws Exception{
	
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourceColNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		/* Get CheckBox Option */
		return getRadioOption(rowNum ,colNum, compSequence );
	}
	
	public String getRadioOption(int rowNum, int colNum,  int compSequence) throws Exception{
		
		System.out.println("Expected Row Num :"+rowNum);
		System.out.println("Expected Column Num :"+colNum);
		System.out.println("Expected compSequence Num  :"+compSequence);
		
		List<String> radioOption = getCellData(rowNum, colNum,"radio",Integer.toString(compSequence));
		
		itv.info("Radio Status at "+rowNum+","+colNum+" is "+radioOption.toString());
		
		return radioOption.get(0);
	}
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 *  Gets Row Number
	 *  
	 * @param colNames
	 * @param rowValues
	 * @return
	 * @throws Exception
	 */
	public int getRowNumber(String[] colNames, String[] rowValues) throws Exception{
		
		return getRowNumber(colNames, rowValues, true);
	}

	
	/**
	 *  Get Row Number
	 *  
	 * @param colNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public int getRowNumber (String[] colNames, String[] rowValues, boolean partialMatch) throws Exception{
		
		
		/* Initialize Variables*/
		int rowNum = -1;
		boolean rowExists = false;
		int listIndex = 0;
		boolean listboxExists = false;
		
		itv.info("Expected Column Names : "+Arrays.asList(colNames).toString());
		itv.info("Expected Row Values : "+Arrays.asList(rowValues).toString());
		
		if(tableListBoxExists()){
			
			itv.info("Navigating Through Table");
			
			/* Get Table List Count */
			int listCount = getTableListCount();
			
			for(; listIndex < listCount; listIndex++){
				
				/* Select List one by one */
				selectTableList(listIndex);
				
				Thread.sleep(5000);
				
				/* Wait for the Column */
				itv.info("Waiting for a Column :"+colNames[0]+" after selecting the list box.");
				waitForColumn(colNames[0]);
				
				doc.executeJavaScript(js);
				
				/* Get the Row Number */
				rowNum = getRowNum(colNames , rowValues , partialMatch);
				
				if(rowNum > 0 ){
					break;
				}
				
				//itv.info("rowNum :"+rowNum);
				
				listCount = getTableListCount();
				
				//itv.info("listCount"+listCount);
			}
			
		} else {

			/* Get the Row Number */
			rowNum = getRowNum(colNames , rowValues , partialMatch);
		}	
			
		itv.info("Row Number in the Table :\""+(listIndex+1)+"\" is : "+rowNum);
		
		return rowNum;
	}
	
	/**
	 * 
	 * @param tablePath
	 * @param colNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public int getRowNum(String[] colNames, String[] rowValues, boolean partialMatch) throws Exception{
		
		System.out.println("****************** Start of Row Num ************************ ");
		
		int rowNumber = -1;
		boolean itemFound = false;
		
		/* Get Column Names*/
		String[] actualColNames = this.colNames;
		List<String> lActualColNames = Arrays.asList(actualColNames);
		
		/* Get Row Count*/
		
		int rowCount = getRowCount();
		int rowStartIndex = getRowStartIndex();
		rowCount = rowCount-rowStartIndex+1;
		
		System.out.println("Row Count is : "+(rowCount));
		System.out.println("Row Start index is : "+(rowStartIndex));
		
		/* Navigate through each row for getting cell value 
		 * rowIndex start with "2" (since the rownum =1 starts with headers)
		 * */
		for(int rowIndex=1; rowIndex < rowCount; rowIndex++){
			
			/* Verification of Data in the Specified Row*/
			for(int expColIndex = 0 ; expColIndex < colNames.length ; expColIndex++){
				
				int colIndex = getColumnNumber(colNames[expColIndex]);
				
				/*int colIndex = -1;
				if(!isANumber(colNames[expColIndex].trim() )){
					colIndex	= lActualColNames.indexOf(colNames[expColIndex]);
				}else{
				
					colIndex = Integer.parseInt(colNames[expColIndex].trim()); //
				}*/
				
				List<String> cellData = new ArrayList<String>();
				
				/*displayTime("Table GetCell Method : start");
				String cellValue = web.table(tablePath).getCell(rowIndex, colIndex+1);
				displayTime("Table GetCell Method : end");
				
				if(cellValue != null){
					cellData.add(cellValue);
				}else{
					 Get the Cell Value -- Here colIndex starts with "0"
					displayTime("Custom GetCell Method : start");
					cellData = getCellData(rowIndex-1, colIndex+1);
					displayTime("Custom GetCell Method : end");
				}*/
				
				/* Get the Cell Value -- Here colIndex starts with "0"*/
				cellData = getCellData(rowIndex, colIndex);
				//System.out.println("rowIndex:"+rowIndex);
				System.out.println("cell Data at "+rowIndex+","+colIndex+" is :"+cellData.toString());
				
				/* Verification of actual Cell value with expected Cell Value
				 * If value is not matched then it comes out of verification "for" loop
				 * */
				if(partialMatch){
					
					boolean itemAvailable = false;
					for(int cellItemIndex=0; cellItemIndex<cellData.size();cellItemIndex++){
						if(compareStrings(rowValues[expColIndex],cellData.get(cellItemIndex))){
							itemAvailable = true;
							break;
						}
					}
					if(!itemAvailable)
					{
						break;
					}
				}else{
					if(!cellData.contains(rowValues[expColIndex]))
					{
						break;
					}
				}
				if(expColIndex == (colNames.length - 1)){
					itemFound = true;
				}
			}
			
			/* If Item is not found , Comes out of the For and then return -1 as row number
			 * If Item found , then returns the RowNumber;
			 * */
			if(itemFound){
				rowNumber = rowIndex;
				break;
			}
		}
		
		return (rowNumber);
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	

	
	
	
	
	
	public String getComponentAvailability(int rowIndex, int columnIndex, String compType) throws Exception{
		
		String ret = doc.executeJsFunction("checkCompExistance", tableColName, tableIndex, Integer.toString(rowIndex), Integer.toString(columnIndex), compType)[0];
		return ret;
	}
	
	
	
	
	
	public List<String> getCellData(int rowIndex, int ColumnIndex) throws Exception {
		
		return getCellData(rowIndex, ColumnIndex, "0");
	}
	
	
	public List<String> getCellData(int rowNum, String colName) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum, "0");
	}
	
	public List<String> getCellData(int rowNum, String colName, String compType) throws Exception{
		
		return getCellData(rowNum, colName, compType, "0");
	}
	
	public List<String> getCellData(int rowNum, String colName, String compType, String compIndex) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum, compType, compIndex);
	}	
	
	public List<String> getCellData(int rowNum, String colName, int compSequence) throws Exception{
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(colName);
		
		return getCellData(rowNum, colNum,Integer.toString(compSequence));
	}	
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues , targetCol, 0);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol) throws Exception{
		
		return getCellData(sourcolNames, rowValues , targetCol, 0);
	}
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol, int compSequence) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues , targetCol, compSequence);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol, int compSequence) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		return getCellData(rowNum, colNum, Integer.toString(compSequence));
	}
	
	
	public List<String> getCellData(String sourcolName, String rowValue, String targetCol, String compType) throws Exception{
		
		/* Convert to Array */
		String[] colNames = {sourcolName};
		String[] rowValues = {rowValue};
		
		return getCellData(colNames, rowValues, targetCol, compType);
	}
	
	public List<String> getCellData(String[] sourcolNames, String[] rowValues, String targetCol, String compType) throws Exception{
		
		/* Get the Matched Row Number*/
		int rowNum = getRowNumber(sourcolNames, rowValues);
		
		/* Get the Matched Column Number*/
		int colNum = getColumnNumber(targetCol);
		
		return getCellData(rowNum, colNum, compType, "0");
	}


	public List<String> getCellData(int rowIndex, int ColumnIndex, String compIndex) throws Exception {
		
		//System.out.println("Expected Row Num :"+rowIndex);
		//System.out.println("Expected Column Num :"+ColumnIndex);
		
		List<String> cellData = new ArrayList<String>();
		
		for(int index=0;index<defComponents.length;index++){
			
			String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), defComponents[index], compIndex);
			
			for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
				if(data[dataIndex] != ""){
					
					String removedNewLineString = data[dataIndex];
					String splittedString = "";
					
					//For new line -- we can replace string with \n & \r
					removedNewLineString = removedNewLineString.replaceAll("\n", "");
					removedNewLineString = removedNewLineString.replaceAll("\r", " ");
					
					cellData.add(removedNewLineString);
				}
			}
		}
		
		return cellData;
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String compType, String compIndex) throws Exception {
		
		//System.out.println("Expected Row Num :"+rowIndex);
		//System.out.println("Expected Column Num :"+ColumnIndex);
		
		List<String> cellData = new ArrayList<String>();
		
		String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), compType, compIndex);
		
		for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
			if(data[dataIndex] != ""){
				cellData.add(data[dataIndex]);
			}
		}
		
		return cellData;
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String[] expCompTypes) throws Exception {
		
		return getCellData(rowIndex, ColumnIndex, expCompTypes, "0");
	}
	
	public List<String> getCellData(int rowIndex, int ColumnIndex, String[] expCompTypes, String compIndex) throws Exception {
		
		//System.out.println("Expected Row Num :"+rowIndex);
		//System.out.println("Expected Column Num :"+ColumnIndex);
		
		List<String> cellData = new ArrayList<String>();
		
		for(int index=0;index<expCompTypes.length;index++){
			
			String[] data = getValueByType(Integer.toString(rowIndex), Integer.toString(ColumnIndex), expCompTypes[index], compIndex);
			
			for(int dataIndex = 0; dataIndex<data.length; dataIndex++){
				if(data[dataIndex] != ""){
					cellData.add(data[dataIndex]);
				}
			}
		}
		
		return cellData;
	}
	
	/**
	 *  Verifies Row Data
	 *  
	 * @param colName
	 * @param rowValue
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String colName, String rowValue) throws Exception {
		
		return verifyRowData(colName , rowValue ,  true);
		
	}
	
	/**
	 *  Verifies Row Data
	 *   
	 * @param colName
	 * @param rowValue
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String colName, String rowValue , boolean partialMatch) throws Exception {
	
		String[] columnNames = {colName};
		String[] rowValues = {rowValue};
		
		return verifyRowData(columnNames , rowValues ,  partialMatch);
	
	}
	
	/**
	 * Verifies Row Data
	 *  
	 * @param columnNames
	 * @param rowValues
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String[] columnNames, String[] rowValues) throws Exception {
		
		return verifyRowData(columnNames , rowValues ,  true);
		
	}

	/**
	 * 
	 * Verifies Row Data
	 * 
	 * @param columnNames
	 * @param rowValues
	 * @param partialMatch
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRowData(String[] columnNames, String[] rowValues , boolean partialMatch) throws Exception {
		
		System.out.println("*** Verifying Row Data  ******");
		System.out.println("Class Name : \"Table\" and Method Name : \" verifyRowData() \" ");
		
		/* Initialize Variables*/
		boolean rowExists = false;
		
		/* Get Row Data */
		int rowNum = getRowNumber(columnNames , rowValues , partialMatch);
		
		/**
		 * If Row Num is less than "1" means specified data is not there in the table
		 * 
		 * If Row Num greater than equal to "1" means specified data is available in the table
		 */
		if(rowNum < 1) {
			rowExists = false;
		}else{
			rowExists = true;
		}
		
		if(rowExists){
			itv.info("Specified data exists at row number : " +rowNum);
		}else{
			itv.info("Specified data doesnot exists");
		}
		
		return rowExists;
	}
	
	
	
	public boolean verifyTableData(String colName, String[][] expectedTableData) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = defComponents;
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}
	
	public boolean verifyTableData(String colName, String[][] expectedTableData, String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = {expComponent};
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String[] colNames, String[][] expectedTableData , String[] sExpComponents) throws Exception {
		
		/* Table Data in HashMap */
		HashMap<String,List<String>> expectedTableLinesData = new HashMap<String,List<String>>();
		
		/* Convert all the array data into a HashMap */
		for(int hashIndex=1,index=0; index<expectedTableData.length; index++,hashIndex++){
		
			List<String> row = new ArrayList<String>();
			
			for(int colIndex=0;colIndex<expectedTableData[index].length;colIndex++){
				row.add(expectedTableData[index][colIndex]);
			}
			
			expectedTableLinesData.put(Integer.toString(hashIndex),row);
		}
		
		return verifyTableData(colNames , expectedTableLinesData , sExpComponents);
	}

	
	public boolean verifyTableData(String colName, HashMap<String,List<String>> expectedTableData) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = defComponents;
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String colName, HashMap<String,List<String>> expectedTableData, String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] sExpComponents = {expComponent};
		return verifyTableData(colNames , expectedTableData , sExpComponents);
		
	}

	public boolean verifyTableData(String[] colNames, HashMap<String,List<String>> expectedTableData , String[] sExpComponents) throws Exception {
		
		/* Initialize Data */
		HashMap<String,HashMap<String,List<String>>> tableData = new HashMap<String,HashMap<String,List<String>>>();
		HashMap<String,List<String>> nonVerifiedData = new HashMap<String,List<String>>();
		
		if(tableListBoxExists()){
			
			itv.info("Navigating Through Table");
			
			/* Get Table List Count */
			int listCount = getTableListCount();
			int listIndex = 0;
			
			for(; listIndex < listCount; listIndex++){
				
				/* Select List one by one */
				selectTableList(listIndex);

				/* Wait for the Column */
				waitForColumn(colNames[0]);
				
				/* Get the Row Number */
				tableData = getTableData(colNames, sExpComponents);
				
				/* Verify Table Data*/
				nonVerifiedData = verifyData(tableData, expectedTableData, colNames);
				
				/* Change Expected Data*/
				if(nonVerifiedData.size() > 0){
					expectedTableData = null;
					expectedTableData = nonVerifiedData;
				}else{
					break;
				}
				
				listCount = getTableListCount();
			}
			
		} else {
			
			/* Get the Row Number */
			tableData = getTableData(colNames, sExpComponents);
			itv.info("Actual Number of Rows in the Table :"+tableData.size());
			itv.info("Expected Number of Rows to verify :"+expectedTableData.size());
			
			/* Verify Table Data*/
			nonVerifiedData = verifyData(tableData, expectedTableData,colNames);
		}
		
		boolean dataVerified = false ;
		
		if(nonVerifiedData.size() == 0){
			dataVerified = true;
		}else{
			
			/* Row Number Keyset*/
			List<String> rowNum = convertStringArrayToList(nonVerifiedData.keySet().toArray());
			
			itv.info("Row(s) Data which is not available in the Table :");
			for(int index=0;index < rowNum.size();index++){
				List<String> colData = nonVerifiedData.get(rowNum.get(index));
				itv.warn(colData.toString());
			}
			dataVerified = false;
		}
		return dataVerified;
	
	}
	
	
	 public boolean verifyTableWithExactRowData(String[] colNames, HashMap<String,List<String>> expectedTableData , String[] sExpComponents) throws Exception {
			
			/* Initialize Data */
			HashMap<String,HashMap<String,List<String>>> tableData = new HashMap<String,HashMap<String,List<String>>>();
			HashMap<String,List<String>> nonVerifiedData = new HashMap<String,List<String>>();
			int expIndex=1, tablIndex=0;
			
			itv.info("*************** Expected Rows To Verify : "+expectedTableData.size()+" ************");
			if(tableListBoxExists()){
				
				itv.info("Navigating Through Table");
				
				/* Get Table List Count */
				int listCount = getTableListCount();
				int listIndex = 0;
				
				for(; listIndex < listCount; listIndex++){
					
					/* Select List one by one */
					selectTableList(listIndex);
					
					Thread.sleep(5000);
					
					/* Wait for the Column */
					waitForColumn(colNames[0]);
					
					doc.executeJavaScript(js);
					
					/* Get the Row Number */
					tableData = getTableData(colNames, sExpComponents);
					
					itv.info("*************** Actual Row Data From the Table ************");
					for(int i=0; i<tableData.size(); i++)
					{
						itv.info("row number "+ (tablIndex+i+1)+ "     "+tableData.get(""+(i+1)));
					}
					
					tablIndex=tablIndex+tableData.size();
					
					itv.info("***************Verification Actual Table Data with Expected Data ************");
					expIndex=verifyExactRowData(tableData ,expectedTableData, colNames, tableData.size(),expIndex);
					
					listCount = getTableListCount();
				}
				
			} else{
				
				/* Get the Row Number */
				tableData = getTableData(colNames, sExpComponents);
				tablIndex=tablIndex+tableData.size();
				
				itv.info("*************** Actual Row Data From the Table ************");
				for(int i=0; i<tableData.size(); i++)
				{
					itv.info("row number "+ (i+1)+ "     "+tableData.get(""+(i+1)));
				}
				
				//itv.info("excel size"+expectedTableData.size());
				itv.info("***************Verification Actual Table Data with Expected Data ************");
				expIndex = verifyExactRowData(tableData ,expectedTableData, colNames, tableData.size(),expIndex);
				
				if(expIndex<=expectedTableData.size())
				{
					itv.info(" row number "+ expIndex + "is  not found  in web table ");
					return false;
				}
				
			}
			return true;
		
		}
	
	public int  verifyExactRowData(HashMap<String,HashMap<String,List<String>>> actualTableData ,HashMap<String,List<String>> expectedTableData, String[] colNames, int tablesize, int expIndex ) throws Exception
	{
		int expExcelIndex = expIndex;
		int actTableIndex = 1;
		//expIndex = expIndex + actualTableData.size()-1;
		int tableSize = expIndex + actualTableData.size()-1;
		
		/*itv.info("******************* actualTableData:"+actualTableData.size()+"  **************");
		itv.info("******************* expIndex:"+expIndex+"  **************");
		itv.info("******************* tableSize:"+tableSize+"  **************");
		itv.info("******************* expectedTableData:"+expectedTableData.size()+"  **************");*/
		for(;expIndex<=expectedTableData.size();expIndex++,actTableIndex++){
				
			if(expIndex > tableSize)
			{
				return expIndex;
			}
			
			itv.info("******************* Row  Number:"+expIndex+"  **************");
			
			List<String> expRowData = expectedTableData.get(Integer.toString(expIndex));
			itv.info("Expected Row :"+expIndex+ " data to verify :"+expRowData.toString());
//			
//			actualTableDataToVerify = actualTableData;
//			actualTableData = new HashMap<String,HashMap<String,List<String>>>();
			boolean rowVerified = false;
			
			/* Get Row Data */
			//int rowIndex = index+1;
			HashMap<String, List<String>> actRowData = actualTableData.get((actTableIndex+""));
			
			//itv.info("actRowData-- ");
			itv.info("Actual Row :"+expIndex+ " data to verify :"+actRowData.toString());
			
			boolean cellVerified = false;
			for(int expColIndex=0; expColIndex < expRowData.size(); expColIndex++){
				
				/* Get Expected Cell Data */
				String expCellVal = expRowData.get(expColIndex);
				//System.out.println("expCellVal_verifyData : "+expCellVal);
				
				
				
				/* Get Actual Cell Data */
				//itv.info(colNames[expColIndex]);
				List<String> cellData  = actRowData.get(colNames[expColIndex]);
				//System.out.println("actCellData_verifyData : "+cellData.toString());
				
				boolean cellDataExists = false;
				for(int cellDataIdex=0;cellDataIdex<cellData.size();cellDataIdex++){
					
					cellDataExists = compareStrings(expCellVal,cellData.get(cellDataIdex));
					
					if(cellDataExists){
						break;
					}
				}
				
				if((expCellVal == null || expCellVal == "") && cellData.size() == 0){
					cellDataExists = true;
			    }
				
				
				if(cellDataExists)
				{
					itv.info("*** Column :"+colNames[expColIndex]+"*** Expected value \""+expCellVal+"\" matches with Actual value :"+cellData.toString());
					//itv.info("web column  and excel column  value are matched");
				}
				else
				{
					itv.reportFailure("*** Column :"+colNames[expColIndex]+"*** Expected value \""+expCellVal+"\" does not match with Actual value :"+cellData.toString());
					//itv.warn("web column  and excel column  value are not matched");
					//itv.reportFailure("web column  and excel column  value are not matched");
				}	
			}	
		}
		
		return  expIndex;
	}
	
	
	
	
	public HashMap<String,List<String>> verifyData(HashMap<String,HashMap<String,List<String>>> actualTableData ,HashMap<String,List<String>> expectedTableData, String[] colNames ) throws AbstractScriptException{
		
		HashMap<String,List<String>> verifiedTableData = new HashMap<String,List<String>>();
		HashMap<String,List<String>> nonVerifiedTableData = new HashMap<String,List<String>>();
		HashMap<String,HashMap<String,List<String>>> actualTableDataToVerify = new HashMap<String,HashMap<String,List<String>>>();
		
		for(int expIndex=0,verifiedIndex=1, notVerifiedIndex=1;expIndex<expectedTableData.size();expIndex++){
			
			/* Get Row Data from expected Data */
			int expRowIndex = expIndex+1;
			List<String> expRowData = expectedTableData.get(Integer.toString(expRowIndex));
			System.out.println("Expected Row"+expRowIndex+ "Data to verify :"+expRowData.toString());
			
			actualTableDataToVerify = actualTableData;
			actualTableData = new HashMap<String,HashMap<String,List<String>>>();
			boolean rowVerified = false;
			for(int index=0,yetToVerify=1;index<actualTableDataToVerify.size();index++){
				
				/* Get Row Data */
				int rowIndex = index+1;
				HashMap<String, List<String>> actRowData = actualTableDataToVerify.get(Integer.toString(rowIndex));
				
				boolean cellVerified = false;
				for(int expColIndex=0; expColIndex < expRowData.size(); expColIndex++){
					
					/* Get Expected Cell Data */
					String expCellVal = expRowData.get(expColIndex);
					System.out.println("expCellVal_verifyData : "+expCellVal);
					
					if(expCellVal == null || expCellVal == ""){
						continue;
					}
					
					/* Get Actual Cell Data */
					List<String> cellData  = actRowData.get(colNames[expColIndex]);
					System.out.println("actCellData_verifyData : "+cellData.toString());
					
					boolean cellDataExists = false;
					for(int cellDataIdex=0;cellDataIdex<cellData.size();cellDataIdex++){
						cellDataExists = compareStrings(expCellVal,cellData.get(cellDataIdex));
						if(cellDataExists){
							break;
						}
					}
					
					if(cellDataExists){
						cellVerified = true;
					}else{
						cellVerified = false;
						break;
					}
				}
				/* Checking row is verified completely or not
				 * If Yes, Go with Verification of another row
				 * */
				if(cellVerified){
					rowVerified = true;
					
					for(index =index+1;index<actualTableDataToVerify.size();index++){
						
						rowIndex = index+1;
						actRowData = actualTableDataToVerify.get(Integer.toString(rowIndex));
						
						actualTableData.put(Integer.toString(yetToVerify), actRowData);
						yetToVerify++;
					}
					break;
					
					
				}else{
					actualTableData.put(Integer.toString(yetToVerify), actRowData);
					yetToVerify++;
				}
			}
			
			if(rowVerified){
				itv.info("Verified Data: "+expRowData.toString());
				verifiedTableData.put(Integer.toString(verifiedIndex), expRowData);
				verifiedIndex++;
			}else{
				System.out.println("Non Verified Data: "+expRowData.toString());
				nonVerifiedTableData.put(Integer.toString(notVerifiedIndex), expRowData);
				notVerifiedIndex++;
			}
		}
		
		System.out.println("Size of nonVerifiedTableData :"+nonVerifiedTableData.size());
		return nonVerifiedTableData;
	}
	
	/**
	 *  Get Table Data (rowNumber, {ColName,Data})
	 *  
	 * @param colNames
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,HashMap<String,List<String>>> getTableData(String[] colNames) throws Exception {
		
		return getTableData(colNames , defComponents);
	}
	
	/**
	 *  Get Table Data (rowNumber, {ColName,Data})
	 *  
	 * @param colName
	 * @param expComponent
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,HashMap<String,List<String>>> getTableData(String colName , String expComponent) throws Exception {
		
		String[] colNames = {colName};
		String[] expComponents = {expComponent};
		
		return getTableData(colNames, expComponents);
	}	
		
		/**
		 *  Get Table Data (rowNumber, {ColName,Data})
		 *  
		 * @param tablePath
		 * @param colNames
		 * @return
		 * @throws Exception
		 */
		public HashMap<String,HashMap<String,List<String>>> getTableData(String[] colNames , String[] expComponents) throws Exception {
			
			/* Initialize Data */
			HashMap<String,HashMap<String,List<String>>> tableData = new HashMap<String,HashMap<String,List<String>>>();
			
			/* Get Row Count*/
			int rowCount = getRowCount();
			int rowStartIndex = getRowStartIndex();
			rowCount = rowCount-rowStartIndex+1;
			//itv.info("Row Count is : "+rowCount);
			
			/* Get Column Names*/
			String[] actualColNames = this.colNames;
			List<String> lActualColNames = Arrays.asList(actualColNames);
			
			/* RowCount includes "Header also"*/
			for(int rowIndex=1;rowIndex<rowCount;rowIndex++){
				
				/* Get the Data of the specified Columns in a row */
				HashMap<String,List<String>> rowData = new HashMap<String,List<String>>();
				for(int expColIndex =0;expColIndex <colNames.length;expColIndex ++){
					
					/* Expected Column Name */
					String colName = colNames[expColIndex];
					
					/* Expected Column Index */
					int colIndex = lActualColNames.indexOf(colName);
					
					/* Get the Cell Data */
					List<String> cellData = new ArrayList<String>();
					if(expComponents == null){
						cellData = getCellData(rowIndex, colIndex);
					}else{
						cellData = getCellData(rowIndex, colIndex, expComponents);
					}
					
					
					rowData.put(colName, cellData);
				}
				
				tableData.put(Integer.toString(rowIndex), rowData);
			}
			return tableData;
		}	
		

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String[] getValueByType(String rowIndex, String columnIndex, String tag, String compIndex)throws Exception
	{
		String[] ret = doc.executeJsFunction("getValueByType", tableColName, tableIndex, rowIndex, columnIndex, tag, compIndex);
		
		return ret;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getCurrentWindowXPath() throws Exception {
		
		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String xPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
		
		return xPath;
	}
	
	public String getCurrentWindowTitle() throws Exception {
		
		String title = web.getFocusedWindow().getTitle();
		
		return title;
	}
	
	public void waitForText(String text) throws Exception {
		
		waitForText(text, "span");
		
	}
	
	public void waitForText(String text, String compType) throws Exception {
		
		/* Document Path */
		String windowPath = getCurrentWindowXPath();
		
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		//String docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']"; 
		String docPath = windowPath + "/web:document[@index='0']";
		
		/* Get Text Path */
		String textPath = docPath + "/web:"+compType+"[@text='"+text+"']";
		
		/* Wait for text to present */
		web.element(textPath).waitFor(SYNCTIME);
	}
	
	public void waitForColumn(String columnName) throws Exception {
		
		/* Document Path */
		//String windowPath = getCurrentWindowXPath();
		
		//int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		//String docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']"; 
		//String docPath = windowPath + "/web:document[@index='0']"; 
		
		/* Get Text Path */
		int colIndex = getColumnNumber(columnName.trim());
		
		columnName = this.colNamesWithoutTrim[colIndex];
		
		itv.info("Waiting for Column");
		/*
		 *  Requirements:
		 * 1. Some tables have column name as "Part Number "   (space after column name)
		 * 
		 * 
		 * 
		 */
		String textPath = docPath + "/web:th[@text='"+columnName+"*']";
		
		if(!web.exists(textPath,10)){
			textPath = docPath + "/web:td[@text='"+columnName+"*']";
		}
		
		/* Wait for text to present */
		itv.info("column path :"+textPath);
		web.element(textPath).waitFor(SYNCTIME);
		
		web.element(textPath).waitFor(SYNCTIME);
		
		doc.executeJavaScript(js);
	}
	
	public boolean tableListBoxExists() throws Exception {
		
		return tableListBoxExists(tableListPath);
	}
	
	
	public boolean tableListBoxExists(String xPath) throws Exception {
		
		boolean listBoxExists = false;
		
		// Check listbox availability with JavaScript
		if("".equalsIgnoreCase(tableListPath) || tableListPath == null){
			
			//itv.info("Checking Table Navigation List Box through JavaScript");
			
			String[] select = doc.executeJsFunction("listBoxExists", this.tableColName, this.tableIndex, "javascript");
			
			//System.out.println("Table Navigation List Box Exists :"+select[0]);
			
			String listItem = "false";
			if(select[0].startsWith("javascript")){
				listItem = select[0].split(":")[1].trim();
			}else{
				listItem = select[0];
			}
			itv.info("List Availability :"+listItem);
			
			listBoxExists = Boolean.parseBoolean(listItem);
			
		}else{
			//itv.info("Table Navigation List Box xPath :"+xPath);
			
			if(web.exists(xPath, 10)){
				listBoxExists =  true;
			}
		}
		
		/*if(listBoxExists){
			System.out.println("Table Navigation List Box Exists");
		}else{
			System.out.println("Table Navigation List Box is not available");
		}*/
		
		return listBoxExists;
	}
	
	/*public boolean tableListBoxExists(String xPath) throws Exception {
		
		boolean listBoxExists = false;
		
		System.out.println("xPath :"+xPath);
		if(web.exists(xPath, 10)){
			listBoxExists =  true;
			System.out.println("List Box Exists");
		}
		
		return listBoxExists;
	}*/
	
	/*public boolean tableListBoxExists() throws Exception {
		
		boolean listBoxExists = false;
		
		if("".equalsIgnoreCase(tableListPath) || tableListPath == null){
			return listBoxExists;
		}
		
		if(web.exists(tableListPath, 10)){
			listBoxExists =  true;
		}
		
		return listBoxExists;
	}*/
	
	public int getTableListCount() throws Exception {
		
		int listCount = 0;
		
		if("".equalsIgnoreCase(tableListPath) || tableListPath == null){
			
			String[] sListCount = doc.executeJsFunction("listCount", this.tableColName, this.tableIndex);
			
			listCount = Integer.parseInt(sListCount[0]);
			
		}else{
			listCount = getListItems(tableListPath).size();
		}
		
		itv.info("Available Table List Items Count :"+listCount);
		
		return listCount;
	}
	
	/*public int getTableListCount() throws Exception {
		
		int listCount = getListItems(tableListPath).size();
		
		//itv.info("Available Table List Items Count :"+listCount);
		
		return listCount;
	}*/
	
	
	public void selectTableList(int listIndex) throws Exception {
		
		// Selecting listbox with JavaScript
		if("".equalsIgnoreCase(tableListPath) || tableListPath == null){
			
			if(listIndex == 0){
				
				itv.info("Selecting List Box Index through JavaScript :"+listIndex);
				
				doc.executeJsFunction("selectListBoxElement", this.tableColName, this.tableIndex,"1");
				
				Thread.sleep(5000);
				
				doc.executeJavaScript(js);
				
				doc.executeJsFunction("selectListBoxElement", this.tableColName, this.tableIndex,"0");
			}else{
				
				String index = listIndex+"";
				
				itv.info("Selecting List Box Index through JavaScript :"+index);
				doc.executeJsFunction("selectListBoxElement", this.tableColName, this.tableIndex,index.trim());
			}
		}else{
			
			// itv.info("Available Table List Path :"+tableListPath);
			
			if(listIndex == 0){
				itv.info("Index :"+listIndex);
				web.selectBox(tableListPath).selectOptionByIndex(1);
				web.selectBox(tableListPath).fireEvent("blur");
				Thread.sleep(5000);
				web.selectBox(tableListPath).selectOptionByIndex(0);
				
			}else{
				web.selectBox(tableListPath).selectOptionByIndex(listIndex);
			}
			
			web.selectBox(tableListPath).fireEvent("blur");
		}
	}
	
	
	
	/*public void selectTableList(int listIndex) throws Exception {
		
		//itv.info("Selection of Table Navigation ListBox having the index :"+listIndex);
		
		 Select the select box  one by one
		itv.info("Available Table List Path :"+tableListPath);
		//web.selectBox(tableListPath).focus();
		//web.selectBox(tableListPath).dblClick();
		//web.selectBox(tableListPath).fireEvent("onfocus");
		
		if(listIndex == 0){
			itv.info("Index :"+listIndex);
			web.selectBox(tableListPath).selectOptionByIndex(1);
			web.selectBox(tableListPath).fireEvent("blur");
			Thread.sleep(5000);
			web.selectBox(tableListPath).selectOptionByIndex(0);
			
		}else{
			web.selectBox(tableListPath).selectOptionByIndex(listIndex);
		}
		
		web.selectBox(tableListPath).fireEvent("blur");
		//web.selectBox(tableListPath).fireEvent("onchange");	
		//web.selectBox(tableListPath).fireEvent("onclick");
	}*/
	
	/**
	 *  Get the Items From the List Box
	 *  
	 * @param xPath		ListBox Xpath
	 * @return 			List of Items
	 * @throws Exception
	 */
	public List<String> getListItems(String xPath) throws Exception{
		
		List<String> actualItems = new ArrayList<String>();
		List<DOMElement> actualAttributes = web.selectBox(xPath).getOptions();
		
		/* Get the Elements one by one from the List Box*/
		for(int index = 0;index < actualAttributes.size();index++){
			actualItems.add(actualAttributes.get(index).getAttribute("text"));
		}
		
		return actualItems ;
	}
	
	public List<String> getHashMapKeySet(HashMap<String,String> hashmap){
		
		/* Get KeySet as Objects */
		Object[] keyObjects  = hashmap.keySet().toArray();
		
		/* Get KeySet as Array */
		String[] keys = null;
		keys = Arrays.asList(keyObjects).toArray(new String[keyObjects.length]);
		
		/* Get KeySet as List */
		List<String> keySet = Arrays.asList(keys);
		
		return keySet;
	}
	
	public List<String> convertStringArrayToList(Object[] objects){
		
		/* Get KeySet as Array */
		String[] keys = null;
		keys = Arrays.asList(objects).toArray(new String[objects.length]);
		
		/* Get KeySet as List */
		List<String> keySet = Arrays.asList(keys);
		
		
		return keySet;
	}
	
	public List<String> getHashMapValues(HashMap<String,String> hashmap){
		
		/* Get KeySet as Objects */
		Object[] valueObjects  = hashmap.values().toArray();
		
		/* Get KeySet as Array */
		String[] values = null;
		values = Arrays.asList(valueObjects).toArray(new String[valueObjects.length]);
		
		/* Get KeySet as List */
		List<String> valueSet = Arrays.asList(values);
		
		return valueSet;
	}
	
	public boolean compareStrings(String expectedString, String actualString){
		
		boolean stringMatched = false;
		Pattern pattern = Pattern.compile(expectedString);
		Matcher matcher = pattern.matcher(actualString.trim());

       if (matcher.matches()) {
       	stringMatched = true;
       } 
       
		return stringMatched;
	}
	
	
	public boolean isANumber(String input){
		
		Pattern p = Pattern.compile( "([0-9]*)" );

		Matcher m = p.matcher(input);
		
		return m.matches();
	}
	
}





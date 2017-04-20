import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	@ScriptService oracle.oats.scripting.modules.datatable.api.DataTableService datatable;
	@FunctionLibrary("GENLIB") lib.ebsqafwk.GENLIB gENLIB;
	@FunctionLibrary("WEBTABLELIB") lib.ebsqafwk.WEBTABLELIB wEBTABLELIB;

	public void initialize() throws Exception {
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {
		

	}

	public void finish() throws Exception {
	}
	
	
	private String getExcelLocation(String ExcelName) throws AbstractScriptException {
		String delimeter = "";

		String scriptFolder = getVariables().get("CURR_SCRIPT_FOLDER");
		
		if (scriptFolder.contains("\\"))
			delimeter = "\\";
		if (scriptFolder.contains("/"))
			delimeter = "/";

		int lastDelimeterIndex = scriptFolder.substring(0,	scriptFolder.length() - 2).lastIndexOf(	delimeter);

		info("" + scriptFolder.substring(0,	lastDelimeterIndex + 1));
		
		String excelLocation = scriptFolder.substring(0,lastDelimeterIndex + 1) + "Attachements" + delimeter + ExcelName+".xls";;
		
		return excelLocation;
	}
	
	/*List <String[]> act    =    new ArrayList<String[]>();
	act.add(new String[]{"VERIFY","EDIT","WEBCOL;EXCECOL1","TEST_COL1_0"});
	act.add(new String[]{"VERIFY","TEXT","WEBCOL;EXCECO2","TEST_COL2_0"});
	act.add(new String[]{"VERIFY","TEXT","WEBCOL;EXCECOL3","TEST_COL3_0"});*/

	public boolean spreadVerifyTable(String excelName, String sheetName, String spreadTableAttribute, List<String[]> verifycolumnsNames) throws AbstractScriptException {
		
		List<String> sheetNames = new ArrayList<String>();
		boolean columnsVerifyDataCheck = true;
		
		//Import Excel Sheet
		sheetNames.add(sheetName);
		datatable.importSheets(excelName,sheetNames,true,true);
		
		// Get the Column Iterator
		Iterator<String[]> verifycolumnsNamesiterator = verifycolumnsNames.iterator();
		
		
		while (verifycolumnsNamesiterator.hasNext()) {
			// Get the First Record : {"VERIFY","EDIT","WEBCOL;EXCECOL1","TEST_COL1_0"}
			String[] record = (String[]) verifycolumnsNamesiterator.next();

			String excelColumnName = record[2].split(";")[1];
			String tblColumnName =  record[2].split(";")[0];
			
			/*if (!spreadVerifycolumsdata(spreadTableAttribute, tblColumnName, record[2],record[3],record[1],sheetName))
				columnsVerifyDataCheck = false;*/

		}
		
		return columnsVerifyDataCheck;
	}
	
	private boolean spreadVerifyColumsdata( String excelColumnName,String fieldName,String fieldType,String sheetName,String spreadTableAttribute) throws AbstractScriptException
	{    
		
		int j=9;
	   boolean columnsVerifyDataCheck=true;
	   String  fieldValue,XpathText,xpathWithName;
	   
	   // Excel Column Name
	   String excelColName=excelColumnName;
	   
	   //
	   String fieldNameWithoutNumber=fieldName.substring(0,fieldName.length()-1);
	   XpathText=getFieldTypeXpath(fieldType);
	   xpathWithName=XpathText.replaceAll("@param1", fieldNameWithoutNumber+j);
	   int excelColNumber=-1;
	   
	   if(columnTypeChecking(excelColName))
			 excelColNumber=Integer.parseInt(excelColName);
	   if(excelColNumber!=-1)
	   {
			 excelColName=datatable.getColumn(sheetName, excelColNumber-1);	 
	   }
	  
	   //Row Count from excel
	   int excelRowCount=datatable.getRowCount(sheetName);
	   
	   for(int rowcount=0; rowcount<excelRowCount; rowcount++)
	   {

		   forms.spreadTable("//forms:spreadTable[(@name='"+spreadTableAttribute+"')]").getCell(rowcount, excelColNumber);
		   
		   xpathWithName=XpathText.replaceAll("@param1", fieldNameWithoutNumber+j);
		   fieldValue=getFieldValue(xpathWithName, fieldType).toString();
		    Object exCelcellValue=datatable.getEditValue(sheetName, rowcount, excelColName);
		    System.out.println(" Form  column value  :"+fieldValue);
			 System.out.println("Excel column value  :"+exCelcellValue);
		     if(exCelcellValue!=null)
		     {if(exCelcellValue.toString().equalsIgnoreCase(fieldValue))
			 {					 
				 System.out.println( "form  column value and excel column  value  is  matched");
				
			 }
			 else{
				 System.out.println("form column value and excel column  value  is  not matched");
				 columnsVerifyDataCheck=false;
			 }
		    	 
		     }
		    	
	   }
		
		return columnsVerifyDataCheck;
	
	}
	
	
	
	/**
	 *  Verification of Data in Forms
	 *  
	 * @param componentType
	 * 		-- Type of Component : compType: {"textfield", "password","textarea","link","checkbox","radio","select","button","text"}
	 * @param attributes
	 * 		-- Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 		-- Excel Path Name or File Name  (i.e: D:\abc.xls or abx.xls)
	 * @param sheetName
	 * 		-- Sheet Name
	 * @param columnName
	 * 		-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean webVerifyWithExcelInput(String excelName,String sheetName,String keyword,String componentType,String columnName, String attributes) throws Exception{
		
		boolean verified = false;
		componentType = componentType.toLowerCase();
		//if(componentType.equals("textfield")){
		if(componentType.equals("edit")){
			verified = verifyEditText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("password")){
			verified = verifyPasswordText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("textarea")){
			verified = verifyTextAreaValue(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("link")){
			verified = verifyLinkText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("checkbox")){
			verified = verifyCheckBoxStatus(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("radio")){
			verified = verifyRadioStatus(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("select")){
			verified = verifyListBoxSelectedValue(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("text")){
			verified = verifyText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("button")){
			verified = verifyButtonName(attributes, excelName, sheetName, columnName);
		}
			
		
		return verified;
	}
	
	
	/**
	 *  Verification of data in forms
	 * @param componentType
	 * 			- Type of Component : {"textfield", "textarea","checkbox","radio","select","button"}
	 * @param attributes
	 * 			- Attributes :name:def
	 * @param excelName
	 * 			- Excel Path Name or File Name  (i.e: D:\abc.xls or abx.xls)
	 * @param sheetName
	 * 			- Sheet Name
	 * @param columnName
	 * 			- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formVerifywithExcelData(String componentType, String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		if(componentType.equals("textfield")){
			verified = formsVerifyEditText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("textarea")){
			verified = formsVerifyTextAreaValue(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("checkbox")){
			verified = formsVerifyCheckBoxStatus(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("radio")){
			verified = formsVerifyRadioStatus(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("select")){
			verified = formsVerifyListBoxSelectedValue(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("text")){
			verified = verifyText(attributes, excelName, sheetName, columnName);
			
		}else if(componentType.equals("button")){
			verified = formsVerifyButtonName(attributes, excelName, sheetName, columnName);
		}
			
		
		return verified;
	}
	
	
	/**
	 *  Verification of Text Field Data in Forms
	 *  
	 * @param attributes
	 * 			-- Text Field Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyEditText(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, columnName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "textfield",columnName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Text Area Data in Forms
	 *  
	 * @param attributes
	 * 			-- Text Area Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyTextAreaValue(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, columnName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "testarea",columnName);
		
		return verified;
	}
	
	/**
	 *  Verification of Checkbox status in Forms
	 *  
	 * @param attributes
	 * 			-- Checkbox Field Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyCheckBoxStatus(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, columnName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "checkbox",columnName);
		
		return verified;
	}
	
	/**
	 *  Verification of Radio button status in Forms
	 *  
	 * @param attributes
	 * 			-- Radio button Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyRadioStatus(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, columnName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "radio",columnName);
		
		return verified;
	}
	
	/**
	 *  Verification of List Value in Forms
	 *  
	 * @param attributes
	 * 			-- List Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyListBoxSelectedValue(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, columnName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "select",columnName);
		
		return verified;
	}
	
	/**
	 *  Verification of Button status in Forms
	 *  
	 * @param attributes
	 * 			-- Button Attributes i.e (name:def)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean formsVerifyButtonName(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = formsVerifyData(attributes, expectedValue, "button",webColName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Text
	 *  
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyText(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		info("");
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "text",webColName);
		
		return verified;
	}
	
	
	
	
	/**
	 *  Verification of Text Field Data
	 *  
	 * @param attributes
	 * 			-- Text Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyEditText(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "textfield",webColName);
		
		return verified;
	}
	
	/**
	 *  Verification of Password Field Data
	 *  
	 * @param attributes
	 * 			-- Password Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyPasswordText(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "password",excelColName);
		
		return verified;
	}
	
	/**
	 *  Verification of Button
	 *  
	 * @param attributes
	 * 			-- Button Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyButtonName(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "button",webColName);
		
		return verified;
	}
	
	/**
	 *  Verification of Selected Field Value
	 *  
	 * @param attributes
	 * 			-- Select Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyListBoxSelectedValue(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "select",webColName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Checkbox Status Value
	 *  
	 * @param attributes
	 * 			-- Checkbox Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyCheckBoxStatus(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "checkbox",webColName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Radio button Status
	 *  
	 * @param attributes
	 * 			-- Radio button Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyRadioStatus(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "radio",webColName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Text Area value
	 *  
	 * @param attributes
	 * 			-- Text Area Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyTextAreaValue(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "textarea",webColName);
		
		return verified;
	}
	
	
	/**
	 *  Verification of Link
	 *  
	 * @param attributes
	 * 			-- Link Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName
	 * 			-- Complete Excel Path
	 * @param sheetName
	 * 			-- Excel Name
	 * @param columnName
	 * 			-- Column Name
	 * @return
	 * @throws Exception
	 */
	public  boolean verifyLinkText(String attributes, String excelName, String sheetName, String columnName) throws Exception{
		
		boolean verified = false;
		
		String[] webAndExcelcolumnNames=columnName.split(";");
		String webColName=webAndExcelcolumnNames[0];
		String excelColName=webAndExcelcolumnNames[1];
		
		// Get the Value from the database
		String expectedValue = getDataFromExcel(excelName, sheetName, 0, excelColName);
		
		// Verify the data
		verified = verifyData(attributes, expectedValue, "link",webColName);
		
		return verified;
	}
	
	
	public boolean webVerifyTable(String tableName, String excelName, String sheetName, List<String[]> verifycolumnsNames) throws Exception {

        // If the table contains "REPORT;",it means functions should verify "Report table data" which is defined at the end
        if(tableName.startsWith("REPORT;")){
            return verifyRequestOutput(getExcelLocation(excelName), sheetName, verifycolumnsNames);
        }

        info(" *** EXCEL NAME :"+getExcelLocation(excelName));
        info(" *** SHEET NAME :"+sheetName);

        List<String> sheetNames = new ArrayList<String>();
        boolean columnsVerifyDataCheck = true;
        sheetNames.add(sheetName);
        datatable.importSheets(getExcelLocation(excelName),  sheetNames,    true,true);

        List<String> webColumnNamesList		= new ArrayList<String>();
        List<String> excelcolumnNamesList	= new ArrayList<String>();
        String webColName="";
        String excelColName="";

        int excelRowCount=datatable.getRowCount(sheetName);
        info(" *** EXCEL ROW COUNT :"+excelRowCount);

        int  webColNumber=-1, excelColNumber=-1;
        String columnName="";

        Iterator<String[]> verifycolumnsNamesiterator = verifycolumnsNames.iterator();
        
        while (verifycolumnsNamesiterator.hasNext()) {
        	
        	String[] record = (String[]) verifycolumnsNamesiterator.next();

        	//System.out.println("Web column :  Excel Column " + record[2]);
        	String[] webAndExcelcolumnNames=record[2].split(";");
        	webColName=webAndExcelcolumnNames[0];
        	excelColName=webAndExcelcolumnNames[1];


        	if(columnTypeChecking(excelColName))
        		excelColNumber=Integer.parseInt(excelColName);
        	
        	if(excelColNumber!=-1)
        	{
        		excelColName=datatable.getColumn(sheetName, excelColNumber-1);
        	}

        	webColumnNamesList.add(webColName);
        	excelcolumnNamesList.add(excelColName);
        	webColNumber=-1; excelColNumber=-1;
        }

        info("excel columns --"+excelcolumnNamesList);
        info("web columns --"+webColumnNamesList);
        
        HashMap<String,String> actions			= 	new HashMap<String, String>();
        int rowIndex=0;
        HashMap<String,List<String>> expectedTableData = new HashMap<String,List<String>>();
        
        String[] sExpComponents=new String[]{"td"};
        for(rowIndex=0; rowIndex<excelRowCount;rowIndex++)
        {
        	datatable.setCurrentRow(sheetName, rowIndex);

        	List<String> rowdata= new ArrayList<String>();

        	for( int columnIndex=0; columnIndex<excelcolumnNamesList.size(); columnIndex++ ){

        		Object exCelcellValue=datatable.getEditValue(sheetName, rowIndex, excelcolumnNamesList.get(columnIndex).trim());
        		
        		if(exCelcellValue!=null){
        			
        			if(exCelcellValue.toString().contains("#SYSTIME")){
        				String sysTimeValue = replaceSystime(exCelcellValue.toString());
        				rowdata.add(sysTimeValue) ;
        			}else{
        				rowdata.add(exCelcellValue.toString()) ;
        			}
 
        		}
        		else
        			rowdata.add("") ;
        	}
        	
        	expectedTableData.put(""+(rowIndex+1), rowdata);
        }
        
        
        String[] webcolString= new String[webColumnNamesList.size()];
        for( int i=0; i<webColumnNamesList.size();i++)
        {
        	webcolString[i] = webColumnNamesList.get(i).toString();
        }
        
        HashMap<String,HashMap<String,List<String>>> actualTableData=new HashMap<String,HashMap<String,List<String>>>();

        columnsVerifyDataCheck = wEBTABLELIB.verifyTableWithExactRowData(tableName,webcolString, expectedTableData ,sExpComponents) ;
        
        wEBTABLELIB.clearWebTableObject();
        
        return columnsVerifyDataCheck;
	} 

	private String replaceSystime(String sysTimeString) throws Exception{
		
	   String timeZone="default";
	   String tempSubString = sysTimeString.substring(sysTimeString.indexOf("#SYSTIME"));
		
       String datePatter = tempSubString.substring(tempSubString.indexOf("#SYSTIME(") + 9, tempSubString.indexOf(")"));
       //System.out.println("datePatter :"+datePatter);
       
       String format=datePatter.substring(1, datePatter.indexOf("\","));
       //System.out.println("format :"+format);
       
       String params[]=datePatter.substring(datePatter.indexOf("\",")+2).split(",");
       //System.out.println("params[] :"+Arrays.asList(params).toString());

        int arr[] = new int[params.length];
        
        for(int i=0;i<params.length;i++)
        	arr[i]=Integer.parseInt(params[i]);
        	
        //System.out.println("int[] :"+arr.length);
        String time = gENLIB.getSysTime(timeZone,format,arr);
        //System.out.println("time :"+time);
        //String time = gENLIB.getSysTime("",datePatter,"");
        
        /*sysTimeString =sysTimeString.replaceFirst("#SYSTIME(" + datePatter + ")", time);*/
        sysTimeString = Pattern.compile("#SYSTIME(" + datePatter + ")", Pattern.LITERAL).matcher(sysTimeString).replaceFirst(Matcher.quoteReplacement(time));
        //System.out.println("sysTimeString :"+sysTimeString);
        
		return sysTimeString;
	}
	
	public  String getCellData(DOMElement element) throws Exception
	{     String celldata="";
		 String text=element.getAttribute("text");
		if(text==null)
		{
			List<DOMElement> childs=element.getElementsByTagName("input");
			if(childs!=null && childs.size()>0)
				celldata=childs.get(0).getAttribute("value");
		}
		else
			celldata=text;
		 return celldata;
	}

	
	
	
	public  boolean formVerifyTable(String excelName, String sheetName, String maxVisibleLines, List<String[]> verifycolumnsNames)throws AbstractScriptException
	{
		List<String> sheetNames=new ArrayList<String>();
        boolean columnsVerifyDataCheck=true;
        sheetNames.add(sheetName);
        
        info(" *** EXCEL NAME :"+getExcelLocation(excelName));
        info(" *** SHEET NAME :"+sheetName);
        
        datatable.importSheets(getExcelLocation(excelName), sheetNames, true, true);
        int excelColNumber=-1;
        String excelColName="";
        List<String> webColumnNamesList= new ArrayList<String>();
		List<String[]> excelcolumnNamesList= new ArrayList<String[]>();
		   
		int excelRowCount=datatable.getRowCount(sheetName);
		info(" *** EXCEL ROW COUNT :"+excelRowCount);
		   
		String fieldName="", fieldType="", webColName;
        Iterator verifycolumnsNamesiterator= verifycolumnsNames.iterator();    
           
         while(verifycolumnsNamesiterator.hasNext())
         {
        	 String[] record=(String[])verifycolumnsNamesiterator.next();
        	 String[] webAndExcelcolumnNames=record[2].split(";");
        	   	
  			 webColName=webAndExcelcolumnNames[0];
  			 excelColName=webAndExcelcolumnNames[1];
        	 
  			if(columnTypeChecking(excelColName))
  				excelColNumber=Integer.parseInt(excelColName);
  			
  			if(excelColNumber!=-1)
  	   		 {
  	   			 excelColName=datatable.getColumn(sheetName, excelColNumber-1);	 
  	   		 }
  	   	    
  			fieldName=record[3];
  	   	    fieldType=record[1];
  	   	    excelcolumnNamesList.add( new String[]{excelColName,fieldName,fieldType});
  	   	    excelColNumber=-1;
        	
           }
         
        int j=0 , excelColumnCount=excelcolumnNamesList.size();
        String fieldNameWithoutNumber="";
 	    String  XpathText="", fieldValue="",xpathWithName="";
 	   
 	    for(int rowIndex=0; rowIndex < excelRowCount; rowIndex++)
  	     {
        	  info( "*********Row Number*********************** "+ (rowIndex+1));
        	  
        	for ( int columnIndex=0; columnIndex < excelColumnCount; columnIndex++)
    	 	{
        		 XpathText=getFieldTypeXpath(excelcolumnNamesList.get(columnIndex)[2]);
        		 
        		 fieldName=excelcolumnNamesList.get(columnIndex)[1];
        		 
        		 int l=fieldName.length()-1;
        		 fieldNameWithoutNumber=fieldName.substring(0,l);
        		 
        		 xpathWithName=XpathText.replaceAll("@param1", fieldNameWithoutNumber+j);
        		 //info("xpathWithName :"+xpathWithName);
        		 
        		 
        		 fieldValue=getFieldValue(xpathWithName, excelcolumnNamesList.get(columnIndex)[2]).toString();
        		 
        		 Object exCelcellValue=datatable.getEditValue(sheetName, rowIndex, excelcolumnNamesList.get(columnIndex)[0]);
        		 
        		 info(" Column Name "+ excelcolumnNamesList.get(columnIndex)[0]);
        		 
        		 
        		 if(exCelcellValue!=null)
        		 {
        			 if(exCelcellValue.toString().equalsIgnoreCase(fieldValue))
        			 {	
        				 info(" Column value in Forms  :"+fieldValue  + " Excel column value  : " + exCelcellValue);
        				 info( " Column value in Forms  and excel column  value  are  matched");
  				
        			 }
        			 else
  		    	 	{
  		    		 info("################################ ERROR ###########################################");
  		    		 
  		    		 info(" Column value in Forms  :"+fieldValue  + " Excel column value  : " + exCelcellValue);
  		    		 reportFailure("Column value in Forms  and Excel column  value  are  not matched");
  		    		 
  		    		 info("###########################################################################");
  		    		 columnsVerifyDataCheck=false;
  			 		}
  		    	 
        		 }
        	 }
        	
        	//info("rowIndex  :"+rowIndex+ "   maxVisibleLines"+maxVisibleLines);
        	
        	
        	
  		    if((rowIndex+1)<toInt(maxVisibleLines))
  		        j++;
  		    else{	// if RowIndex refers to last row of the table, then don't click on DOWN arrow
  		    	if((rowIndex+1) != excelRowCount){
  		    		//info("xpathWithName while donw :"+xpathWithName);
  	  		       	forms.textField(xpathWithName).invokeSoftKey("DOWN");
  		    	}
  		    }
  		  
  	   }
         
//         if( !formVerifycolumsdata(record[2],record[3],record[1],sheetName,maxVisibleLines))
//        				  columnsVerifyDataCheck=false;
        		     		   
        	   
       
			return columnsVerifyDataCheck;	
	}
	
	
	
	
	private boolean columnTypeChecking(String colunmName)
	{
		Pattern p = Pattern.compile( "([0-9]*)" );

		Matcher m = p.matcher(colunmName);
		
		return m.matches();
	}
	
	
	
	
	
	
	private String  getFieldTypeXpath(String fieldType)
	{
		
		////forms:textField[(@name='PROJECT_OPTIONS_OPTION_NAME_DISP_"+j+"')]";
		
		if("EDIT".equalsIgnoreCase(fieldType) ||"TEXTAREA".equalsIgnoreCase(fieldType))
		   return "//forms:textField[(@name='@param1')]";
		
		if("CHECKBOX".equalsIgnoreCase(fieldType))
			return "//forms:checkBox[(@name='@param1')]";
		
		if("RADIOBUTTON".equalsIgnoreCase(fieldType))
			return "//forms:radioButton[(@name='@param1')]";
		
		if("LOV".equalsIgnoreCase(fieldType))
			return "//forms:listOfValues";
		
		if("BUTTON".equalsIgnoreCase(fieldType))
			return "//forms:button[(@label='@param1')]";
		
		return "";
	}
	
	private Object getFieldValue(String Xpath, String fieldType) throws AbstractScriptException
	{
		  if("EDIT".equalsIgnoreCase(fieldType) ||"TEXTAREA".equalsIgnoreCase(fieldType))
			   return forms.textField(Xpath).getText();
			
		  	if("CHECKBOX".equalsIgnoreCase(fieldType))
				return forms.checkBox(Xpath).isSelected();
			
			if("RADIOBUTTON".equalsIgnoreCase(fieldType))
				return forms.radioButton(Xpath).isSelected();
			/*
			if("LOV".equalsIgnoreCase(fieldType))
				return "//forms:listOfValues";
			
			if("BUTTON".equalsIgnoreCase(fieldType))
				return "//forms:button[(@label='@param1')]";
				*/
		
		 return "";
	}
	
	
	//**********************************************************************************************************************//
	
	private  boolean formsVerifyData(String attributes, String expectedValue,String componentType, String columnNAme) throws Exception{
		
		boolean verified = false;
		String actualCellValue = "";
		
		// Get the Actual Value
		if(componentType.equalsIgnoreCase("text")){
			/*// attributes are displayed as @after="afterText", @before="beforeText" 
			String[] values = attributes.split(",");
			String beforeText = values[0].substring(values[0].indexOf("=\"")+2,values[0].length()-1);
			String afterText = values[1].substring(values[1].indexOf("=\"")+2,values[1].length()-1);
			info("beforeText :"+beforeText);
			info("afterText :"+afterText);
			actualCellValue = getText(beforeText,afterText);*/
		}else{
			actualCellValue = getActualValueFromForms(attributes, componentType);
		}
		
		
		
		// Verification
		if(expectedValue.equals(actualCellValue)){
			info("verifyData for the component "+columnNAme+": Actaul Value '"+actualCellValue+"' is matched with Expected Value '"+expectedValue+"'.");
			
			verified = true;
		}else{
			this.reportFailure("verifyData for the component "+columnNAme+": Actaul Value '"+actualCellValue+"' is not matched with Expected Value '"+expectedValue+"'.");
			verified = false;
		}
		
		return verified;
	}
	
	private  boolean verifyData(String attributes, String expectedValue,String componentType, String columnNAme) throws Exception{
		
		info("****** Start of verifyData ******");
		boolean verified = false;
		String actualCellValue = "";
		
		// Get the Actual Value
		if(componentType.equalsIgnoreCase("text")){
			// attributes are displayed as @after="afterText", @before="beforeText" 
			String[] values = attributes.split(",");
			String beforeText = "1";
			String afterText = "1";
			for(int index=0; index < values.length; index++){
				
				String val = values[index];
				
				if(val.startsWith("@after=")){
					afterText = values[index].substring(values[1].indexOf("=\"")+2,values[1].length()-1);
				}else if(val.startsWith("@before=")){
					beforeText = values[index].substring(values[0].indexOf("=\"")+2,values[0].length()-1);
				}
			}
			
			info("beforeText :"+beforeText);
			info("afterText :"+afterText);
			
			actualCellValue =  gENLIB.webGetText(beforeText, afterText);
			//actualCellValue = getText(beforeText,afterText);
		}else{
			actualCellValue = getActualValue(attributes, componentType);
		}
		
		
		
		// Verification
		if(expectedValue.equals(actualCellValue)){
			info("verifyData for the component "+columnNAme+": Actaul Value '"+actualCellValue+"' is matched with Expected Value '"+expectedValue+"'.");
			
			verified = true;
		}else{
			this.reportFailure("verifyData for the component "+columnNAme+": Actaul Value '"+actualCellValue+"' is not matched with Expected Value '"+expectedValue+"'.");
			verified = false;
		}
		
		info("****** End of verifyData ******");
		return verified;
	}
	
	
	private String getActualValueFromForms(String attributes, String componentType) throws Exception {
		
		// Get the attributes List & prepare xpath Attribute string
		String compAttributes = "";
		String[] attributesArray = attributes.split(",");  // Multiple Attributes i.e (id:abc,name:def,index:ghi)
		for(int index=0; index < attributesArray.length; index++ ){
			
			String[] attributeArray = attributesArray[index].split(":"); // single Attribute .e (id:abc)
			
			if("".equals(compAttributes)){
				compAttributes = "@"+attributeArray[0]+"='"+attributeArray[1]+"'";
			}else{
				compAttributes = compAttributes + " or "+"@"+attributeArray[0]+"='"+attributeArray[1]+"'";
			}
		}
		sop("Attribute List Path : "+compAttributes);
		
		
		/* Generate XPath */
		String xPath = null;
		String actualValue = null;
		if(componentType.equals("textfield")){
			xPath = "//forms:textField["+compAttributes+"]";
			actualValue =  forms.textField(xPath).getText();
						
		}else if(componentType.equals("textarea")){
			xPath = "//forms:textField["+compAttributes+"]";
			actualValue =  forms.textField(xPath).getText();
			
		}else if(componentType.equals("checkbox")){
			xPath = "//forms:checkBox["+compAttributes+"]";
			actualValue =  forms.checkBox(xPath).getAttribute("selected");
			
		}else if(componentType.equals("radio")){
			xPath = "//forms:radioButton["+compAttributes+"]";
			actualValue =  ""+forms.radioButton(xPath).isSelected();
			
		}else if(componentType.equals("select")){
			xPath = "//forms:list["+compAttributes+"]";
			actualValue = forms.list(xPath).getItemValue();
			
		}else if(componentType.equals("image")){
			xPath = "//forms:imageItem["+compAttributes+"]";
			actualValue = forms.imageItem(xPath).getName();
			
		}else if(componentType.equals("button")){
			xPath = "//forms:button["+compAttributes+"]";
			actualValue = forms.button(xPath).getLabel();
		}
				
		return actualValue;
		
	}
	
	
	private String getActualValue(String attributes, String componentType) throws Exception {
		
		/* Get Current Window Path */
		String windowPath = getCurrentWindowXPath();
		
		// Get Current Window Index
		int currentWindowIndex = Integer.parseInt(web.window(windowPath).getAttribute("index"));
		
		// Set the document Index & Get the Document Path
		String docPath = windowPath + "/web:document[@index='"+currentWindowIndex+"']"; 
		
		// Get the attributes List & prepare xpath Attribute string
		String compAttributes = "";
		String[] attributesArray = attributes.split(",");  // Multiple Attributes i.e (id:abc,name:def,index:ghi)
		for(int index=0; index < attributesArray.length; index++ ){
			
			String[] attributeArray = attributesArray[index].split(":"); // single Attribute .e (id:abc)
			
			if("".equals(compAttributes)){
				compAttributes = "@"+attributeArray[0]+"='"+attributeArray[1]+"'";
			}else{
				compAttributes = compAttributes + " or "+"@"+attributeArray[0]+"='"+attributeArray[1]+"'";
			}
		}
		sop("Attribute List Path : "+compAttributes);
		
		
		/* Generate XPath */
		String xPath = null;
		String actualValue = null;
		if(componentType.equals("input") || componentType.equals("textfield")){
			xPath = docPath + "/web:input_text["+compAttributes+"]";
			actualValue = web.textBox(xPath).getAttribute("value");
			
		}else if(componentType.equals("password")){
			xPath = docPath + "/web:input_password["+compAttributes+"]";
			actualValue = web.textArea(xPath).getAttribute("value");
			
		}else if(componentType.equals("textarea")){
			xPath = docPath + "/web:textarea["+compAttributes+"]";
			actualValue = web.textArea(xPath).getAttribute("value");
			
		}else if(componentType.equals("link")){
			xPath = docPath + "/web:a["+compAttributes+"]";
			actualValue = web.link(xPath).getAttribute("text");
			
		}else if(componentType.equals("checkbox")){
			xPath = docPath + "/web:input_checkbox["+compAttributes+"]";
			actualValue = web.checkBox(xPath).getAttribute("checked");
			
		}else if(componentType.equals("radio")){
			xPath = docPath + "/web:input_radio["+compAttributes+"]";
			actualValue = web.radioButton(xPath).isSelected()+"";
			
		}else if(componentType.equals("select")){
			xPath = docPath + "/web:select["+compAttributes+"]";
			actualValue = web.selectBox(xPath).getSelectedText()[0];
			
		}else if(componentType.equals("image")){
			xPath = docPath + "/web:img["+compAttributes+"]";
			actualValue = web.element(xPath).getAttribute("alt");
			
		}else if(componentType.equals("button")){
			xPath = docPath + "/web:button["+compAttributes+"]";
			actualValue = web.element(xPath).getAttribute("innertext");
		}
				
		return actualValue;
		
	}
	
	
	
	
	private String getDataFromExcel(String excelName, String sheetName, int rowNumber, String columnName)throws AbstractScriptException
	{
		
		 
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(columnName);
		
		HashMap<String,String> columnCellValues = getDataFromExcel(getExcelLocation(excelName), sheetName, rowNumber, columnNames);
		
		String specificColValue = columnCellValues.get(columnName);
		
		return specificColValue;
	}
	
	
	private  HashMap<String,String> getDataFromExcel(String excelName, String sheetName, int rowNumber, List<String> columnNames)throws AbstractScriptException
	{
		// Initialize HashMap
		HashMap<String,String> excelValues = new HashMap<String,String>();
		
		// Excel Path
		info("Excel Path :"+excelName);
		info("Sheet Name :"+sheetName);
		info("Columns :"+columnNames.toString());
		info("Row Number :"+rowNumber);
		
		// Import Excel Sheet
		List<String> sheetNames=new ArrayList<String>();
        sheetNames.add(sheetName);
        datatable.importSheets(excelName, sheetNames, true, true);
		
        // Get Column Count
        
        int colCount = datatable.getColumnCount(sheetName);
        sop("Column Count :"+colCount);
        
        if(columnNames != null && columnNames.size() > 0){
        	
        	for(int index=0; index < columnNames.size(); index++){
        		
        		// Get the Cell Value from specified row & row starts from '0'
            	Object cellValue = datatable.getEditValue(sheetName, rowNumber, columnNames.get(index));
            	
            	//Put the Cell values in the HashMap
            	if(cellValue != null){
            		sop("Cell Value "+columnNames.get(index)+" : "+cellValue.toString());
            		 excelValues.put(columnNames.get(index), cellValue.toString());
            	}else{
            		sop("Cell Value "+columnNames.get(index)+" : "+"");
           		 	excelValues.put(columnNames.get(index), "");
            	}
        	}	
    	}else{
    		
    		// Get the Data from Excel  
            for(int index=0; index < colCount; index++){
                    	
            	// Get Column Name
            	String columnName = datatable.getColumn(sheetName, index);
            	
            	// Get the Cell Value from specified row & row starts from '0'
            	Object cellValue = datatable.getEditValue(sheetName, rowNumber, columnName);
            	
            	//Put the Cell values in the HashMap
            	if(cellValue != null){
            		sop("Cell Value "+columnName+" : "+cellValue.toString());
            		 excelValues.put(columnName, cellValue.toString());
            	}else{
            		sop("Cell Value "+columnName+" : "+"");
           		 	excelValues.put(columnName, "");
            	}
             }
    	}
       
		return excelValues;
	}
	
	
	
	private String getCurrentWindowXPath() throws Exception {
		
		String index = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String xPath = "/web:window[@index='"+index+"' or @title='"+title+"']";
		
		return xPath;
	}
	
	
	/**
	 * 
	 * @param before 
	 * @param after 
	 * @param textToVerify 
	 * @param index 
	 */
	public String getText(String before, String after) throws Exception {
		
		int index = 0;
		String textToVerify  = "";
		String content = web.document( "/web:window[@index='*']/web:document[@index='*']").getElementsByTagName("body").get(0).getAttribute("text");
		List<Integer> indexOfAfterTextPresence = new ArrayList<Integer>();
		int afterindex = content.length(), beforeindex = 0 , startIndex =0 ;
		
		while ((afterindex = content.lastIndexOf(after, afterindex)) != -1) {
			
			//System.err.println("Inside afterindex loop"+afterindex);
			indexOfAfterTextPresence.add(afterindex);
			afterindex -= after.length() - 1;
			
	    }
		List<Integer> noOfTimesBeforeTextisPresent = new ArrayList<Integer>();
		startIndex =0 ;
		while ((beforeindex = content.indexOf(before, beforeindex)) != -1) {
			//System.err.println("Inside b4index loop"+beforeindex);
			noOfTimesBeforeTextisPresent.add(beforeindex+before.length());
			beforeindex += before.length() - 1;
			
	    }
		if((indexOfAfterTextPresence.size()==0)||((noOfTimesBeforeTextisPresent.size()==0))){
			if(noOfTimesBeforeTextisPresent.size()==0){
				
				this.reportFailure(" getText: Before text: "+before+"isn't found in Webpage");
				
				return "";
				
			}else{
				this.reportFailure("getText: after text: "+after+"isn't found in Webpage");
			
			return "";}
		}
		else{

			textToVerify = content.substring( noOfTimesBeforeTextisPresent.get(index), indexOfAfterTextPresence.get(index));

		}
		
		return textToVerify;
	}
	
	
	
	private void sop(String message){
		//System.out.println(message);
	}
	
	
	
	
	
	//###################################################################################################################################
	//   Verification of Report Data
	//###################################################################################################################################
	
	/**
	 * 
	 * 
	 * Example: 
	 * 
	 * String path = "O:\\EBS QA\\GUIDS\\Ganesh\\LibraryTest\\Data.xls";
		
		List <String[]> act    =    new ArrayList<String[]>();
		act.add(new String[]{"VERIFY","TEXT","Date;Date",""});
		act.add(new String[]{"VERIFY","TEXT","Currency Code;Currency Code",""});
		act.add(new String[]{"VERIFY","TEXT","Total Cash;Total Cash",""});
		
		verifyRequestOutput(path, "Report Data", act);
	 */
	public boolean verifyRequestOutput(String excelName, String sheetName, List<String[]> verifyColumnsNames) throws Exception {
		
		List<String> sheetNames = new ArrayList<String>();
		boolean columnsVerifyDataCheck = true;
		sheetNames.add(sheetName);
		
		info(" *** EXCEL NAME :"+excelName);
        info(" *** SHEET NAME :"+sheetName);
        
		// Import data
        //datatable.deleteSheet(sheetName);
		datatable.importSheets(excelName,sheetNames,true,true);
		
		HashMap<String,String> excelWebColumnsMapping = new HashMap<String,String>();
		Iterator<String[]> verifycolumnsNamesIterator = verifyColumnsNames.iterator();
		while (verifycolumnsNamesIterator.hasNext()) {
			
			String[] record = (String[]) verifycolumnsNamesIterator.next();
			
			//record[2] -- Contains Report Column Name & Excel Column Name  ex: "WEBCOL;EXCECOL1"
			String[] columnMapping = record[2].split(";");
			excelWebColumnsMapping.put(columnMapping[1], columnMapping[0]); // Excel Column Name: columnMapping[1] && Report Column Name: columnMapping[0]
			
		}// End of While

		
		// Get the Report Data Log
		HashMap<String,HashMap<String,String>> actualReportData = getReportData();
		//System.out.println("actualReportData :"+actualReportData.toString());
		
		// Verify Excel Data with Expected Data
		columnsVerifyDataCheck = webVerifycolumsDataForReport(sheetName, excelWebColumnsMapping, actualReportData);
		
		return columnsVerifyDataCheck;
	}
	
	
	private boolean webVerifycolumsDataForReport(String sheetName,HashMap<String,String> excelWebColumnsMapping, HashMap<String,HashMap<String,String>> actualReportData) throws AbstractScriptException
	{    
	  	boolean columnsVerifyDataCheck = true;
		
	  	// Get Excel Column Names
	  	ArrayList<String> excelColumns = getKeySet(excelWebColumnsMapping);
	  	
		// Get Excel Row Count
		int excelRowCount=datatable.getRowCount(sheetName); 
		info(" *** EXCEL ROW COUNT :"+excelRowCount);
		
		// Application Log Report Row Count
		int webRowCount = actualReportData.size();
		
		for(int rowIndex=0; rowIndex < excelRowCount;rowIndex++)
		 {
			/* if(rowIndex>=webRowCount-1)
				continue; */
			 
			 HashMap<String,String> columnsVerified = new HashMap<String,String>();
			 HashMap<String,String> columnsNotVerified = new HashMap<String,String>();
			 
			 // Get Actual Row Data
			 HashMap<String,String> actRowData = actualReportData.get(Integer.toString(rowIndex));
			 System.out.println("actRowData :"+actRowData.toString());
			 
			 for(int colIndex=0; colIndex < excelColumns.size(); colIndex++){
				 
				 // Get the Data from the Excel for RowIndex, ColumnIndex
				 String columnName = excelColumns.get(colIndex); // get the excel Column Name
				 Object exCelcellValue = datatable.getValue(sheetName, rowIndex, columnName);
				 
				 // Get the Data from the reprot table for RowIndex, ColumnIndex
				 //System.out.println("columnName :"+columnName);
				 String actualColumnName = getExactMappedWebColumn(actRowData, excelWebColumnsMapping.get(columnName));
				 String webCellValue = actRowData.get(actualColumnName); // Application Report Column = excelWebColumnsMapping.get(columnName)
				// System.out.println("webCellValue :"+webCellValue);
				 if(exCelcellValue != null){
					 
					 if(exCelcellValue.toString().equalsIgnoreCase(webCellValue))
					 {					 
						 columnsVerified.put(columnName, exCelcellValue.toString());
						
					 }else{
						 columnsNotVerified.put(columnName, exCelcellValue.toString());
						 columnsVerifyDataCheck = false;
					 }
				 }
				 
			 }
			 
			 if(columnsNotVerified.size() > 0){

				 info("################################ ERROR ###########################################");
				 //System.err.println("Start:- Error Info for verification  -- Failure at Row Number :"+(rowIndex+1));
				 reportFailure("Actual Row Data :"+actRowData.toString());
				 reportFailure("Expected Columns Data verified successfully in the row :"+(rowIndex+1)+" are "+columnsVerified.toString());
				 reportFailure("Expected Columns not verified in the row :"+(rowIndex+1)+" are "+columnsNotVerified.toString());
				 //System.err.println("End:- Error Info for verification");

				 info("###########################################################################");
				 
			 }else{
				 info("Columns Verified successfully in the row :"+(rowIndex+1)+" are "+columnsVerified.toString()); 
			 }
		 }
		
		
		 return columnsVerifyDataCheck;
	}
	
	 private  String  getExactMappedWebColumn( HashMap<String, String> webColumnMap, String excelColumnName)
	 {
			String webcolumnName="";		
			Iterator  columnIterator= webColumnMap.keySet().iterator();
			while (columnIterator.hasNext()){
				webcolumnName=(String)columnIterator.next();
				if(webcolumnName!=null)
				{
					if(excelColumnName.contains(webcolumnName))
						return webcolumnName;
				}
			}
			
			return "";
	}
	
	public HashMap<String,HashMap<String,String>> getReportData() throws Exception{
		
		//String winnIndex = "2";
		String winnIndex = web.getFocusedWindow().getAttribute("index");
		String title = web.getFocusedWindow().getTitle();
		
		String winPath = "/web:window[@index='"+winnIndex+"' or @title='"+title+"']";	
		String docPath = winPath+"/web:document[@index='0']";
		System.out.println("docPath  : "+docPath);
		
		
		List<DOMElement> elements= web.document(docPath).getElementsByTagName("pre");
		//System.out.println("Elements Size  : "+elements.size());
		
		String text = elements.get(0).getAttribute("text");
		
		//System.out.println("Test :"+text);
		
		// Report Data
		String[] reportData = text.split("\n");
		
		int actualRowDataStart = 0; // Start of Actual Rows
		int columnStart = 0; // Strat of Column Headers
		int columnEnd = 0; // end of Column Headers
		
		for(int i=0; i< reportData.length; i++){
			System.out.println("Row :"+i+"     data :"+reportData[i]);
			
			if(reportData[i].trim().equals("")){
				columnStart = i+1;
			}
			
			if(reportData[i].startsWith("==")){
				actualRowDataStart = i+1;
				columnEnd = i-1;
				break;
				
			}
		}
		
		
		// Get Column Names
		ArrayList<String> colNames = getReportHistoryColumns(reportData, columnStart, columnEnd);
		System.out.println("Columns :"+colNames.toString());
		
		HashMap<String,HashMap<String,String>> actualTableData = new HashMap<String,HashMap<String,String>> ();
		for(int dataIndex =actualRowDataStart, index=0; dataIndex< reportData.length; dataIndex++, index++){
			
			HashMap<String,String> rowData = new HashMap<String,String>();
			//System.out.println("Row Number :"+(dataIndex-actualRowDataStart+1));
			
			String sRowData = reportData[dataIndex];
			//System.out.println("sRowData :"+sRowData);
			
			String[] columnData = sRowData.split("  ");
			//System.out.println("columnData Lenght:"+columnData.length);
			
			for(int colIndex=0, actualColIndex=0; colIndex < columnData.length; colIndex++){
				
				if(!"".equals(columnData[colIndex].trim())){
					//System.out.println(columnData[colIndex].trim());
					rowData.put(colNames.get(actualColIndex), columnData[colIndex].trim());
					actualColIndex++;
				}
			}
			
			actualTableData.put(""+index, rowData);
		}
		
		return actualTableData;
		
	}
	
	/**
	 *  Get Report Header Columns
	 *  
	 * @param reportData
	 * @param columnStart
	 * @param columnEnd
	 * @return
	 */
	private ArrayList<String> getReportHistoryColumns(String[] reportData, int columnStart, int columnEnd){
		
		boolean spaceVisit = false;
		
		ArrayList<String> colLength = new ArrayList<String>();
		for(int index=0; index < reportData[columnEnd].length(); index++){
	
			char ch = reportData[columnEnd].charAt(index);
			
			if(ch == ' '){
				spaceVisit = true;
			}
			
			if(ch != ' ' && spaceVisit){
				colLength.add(""+(index));
				spaceVisit = false;
			}
			
			if(index == reportData[columnEnd].length()-1){
				colLength.add(""+(index-1));
				spaceVisit = false;
			}
		}
		
		
		System.out.println("Column length :"+colLength.toString());
		
		
		// Getting Columns
		ArrayList<String> colNames = new ArrayList<String>();
		
		int startIndex=0;
		for(int colIndex=0 ; colIndex < colLength.size(); colIndex++ ){
			
			String columnName = "";
			for(int index = columnStart; index<=columnEnd; index++){
				
				String cellValue = reportData[index].substring(startIndex, Integer.parseInt(colLength.get(colIndex)));
				
				if(!"".equals(cellValue.trim())){
					columnName = columnName +" "+ cellValue.trim();
				}
			}
			
			startIndex = Integer.parseInt(colLength.get(colIndex));
			
			colNames.add(columnName.trim());
		}
		
		return colNames;
	}
	
	
	
	private ArrayList<String> getKeySet(HashMap<String,String> excelWebColumnsMapping){
		
		ArrayList<String> keyset = new ArrayList<String>();
		Iterator  iterator = excelWebColumnsMapping.keySet().iterator();
		
	    while (iterator.hasNext()) {
	      String key = (String) iterator.next();
	      keyset.add(key);
	    }
	    
	    System.out.println("Excel Columns :"+keyset.toString());
	    
	    return keyset;
	}
}

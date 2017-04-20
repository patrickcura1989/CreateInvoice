//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.Variables;
import oracle.oats.scripting.modules.formsFT.common.api.elements.AbstractWindow;
import oracle.oats.scripting.modules.webdom.api.elements.DOMBrowser;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMTable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import oracle.oats.scripting.modules.applet.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;

public class PROJLIB extends FuncLibraryWrapper
{

	public void initialize() throws AbstractScriptException {
		checkInit();
		callFunction("initialize");
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws AbstractScriptException {
		checkInit();
		callFunction("run");
	}

	public void finish() throws AbstractScriptException {
		checkInit();
		callFunction("finish");
	}

	public boolean refreshPaymentProcessRequest(String buttonName,
			String requestSourceColName, String requestSourceColValue,
			String referenceSourceColName, String referenceSourceColValue,
			String Status) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("refreshPaymentProcessRequest",
				buttonName, requestSourceColName, requestSourceColValue,
				referenceSourceColName, referenceSourceColValue, Status);
	}

	public void leaseOpenAccountingPeriod(String startDate, String endDate)
			throws AbstractScriptException {
		checkInit();
		callFunction("leaseOpenAccountingPeriod", startDate, endDate);
	}

	public String getCurrentWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowXPath");
	}

	public String getCurrentWindowTitle() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowTitle");
	}

	public void openPeriod_obsolete(String period)
			throws AbstractScriptException {
		checkInit();
		callFunction("openPeriod_obsolete", period);
	}

	public void openPeriod(String period) throws AbstractScriptException {
		checkInit();
		callFunction("openPeriod", period);
	}

	public void CheckForms() throws AbstractScriptException {
		checkInit();
		callFunction("CheckForms");
	}

	/**
	 * This function closes Forms and Launches SSA Login page
	 * @return
	 * @throws Exception
	 */
	public int CloseForms() throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("CloseForms");
	}

	/**
	 * This function sets the state of the application to the specified state.
	 * @param State # "<User>:<Responsibility>:<Project>" # "<User>:<Responsibility>" # "<User>" #  None
	 * @return  0 if successful. Negative Integer if fails.
	 * @throws Exception
	 */
	public int SetBaseState(String State) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetBaseState", State);
	}

	public int SetResponsibility(String RespName, int Project)
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetResponsibility", RespName, Project);
	}

	public int LogOut(int State) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("LogOut", State);
	}

	public int SetUser(String OldState[], String NewState[])
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetUser", OldState, NewState);
	}

	public int LogIn(String User) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("LogIn", User);
	}

	public void initEBS() throws AbstractScriptException {
		checkInit();
		callFunction("initEBS");
	}

	/**
	 * Replaces ** with Index
	 * @param strString
	 * @return
	 */
	public String ReplaceIndex(String strString) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("ReplaceIndex", strString);
	}

	@SuppressWarnings("unchecked")
	public String FormatStdDate(String Date, String Format)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("FormatStdDate", Date, Format);
	}

	public String IsNumber(String TestVar) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("IsNumber", TestVar);
	}

	public boolean CompareValue(int ActValue, int ExpValue)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("CompareValue", ActValue, ExpValue);
	}

	public String GetNextNumber(String CurrentNumber)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetNextNumber", CurrentNumber);
	}

	public String getRequestId(String Source, String Request)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRequestId", Source, Request);
	}

	public boolean FindInArray(String FindArray[], String Item)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("FindInArray", FindArray, Item);
	}

	public String[] ClearArray(String Array[]) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("ClearArray", Array);
	}

	public String RPad(String Str, int Len, String PadStr)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("RPad", Str, Len, PadStr);
	}

	@SuppressWarnings("unchecked")
	public String FormatDate(String Date, String Format)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("FormatDate", Date, Format);
	}

	public String StrLtrim(String value) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("StrLtrim", value);
	}

	public String GetRequestID(String Text) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetRequestID", Text);
	}

	public String GetUniqueIndex() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetUniqueIndex");
	}

	public int GetNumber(String Text) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("GetNumber", Text);
	}

	public String Get_Add_Sub_Date(String date, String Add_Type, int Value)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("Get_Add_Sub_Date", date, Add_Type, Value);
	}

	/**
	 * GetObjVal
	 * @param Object
	 * @param Step
	 * @param Instance
	 * @return
	 * @throws Exception
	 */
	public String GetObjVal(String Object, String Step, String Instance)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetObjVal", Object, Step, Instance);
	}

	/**
	 * GetMessage
	 * @param Source
	 * @param Request
	 * @return
	 * @throws Exception
	 */
	public String GetMessage(int Source, int Request)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetMessage", Source, Request);
	}

	public int SetObjVal(String Object, String Value, String Step,
			String Instance) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetObjVal", Object, Value, Step,
				Instance);
	}

	public String getGlobalPrameter(String parameter, int index)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getGlobalPrameter", parameter, index);
	}

	public void RunQuery() throws AbstractScriptException {
		checkInit();
		callFunction("RunQuery");
	}

	/**
	 * Prepares the form for entering the query
	 * @param Win_Name
	 * @throws Exception
	 */
	public void EnterQuery() throws AbstractScriptException {
		checkInit();
		callFunction("EnterQuery");
	}

	/**
	 * Cancels the query
	 * @param Win_Name
	 * @throws Exception
	 */
	public void CancelQuery() throws AbstractScriptException {
		checkInit();
		callFunction("CancelQuery");
	}

	/**
	 * Shows a field
	 * @param Win_Name
	 * @param FieldName
	 * @throws Exception
	 * @return  1 if successful and 0 for fails
	 */
	public int ShowField(String FieldName) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("ShowField", FieldName);
	}

	/**
	 * Hides a Field 
	 * @param Win_Name
	 * @throws Exception
	 */
	public void HideField() throws AbstractScriptException {
		checkInit();
		callFunction("HideField");
	}

	/**
	 * Clears the Record
	 * @param Win_Name
	 * @throws Exception
	 */
	public void ClearRecord() throws AbstractScriptException {
		checkInit();
		callFunction("ClearRecord");
	}

	/**
	 * Saves the current display as the given folder Name
	 * @param FolderName
	 * @throws Exception
	 */
	public void SaveFolder(String FolderName) throws AbstractScriptException {
		checkInit();
		callFunction("SaveFolder", FolderName);
	}

	/**
	 * Deletes Current Record
	 * @throws Exception
	 */
	public void DeleteRecord() throws AbstractScriptException {
		checkInit();
		callFunction("DeleteRecord");
	}

	/**
	 * Shows all the fields passed into it in the form of an array. 
	 * @param FieldName   - An array containing all the fields to be displayed.
	 * @param ParamNum   - Contains the number of values contained in the ParamArray.
	 * @throws Exception
	 */
	public void ShowAllFields(String[] FieldName, int ParamNum)
			throws AbstractScriptException {
		checkInit();
		callFunction("ShowAllFields", (Object) FieldName, ParamNum);
	}

	/**
	 * Gets The Error Text From the Error or Note Window. 
	 * @param Text -The Captured Text Will be Stored 
	 * @throws Exception
	 */
	public void GetErrText(String Text) throws AbstractScriptException {
		checkInit();
		callFunction("GetErrText", Text);
	}

	/**
	 * Navigates to the specified Project Option
	 * @param  Option- The project option to navigate to. In case of child, use ":" as a seperator, without spaces
	 * @throws Exception
	 */
	public void NavProjectOption(String Option) throws AbstractScriptException {
		checkInit();
		callFunction("NavProjectOption", Option);
	}

	/**
	 * Navigates to the specified Task Number (Invoke NavProjectOption("Tasks") and then use this)
	 * @param Option  - The Task Number to navigate to
	 * @return  0 if successful, -1 - failure
	 * @throws Exception
	 */
	public int NavTaskNum(String Option) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("NavTaskNum", Option);
	}

	public void NavTaskOption(String Option) throws AbstractScriptException {
		checkInit();
		callFunction("NavTaskOption", Option);
	}

	/**
	 * Validates actual results data with expected results. 
	 * @param ExpVal  - Path of the first file.
	 * @param ActualVal  - Path of the second file.
	 * @param StartNo  - Starting Line Number of the ExpFile
	 * @param EndNo  - Corresponding Ending Line Number of the ExpFile 
	 * @param ShowAll
	 * @return  0 if successful, 1 - failure
	 * @throws Exception
	 */
	public int ValidateData(String ExpVal, String ActualVal, int StartNo,
			int EndNo, String ShowAll) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("ValidateData", ExpVal, ActualVal,
				StartNo, EndNo, ShowAll);
	}

	/**
	 * Gets the parameter array and the number of elements in it.  
	 * @param FileName  -Path of the xls file.
	 * @param ParamArray
	 * @param ParamNum
	 * @return  ParamArray[]: Array containing all the column values.
	 * @throws Exception
	 */
	public String[] GetParamArray(String FileName, String ParamArray[],
			int ParamNum) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("GetParamArray", FileName, ParamArray,
				ParamNum);
	}

	/**
	 * Compares the data passed through two excels. 
	 * @param ExpValue  - Path of the first file.
	 * @param ActualValue  - Path of the second file.
	 * @param StartNo
	 * @param EndNo
	 * @return  0 if successful, 1 - failure
	 * @throws Exception
	 */
	public int CompareXLS(String ExpValue, String ActualValue, int StartNo,
			int EndNo) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("CompareXLS", ExpValue, ActualValue,
				StartNo, EndNo);
	}

	public String CheckRequestStatus(String req_id, int NumberOfTries)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("CheckRequestStatus", req_id,
				NumberOfTries);
	}

	public String[] FireRequest(String Request, String Parameters[],
			String Prompt[], int NumberOfTries, String OU)
			throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("FireRequest", Request, Parameters,
				Prompt, NumberOfTries, OU);
	}

	/**
	 * This function Navigates to the Home page of given Template
	 * @param ProjectTemplate
	 * @return
	 * @throws Exception
	 */
	public int TemplateHome(String ProjectTemplate)
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("TemplateHome", ProjectTemplate);
	}

	/**
	 * Checks for personalized view. Sets the view if found otherwise creates the view.
	 * @param ViewName
	 * @param ColList
	 * @return
	 * @throws Exception
	 */
	public int CheckView(String ViewName, String ColList[])
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("CheckView", ViewName, ColList);
	}

	/**
	 * @param ProjectName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int SetProjectBaseState(String ProjectName)
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetProjectBaseState", ProjectName);
	}

	public int SetProjOrTempBaseState(String ProjOrTemplateValue, String PorTorA)
			throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("SetProjOrTempBaseState",
				ProjOrTemplateValue, PorTorA);
	}

	public void SelectTask(String RequiredTaskValue)
			throws AbstractScriptException {
		checkInit();
		callFunction("SelectTask", RequiredTaskValue);
	}

	public void SelectTemplateOption(String TemplateOption)
			throws AbstractScriptException {
		checkInit();
		callFunction("SelectTemplateOption", TemplateOption);
	}

	public boolean TemplateBaseState(String Template)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("TemplateBaseState", Template);
	}

	public void ProjectHome(String Project) throws AbstractScriptException {
		checkInit();
		callFunction("ProjectHome", Project);
	}

	/**
	 * FUNCTION: 			SelectValue CREATED BY: 		Chandrika Vepati  PURPOSE:  			Selecting the Required Value from the "Search and Select List of Values" Window INPUT PARAMS:  RequiredValue -	Value to be chosen from Table  OUTPUT PARAMS: 		None  RETURN VALUES: 		'0' if successful : 		-1 if unsuccessful
	 */
	public boolean selectValue(String RequiredValue)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("selectValue", RequiredValue);
	}

	public List<String> getColumnValuesfromExcelSheet(String inputFileName,
			String outputFileName, String worksheetName, String tableName,
			String columnHeader) throws AbstractScriptException {
		checkInit();
		return (List<String>) callFunction("getColumnValuesfromExcelSheet",
				inputFileName, outputFileName, worksheetName, tableName,
				columnHeader);
	}

	public void MinMaxViewOutput(String item, String target_col,
			String target_col_Value) throws AbstractScriptException {
		checkInit();
		callFunction("MinMaxViewOutput", item, target_col, target_col_Value);
	}

	public void webImgVerfyCheckBox(String imglabel)
			throws AbstractScriptException {
		checkInit();
		callFunction("webImgVerfyCheckBox", imglabel);
	}

	public boolean webVerfyTblValueBasedOnLabel(String label_val,
			String current_value, String proposed_value)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerfyTblValueBasedOnLabel",
				label_val, current_value, proposed_value);
	}

	public boolean Tablevalues(HashMap<String, String[]> m, String test,
			String current_value, String proposed_value)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("Tablevalues", m, test, current_value,
				proposed_value);
	}

	public void webVerfyTblValueBasedOnLabel1(String label_val)
			throws AbstractScriptException {
		checkInit();
		callFunction("webVerfyTblValueBasedOnLabel1", label_val);
	}

	public void Tablevalues(HashMap<String, String[]> m, String test)
			throws AbstractScriptException {
		checkInit();
		callFunction("Tablevalues", m, test);
	}

	public void webVerfyMergTblValBasedOnLabel(String label_val)
			throws AbstractScriptException {
		checkInit();
		callFunction("webVerfyMergTblValBasedOnLabel", label_val);
	}

	public void Table_merge(HashMap<String, ArrayList<String>> hm, String test,
			ArrayList<String> day_list) throws AbstractScriptException {
		checkInit();
		callFunction("Table_merge", hm, test, day_list);
	}

	public void SchedulesViewOutput(String date, String target_col,
			String target_col_Value) throws AbstractScriptException {
		checkInit();
		callFunction("SchedulesViewOutput", date, target_col, target_col_Value);
	}

}


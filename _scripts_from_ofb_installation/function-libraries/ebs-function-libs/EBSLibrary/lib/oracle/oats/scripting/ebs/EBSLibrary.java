//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.oracle.oats.scripting.ebs;

import java.net.*;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
import oracle.oats.scripting.modules.webdom.common.api.exception.PlaybackException;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import javax.swing.UIManager;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class EBSLibrary extends FuncLibraryWrapper
{

	public void finish() throws AbstractScriptException {
		checkInit();
		callFunction("finish");
	}

	public void initialize() throws AbstractScriptException {
		checkInit();
		callFunction("initialize");
	}

	public void run() throws AbstractScriptException {
		checkInit();
		callFunction("run");
	}

	/**
	 * This method prompts the user for the EBS Application URLS. It creates the input box and then user enters the URL for the application. The URLS are set as session  variables  which will be available throughout the session. To get the URLS user would have to  create the object of this class and call this method. This method does not return anything and this is why it is set as return type void. <p> Call for this method can be done as follows:<br> EBSLibrary.oracle_prompt_url(); <p>     To access the session variables user would need to do following <p>	    String PHP_URL = getVariables().get("oracle_php_url");<br> String JSP_URL = getVariables().get("oracle_jsp_url");<br> String ISTORE_URL = getVariables().get("oracle_istore_url");<br> String SERVER_URL = getVariables().get("oracle_server_url");<br> <p> 
	 */
	public void oracle_prompt_url() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_prompt_url");
	}

	/**
	 * This method prompts the user for the DB Connection Details. It creates the input box and then user enters the details for the DB. The details are set as session  variables  which will be available throughout the session. To get the details user would have to  create the object of this class and call this method. This method does not return anything and this is why it is set as return type void. <p> Call for this method can be done as follows:<br> EBSLibrary.oracle_prompt_sql() <p>     To access the session variables user would need to do following <p>	    getVariables().get("env_dbq");<br> getVariables().get("env_dsn);<br> getVariables().get("env_user");<br> getVariables().get("env_pw");<br> getVariables().get("env_server");<br> getVariables().get("env_port");<br> <p>
	 */
	public void oracle_prompt_sql() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_prompt_sql");
	}

	/**
	 * User can use this method within the script. This method creates the input box.<br> Returns the string keyed by user and if nothing is keyed in, it returns empty string.
	 */
	public String oracle_input_dialog(String msg)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("oracle_input_dialog", msg);
	}

	/**
	 * This method adds a Failed result and Comment to the containing parent step group summary<br> The step name should be exact as mentioned in the beginStep(stepname); method.<br> The call to this method can be made only after the step for which this method is being called.<br>
	 * @param stepName  The name of the step we are setting the failed result
	 * @param error  The error comment for this step 
	 * @example <br> beginStep("<b>Step Group Name</b>", -1);<br> {<br> <b>addFailedResult</b>("<b>Step Group Name</b>", "Error Comment");<br> }<br> endStep();
	 */
	public void addFailedResult(String stepName, String error)
			throws AbstractScriptException {
		checkInit();
		callFunction("addFailedResult", stepName, error);
	}

	/**
	 * This method adds the Comment to the containing parent step group summary. Must be called within a Step Group and stepName must match parent step group
	 * @param stepName  The name of the step we are setting the result
	 * @param comments  The passed comment for this step
	 * @example <br> beginStep("<b>Step Group Name</b>", -1);<br> {<br> <b>addPassedResult</b>("<b>Step Group Name</b>", "Comments");<br> }<br> endStep(); 
	 */
	public void addPassedResult(String stepName, String comments)
			throws AbstractScriptException {
		checkInit();
		callFunction("addPassedResult", stepName, comments);
	}

	/**
	 * This method logs the user in with the username and password.
	 * @param user  Username for the apps 
	 * @param password  password
	 */
	public void oracle_php_login(String user, String password)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_php_login", user, password);
	}

	/**
	 * This method sets focus to active web browser to bring in front of command window
	 * @throws Exception  
	 */
	public void oracle_browser_focus() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_browser_focus");
	}

	/**
	 * This method launches a new web browser and navigates to oracle PHP URL
	 * @throws Exception  
	 */
	public void oracle_launch_php_url() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_launch_php_url");
	}

	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception  
	 */
	public void oracle_launch_istore_url() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_launch_istore_url");
	}

	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception  
	 */
	public void oracle_launch_jsp_url() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_launch_jsp_url");
	}

	/**
	 * This method logs the user in with the username and password and selects the responsibility from oracle application's home page.  <br><br>
	 * @param user
	 * @param password
	 * @param resp  Responsibility to select.
	 */
	public void oracle_php_signon(String user, String password, String resp)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_php_signon", user, password, resp);
	}

	/**
	 * This function switches a responsibility within Forms applications
	 * @param resp
	 * @throws Exception
	 */
	public void oracle_switch_responsibility(String resp)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_switch_responsibility", resp);
	}

	/**
	 * This function exits applications from Forms
	 * @throws Exception
	 */
	public void oracle_exit_app() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_exit_app");
	}

	/**
	 * This function closes all browser windows.  This is required to set up the initial base state of a test.
	 * @throws Exception
	 */
	public void oracle_close_all_browsers() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_close_all_browsers");
	}

	/**
	 * This function resets all the global variables to null.
	 */
	public void cleanGlobal() throws AbstractScriptException {
		checkInit();
		callFunction("cleanGlobal");
	}

	/**
	 * This function retrieves a value for a global variable.
	 * @param var
	 * @return
	 */
	public String getGlobal(String var) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getGlobal", var);
	}

	/**
	 * This function sets a value for a global variable
	 * @param varname
	 * @param value
	 * @throws AbstractScriptException
	 */
	public void setGlobal(String varname, String value)
			throws AbstractScriptException {
		checkInit();
		callFunction("setGlobal", varname, value);
	}

	/**
	 * This functions deletes a global variable   
	 * @param var
	 */
	public void delGlobal(String var) throws AbstractScriptException {
		checkInit();
		callFunction("delGlobal", var);
	}

	/**
	 * This user-defined function sets the initial condition for Oracle Apps at the Forms Navigator window.   It ensures that the Function tab is activated and the Navigator window menus are in collapse state
	 * @throws Exception
	 */
	public void oracle_form_initial_condition() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_form_initial_condition");
	}

	/**
	 * This method navigates to the nth level on the Forms navigator specified by the user as a single string seperated by ",".   If a choice requires a comma, then enclose choice in double quotes:<br> Tree View Example: choice1,"choice2a,choice2b",choice3<br> Java Code Example: EBSLibrary.oracle_navigator_select("choice1,\"choice2a,choice2b\",choice3")
	 * @param commaSeperatedChoices  A single String seperated by ","(comma)
	 * @throws AbstractScriptException
	 */
	public void oracle_navigator_select(String commaSeperatedChoices)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_navigator_select", commaSeperatedChoices);
	}

	/**
	 * This function is used in the Framework Navigation Page and navigate to the menu function. Accepts the table heading (menuPath) as first argument and Choice(menuChoice) under that heading as second.<br><br> Non-Tree Framework Navigation - Initial Condition:<br> - Responsibility page is displayed.<br>  <br><br> Tree View Framework Navigation Menu - Initial Condition:<br> - Responsibility tree selection is expanded<br> - Only 1 Responsibility is collapsed in the Tree View UI<br> <br><br> For EBS Tree View Framework enter fw_menuPath separated by "," or " : ". If a menu path requires a "," or " : "(note the space(s)) in its value then enclose it in double quotes. <br><br> Example:<br> EBSLibrary.oracle_navigation_menu("fw_menuPath","fw_menuChoice")<br><br> EBSLibrary.oracle_navigation_menu("menuPath0 : menuPath1 : menuPath2","Choice")<br> EBSLibrary.oracle_navigation_menu("menuPath0, menuPath1, menuPath2","Choice")<br> EBSLibrary.oracle_navigation_menu("Negotiation" ,"Quote");<br> <br><br> Examples with "," or " : " in the menu value<br> EBSLibrary.oracle_navigation_menu("menuPath0,\"menuPath, with comma 1/",menuPath2","Choice")<br> EBSLibrary.oracle_navigation_menu("Mass Information eXchange: MIX","BEE Spreadsheet Interface")<br> EBSLibrary.oracle_navigation_menu("\"Orders, Returns\"", "Order Organizer")<br> <br><br>
	 * @param fw_menuPath  (Non-Tree View)Heading on the navigation page. (Tree View)Menu path under which the choice needs to be clicked on.
	 * @param fw_menuChoice  Choice to be clicked on to launch form or proceed further
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void oracle_navigation_menu(String fw_menuPath, String fw_menuChoice)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_navigation_menu", fw_menuPath, fw_menuChoice);
	}

	/**
	 * Closes the form window with the specified Title 
	 * @param title  Title of the form Window
	 * @throws AbstractScriptException
	 */
	public void oracle_formWindow_close(String title)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_formWindow_close", title);
	}

	/**
	 * This method checks if the user passed message is present in the Form Status Bar.<br> It also add the validation results to the results of the run. User can staore the value of validation as boolean for other code.
	 * @param msg  Message which need to check in the Form Status Bar. 
	 * @return  True if user passed message is present in the Form Status Bar else false.
	 * @throws Exception
	 */
	public boolean oracle_statusbar_msgck(String msg)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("oracle_statusbar_msgck", msg);
	}

	/**
	 * This user defined method clicks on the web object in the Column specified by the user and in the row<br> where the unique identifier falls.<br> Works for the window which is in focus.<br> Here is the table which defines the usage for state options:<br> <table border = 1 rules = "all" ><tr><td></td><td>CheckBox</td><td>Link</td><td>Radio</td></tr><tr><td>Null</td><td>0</td><td>1</td><td>1</td></tr><tr><td>ON</td><td>1</td><td>0</td><td>0</td></tr><tr><td>OFF</td><td>1</td><td>0</td><td>0</td></tr><tr><td>Toggle</td><td>1</td><td>0</td><td>0</td></tr></table>
	 * @param uniqueIdentifier  A unique identifier passed as String
	 * @param col  The column number where the link,image,checkbox,radio button is located; col starting from 0.
	 * @param state  The object state options. Blank will default to null. See table for more info
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void oracle_table_objClick(String uniqueIdentifier, int col,
			String state) throws AbstractScriptException {
		checkInit();
		callFunction("oracle_table_objClick", uniqueIdentifier, col, state);
	}

	/**
	 * This method selects the responsibility and then clicks on the choice under the menupath.<br> For EBS Tree View Framework enter fw_menuPath separated by "," or " : ". If a menu path requires a "," or " : "(note the space(s)) in its value then enclose it in double quotes. <br><br> Example:<br> EBSLibrary.oracle_homepage_nav("fw_resp","fw_menuPath","fw_menuChoice")<br><br> EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0 : menuPath1 : menuPath2","Choice")<br> EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0, menuPath1, menuPath2","Choice")<br> EBSLibrary.oracle_homepage_nav("Order Management Super User, Vision Operations (USA)","Negotiation" ,"Quote");<br> <br><br> Examples with "," or " : " in the menu value<br> EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0,\"menuPath, with comma 1/",menuPath2","Choice")<br> EBSLibrary.oracle_homepage_nav("Human Resources, Vision Enterprises","Mass Information eXchange: MIX","BEE Spreadsheet Interface")<br> EBSLibrary.oracle_homepage_nav("Order Management Super User, Vision Operations (USA)","\"Orders, Returns\"", "Order Organizer")<br> <br><br><br>
	 * @param fw_resp  Responsibility on the Oracle Application Home Page.  Wildcard can be used. 
	 * @param fw_menuPath  Menu path under which the choice needs to be clicked on.
	 * @param fw_menuChoice  Choice to be clicked on to launch form or proceed further
	 */
	public void oracle_homepage_nav(String fw_resp, String fw_menuPath,
			String fw_menuChoice) throws AbstractScriptException {
		checkInit();
		callFunction("oracle_homepage_nav", fw_resp, fw_menuPath, fw_menuChoice);
	}

	/**
	 * This method has the flexibility on working on any date.<br>  The date can be passed as one of the parameter. <br> This function should return the calculated dates based on date passed as parameters. Currently this metthod works only with the input date in format dd-MMM-yyyy e.g (21-JAN-1982) Once this function is called, the result is stored in the environment variable "oracle_nw_date". To get the value of this variable the following statement needs to be executed: <b> getVariables().get("oracle_nw_date");</b> Or the vaue can be directly stored into the String as follows: <b>String result = EBSLibrary.oracle_date_manipulation("12-Jan-1982", "15", "6", "4");</b>
	 * @param ddMMMyyyy  The input Date in the format DD-MMM-YYYY 
	 * @param days  Number of days to the added
	 * @param months  Number of months to be added
	 * @param years  Number of years to be added
	 * @return  The Date as String in the same format as it was passed as input (DD-MMM-YYYY)
	 * @throws Exception
	 */
	public String oracle_date_manipulation(String ddMMMyyyy, String days,
			String months, String years) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("oracle_date_manipulation", ddMMMyyyy,
				days, months, years);
	}

	/**
	 * oracle_fwNav_Wait sets the delay for the tree view framework ui navigation when using oracle_homepage_nav or oracle_navigation_menu.   Sets the wait time between clicking/expanding the tree view navigation.  The default is 20 seconds(20000 milliseconds)
	 * @param fwWaitTime  milliseconds between each clicking/expanding. Please ensure clickInterval should large than the load time of child nodes.
	 * @throws Exception
	 */
	public void oracle_fwNav_Wait(int fwWaitTime)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_fwNav_Wait", fwWaitTime);
	}

	/**
	 * This method will invoke your URL based on a defined xml file. Will launch browser and navigate to your specified URL.
	 * @param filename  The path and filename of your environment xml file
	 * @param url  The Application URL to be invoked as defined in the xml file
	 */
	public void oracle_invoke_env(String filename, String url)
			throws AbstractScriptException {
		checkInit();
		callFunction("oracle_invoke_env", filename, url);
	}

	/**
	 * This method will return Value of Variable based on a defined xml file.
	 * @param oFileName  The Path and Filename of your xml file.
	 * @param oVarName  The Name Value of variable defined in your xml file.
	 */
	public String oracle_xml_value(String oFileName, String oVarName)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("oracle_xml_value", oFileName, oVarName);
	}

}


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

/**
 * This is function library script to
 *  use by EBS QA team for testing Oracle
 *  EBS applications.
 *  The code is adopted from the 
 *  oracle.oats.scripting.modules.ebs.library.plugin.api.OracleLibrary class
 *  that implemented EBSLibrary api service for EBS Library custom plug-in
 *  This function library script provides the methods for the tester to directly call them as they were used in Winrunner.
 * Currently Coded methods:
 *  	public void oracle_prompt_url() throws Exception
 *		public String oracle_input_dialog(String msg) throws Exception
 *		public void addFailedResult(String stepName, String error)
 *		public void addPassedResult(String stepName, String comments)
 *		public void oracle_php_login(String user, String password) throws AbstractScriptException
 *		public void oracle_launch_php_url() throws Exception
 *		public void oracle_php_signon(String user, String password, String resp) throws AbstractScriptException
 *		public void oracle_switch_responsibility(String resp) throws AbstractScriptException
 *		public void oracle_exit_app() throws AbstractScriptException
 *		public void oracle_close_all_browsers() throws AbstractScriptException
 *		public void cleanGlobal()
 *		public String getGlobal(String var)
 *		public void setGlobal(String varname, String value) throws AbstractScriptException
 *		public void delGlobal(String var)
 *		public void oracle_form_initial_condition() throws AbstractScriptException
 *		public void oracle_navigator_select(String commaSeperatedChoices) throws AbstractScriptException
 *		public void oracle_navigation_menu(String fw_menuPath, String fw_menuChoice) throws Exception
 *		public void oracle_formWindow_close(String title) throws AbstractScriptException
 *		public void oracle_table_objClick(String uniqueIdentifier, int col) throws Exception
 *		public void oracle_invoke_env(String filename, String url) throws Exception
 *		public String oracle_xml_value(String oFileName, String oVarName) throws Exception
 * @author oracle.oats
 * @since oracle.oats v 12.2
 */

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	
	private String oracle_php_url = null, oracle_jsp_url = null, oracle_istore_url = null, oracle_server_url = null;
	private String db_name = null, db_DSN = null, db_user = null, db_password = null, db_server = null, db_port=null;
	private int fw_think = 20000;
	
	//This function library script is not for run.
	//The following 3 methods will be never called.
	//They need to be declared, because 
	//IteratingVUserScript declares them.
	
	public void finish() throws Exception {	
	}
	
	public void initialize() throws Exception {		
	}
	
	public void run() throws Exception {
	}
	
	/**
	 * This method prompts the user for the EBS Application URLS. It creates the input box
	 * and then user enters the URL for the application. The URLS are set as session  variables 
	 * which will be available throughout the session. To get the URLS user would have to 
	 * create the object of this class and call this method. This method does not return anything
	 * and this is why it is set as return type void.
	 * <p>
	 *     Call for this method can be done as follows:<br>
	 *     EBSLibrary.oracle_prompt_url();
	 * <p>    
	 * To access the session variables user would need to do following
	 * <p>	   
	 * 	   String PHP_URL = getVariables().get("oracle_php_url");<br>
	 * 	   String JSP_URL = getVariables().get("oracle_jsp_url");<br>
	 * 	   String ISTORE_URL = getVariables().get("oracle_istore_url");<br>
	 * 	   String SERVER_URL = getVariables().get("oracle_server_url");<br>
	 * <p> 
	 * 
	 */
	public void oracle_prompt_url() throws Exception
	{
		if (isQosRun()){
			String oraJsp = "jtflogin.jsp";
			String oraIstore = "OA_HTML/ibeCZzdMinisites.jsp";
			
			/*Getting the user input for the URLs for Apps, jsp, and istore from the command line */
			
			if(getSettings().get("oracle_php_url")!=null)
			{
				oracle_php_url = getSettings().get("oracle_php_url");
				getVariables().set("oracle_php_url", oracle_php_url);
			}
			
			if(getSettings().get("oracle_jsp_url")!=null)
			{
				oracle_jsp_url = getSettings().get("oracle_jsp_url");
				getVariables().set("oracle_jsp_url", oracle_jsp_url);
			}
			else{
				oracle_jsp_url = oracle_server_url(getVariables().get("oracle_php_url"))+oraJsp;
				getVariables().set("oracle_jsp_url", oracle_jsp_url);
			}
				
			if(getSettings().get("oracle_istore_url")!=null)
			{
				oracle_istore_url = getSettings().get("oracle_istore_url");
				getVariables().set("oracle_istore_url", oracle_istore_url);
			}
			else{
				oracle_istore_url = oracle_server_url(getVariables().get("oracle_php_url"))+oraIstore;
				getVariables().set("oracle_istore_url", oracle_istore_url);
			}
			/*Calling oracle_server_url to set oracle_server_url variable from oracle_php_url */
			oracle_server_url = oracle_server_url(getVariables().get("oracle_php_url"));
			getVariables().set("oracle_server_url", oracle_server_url);
		}
		else
		{	
			/*Getting the user input for the URLs for Apps, jsp, and istore*/
			oracle_php_url = oracle_input_dialog("Please enter the ICXINDEX or AppsLogin URL");
			oracle_jsp_url = oracle_input_dialog("Please enter the JSP Login URL");
			oracle_istore_url = oracle_input_dialog("Please enter the iStore Login URL");
			
			/*Setting the URLs' as session variables i.e. oracle_php_url, oracle_jsp_url,
			 * oracle_istore_url and oracle_server_url. These variables will be available 
			 * for the whole session of the script run*/  
			getVariables().set("oracle_php_url", oracle_php_url);
			getVariables().set("oracle_jsp_url", oracle_jsp_url);
			getVariables().set("oracle_istore_url", oracle_istore_url);
		}
	}
	/**
	 * This method trims the actual apps url using the URL class
	 * <p>
	 *     Calling this method can be done as follows:<br>
	 *     EBSLibrary.oracle_server_url(String);<br>
	 *     <br>
	 * <p>    
	 * oracle_server_url is the method which trims the actual apps url as follows:<br>
	 * if actual url is https://hanovereip.oracle.com/OA_HTML/AppsLogin then oracle_server_url
	 * will return https://hanovereip.oracle.com/ including port number if used.
	 * 
	 */
	
	private String oracle_server_url(String oracle_url) throws Exception {
		try { 
			URL eURL = new URL(oracle_url);
			String oPort = Integer.toString(eURL.getPort());			
			String oraHost = null;
		
			if(oPort.equals("-1")){
				oraHost = eURL.getProtocol()+"://"+eURL.getHost()+"/";
				return oraHost;
			}else{
				oraHost = eURL.getProtocol()+"://"+eURL.getHost()+":"+eURL.getPort()+"/";
				return oraHost;
			}	  
			
		} catch (MalformedURLException e) {
			System.err.println("Exception: "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * This method prompts the user for the DB Connection Details. It creates the input box
	 * and then user enters the details for the DB. The details are set as session  variables 
	 * which will be available throughout the session. To get the details user would have to 
	 * create the object of this class and call this method. This method does not return anything
	 * and this is why it is set as return type void.
	 * <p>
	 *     Call for this method can be done as follows:<br>
	 *     EBSLibrary.oracle_prompt_sql()
	 * <p>    
	 * To access the session variables user would need to do following
	 * <p>	   
	 * 	getVariables().get("env_dbq");<br>
	 *	getVariables().get("env_dsn);<br>
	 *	getVariables().get("env_user");<br>
	 *	getVariables().get("env_pw");<br>
	 *	getVariables().get("env_server");<br>
	 *	getVariables().get("env_port");<br>
	 * <p>
	 */
	public void oracle_prompt_sql() throws Exception
	{
		if (isQosRun()){
			if(getSettings().get("ORACLE_SID")!=null)
			{
				db_name = getSettings().get("ORACLE_SID");
				getVariables().set("env_dbq", db_name);
			}
			if(getSettings().get("DATABASE_ID")!=null)
			{
				db_DSN = getSettings().get("DATABASE_ID ");
				getVariables().set("env_dsn", db_DSN);
			}
			if(getSettings().get("USERID_1")!=null)
			{
				db_user = getSettings().get("USERID_1");
				getVariables().set("env_user", db_user);
			}
			if(getSettings().get("PASSWORD_1")!=null)
			{
				db_password = getSettings().get("PASSWORD_1");
				getVariables().set("env_pw", db_password);
			}
			if(getSettings().get("DB_SERVER")!=null)
			{
				db_server = getSettings().get("DB_SERVER");
				getVariables().set("env_server", db_server);
			}
			if(getSettings().get("DB_PORT")!=null)
			{
				db_port = getSettings().get("DB_PORT");
				getVariables().set("env_port", db_port);
			}
		}else{	
		/*Getting the user input for DB details */
		db_name = oracle_input_dialog("Please enter database name for SQL connection");
		db_DSN = oracle_input_dialog("Please enter database DSN name for SQL connection");
		db_user = oracle_input_dialog("Please enter database username for SQL connection");
		db_password = oracle_input_dialog("Please enter database password for SQL connection");
		db_server = oracle_input_dialog("Please enter database server for SQL connection");
		db_port = oracle_input_dialog("Please enter database port number for SQL connection");
		
		/*Setting the values to the environmental variables*/
		getVariables().set("env_dbq", db_name);
		getVariables().set("env_dsn", db_DSN);
		getVariables().set("env_user", db_user);
		getVariables().set("env_pw", db_password);
		getVariables().set("env_server", db_server);
		getVariables().set("env_port", db_port);
		}
	}
	
	/**
	 * User can use this method within the script. This method creates the input box.<br>
	 * Returns the string keyed by user and if nothing is keyed in, it returns empty string.
	 *    
	 */
	public String oracle_input_dialog(String msg) throws Exception
	{
		UIManager.setLookAndFeel(
	            UIManager.getCrossPlatformLookAndFeelClassName());

		String str = javax.swing.JOptionPane.showInputDialog(null, msg+":", null, 1);
		if(str!=null)
			return str;
		else
			return "";
	}
	
	/** 
	 *  
	 * This method adds a Failed result and Comment to the containing parent step group summary<br>
	 * The step name should be exact as mentioned in the beginStep(stepname); method.<br>
	 * The call to this method can be made only after the step for which this method is being called.<br>
	 * 
	 * @param stepName The name of the step we are setting the failed result
	 * @param error The error comment for this step 
	 * 
	 * @example
	 * <br>
	 * beginStep("<b>Step Group Name</b>", -1);<br>
	 * {<br>
	 * <b>addFailedResult</b>("<b>Step Group Name</b>", "Error Comment");<br>
	 * }<br>
	 * endStep();
	 */
	public void addFailedResult(String stepName, String error)
	{
		String snme;
		IScriptStepResult sr = null;
		sr = getStepResult();
		StepResult srParent = ((StepResult)sr).getParent();
		snme = srParent.getStepName();
		
		// Match StepName
		if (snme.equals(stepName)){
			srParent.addComment(error);
			srParent.setError("["+stepName+"] ");
		}
		else if (!snme.equals(stepName) || snme.equals(null)){
			System.out.println("addFailedResult: Mismatch - ["+stepName+"] does not match Parent Step group ["+snme+"]");
			sr.addWarning("addFailedResult: Mismatch - Check console for details");			
		}
	}
	
	/** 
	 * 
	 * This method adds the Comment to the containing parent step group summary.
	 * Must be called within a Step Group and stepName must match parent step group
	 * 
	 * @param stepName The name of the step we are setting the result
	 * @param comments The passed comment for this step
	 * 
	 * @example
	 * <br>
	 * beginStep("<b>Step Group Name</b>", -1);<br>
	 * {<br>
	 * <b>addPassedResult</b>("<b>Step Group Name</b>", "Comments");<br>
	 * }<br>
	 * endStep(); 
	 * 
	 */
	public void addPassedResult(String stepName, String comments)
	{
		String snme;
		IScriptStepResult sr = null;
		sr = getStepResult();
		StepResult srParent = ((StepResult)sr).getParent();
		snme = srParent.getStepName();
		
		//Match StepName to StepGroup Name
		if (snme.equals(stepName)){
			srParent.addComment("["+stepName+"] : "+comments);
		}
		else if (!snme.equals(stepName) || snme.equals(null)){
			System.out.println("addPassedResult: Mismatch - ["+stepName+"] does not match Parent Step group ["+snme+"]");
			sr.addWarning("addPassedResult: Mismatch - Check console for details");
		}
	}

	/**
	 * This method logs the user in with the username and password.
	 * @param user Username for the apps 
	 * @param password password
	 */
	public void oracle_php_login(String user, String password) throws AbstractScriptException
	{
			String pageindex = web.getFocusedWindow().getAttribute("index");
			
			//Bug 10095918 - Pause on Exception fix
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			
			web
				.textBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:form[@index='0' or @id='DefaultFormName' or @name='DefaultFormName']/web:input_text[@name='usernameField' or @name='username']")
				.setText(user);
			
			web
				.textBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:form[@index='0' or @id='DefaultFormName' or @name='DefaultFormName']/web:input_password[@name='passwordField' or @name='password']")
				.setPassword(password);
			
			if(web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:button[@id=SubmitButton]"))
			{
				web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:button[@id=SubmitButton]")
				.click();
			}
			else
			{
				web
					.textBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:form[@index='0' or @id='DefaultFormName' or @name='DefaultFormName']/web:input_password[@name='passwordField' or @name='password']")
					.pressEnter();
			}
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
	}

	/**
	 * This method sets focus to active web browser to bring in front of command window
	 * @throws Exception 
	 */
	public void oracle_browser_focus() throws Exception
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		System.out.println("oracle_browser_focus:pageindex"+ pageindex);
		delay(5000);
		//Bug 14372574 - Bring browser forward with min and max
		System.out.println("Browser: Minimized");
		web.window("/web:window[@index='"+pageindex+"']").minimize();
		delay(5000);
		System.out.println("Browser: Maximized");
		web.window("/web:window[@index='"+pageindex+"']").maximize();
		delay(5000);
	}
	/**
	 * This method launches a new web browser and navigates to oracle PHP URL
	 * @throws Exception 
	 */
	public void oracle_launch_php_url() throws Exception
	{
		if (isQosRun()){
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			oracle_browser_focus(); // For QOS to bring IE window to the front.
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_php_url"));			
		}
		else{
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_php_url"));
		}
	}
	
	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception 
	 */
	public void oracle_launch_istore_url() throws Exception //[Added by SAJOHRI] [04-JAN-2010][Requested by Amy]
	{
		if (isQosRun()){
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			oracle_browser_focus(); // For QOS to bring IE window to the front.
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_istore_url"));
		}
		else
		{
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_istore_url"));
		}
	}
	
	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception 
	 */
	public void oracle_launch_jsp_url() throws Exception //[Added by SAJOHRI] [04-JAN-2010][Requested by Amy]
	{
		if (isQosRun()){
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			oracle_browser_focus(); // For QOS to bring IE window to the front.
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_jsp_url"));
		}
		else
		{
			browser.launch();
			String pageindex = web.getFocusedWindow().getAttribute("index");
			web.window(12,"/web:window[@index='"+pageindex+"']").navigate(getVariables().get("oracle_jsp_url"));			
		}
	}
	
	/**
	 * This method logs the user in with the username and password and selects the responsibility from
	 * oracle application's home page. 
	 * <br><br>
	 * @param user
	 * @param password
	 * @param resp Responsibility to select.
	 */
	public void oracle_php_signon(String user, String password, String resp) throws AbstractScriptException
	{
		info("oracle_php_signon:\""+user+"\", \""+resp+"\"");
		String pageindex = web.getFocusedWindow().getAttribute("index"),mSep="#";
		oracle_php_login(user, password);
		delay(2000);
		boolean isNewViewEnabled = web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:table[@id='menuTable']");
		if (isNewViewEnabled)
		{
			System.out.println("oracle_php_signon: Tree-View Framework");
			String mPath = resp+mSep;
			String[] soMenu = mPath.split("#");
			clickRespLinkViaJs(500, soMenu);
			delay(2000);
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			if (web.element("//web:table[@id='respList']").exists()){
				System.out.println("oracle_php_signon: Wait for responsibility to expand");
				web.element("//web:img[@alt='collapse']").waitFor(60);
			}else{
				System.out.println("oracle_php_signon: Responsibility navigates to page");
			}
//			web.element("//web:img[@alt='collapse']").waitFor(60);
//			// MousClick Link performs faster than click in tree view framework
//			web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+resp+"']").mouseClick(5,5);
//			web.element("//web:img[@alt='collapse']").waitFor(30);
		}
		else
		{
			web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+resp+"']").click();
		}
		web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
		delay(2000);
	}
	
	/**
	 * This function switches a responsibility within Forms applications
	 * @param resp
	 * @throws Exception
	 */
	public void oracle_switch_responsibility(String resp) throws AbstractScriptException
	{
		
		forms
			.window("//forms:window[(@name='NAVIGATOR')]")
			.selectMenu("File|Switch Responsibility...");
		
		
		
		forms
			.listOfValues("//forms:listOfValues")
			.find(resp);
	}
	
	/**
	 * This function exits applications from Forms
	 * @throws Exception
	 */
	public void oracle_exit_app() throws Exception 
	{
		int winCount = 0;
		int splCount = 0;

		//Count the number of Web Windows Open
		for (int w = 0 ; w < 100; w++) 
        {
              if (! web.exists("/web:window[index='"+w+"']")) {
                  winCount = w;
                  break;
              }
        }
		//For all open Web Windows check for the image
        for(int i=0; i<=winCount; i++)
        {
        	if(web.exists("/web:window[@index='"+i+"']/web:document[@index='0']/web:img[@alt='IMPORTANT: Do not close this window. Closing this window will cause Oracle Forms-based applications to close immediately, losing any unsaved data. This window may be minimized safely at any time and may be closed once all work in Oracle Forms-based applications is complete.']"))
        	{
        		splCount =  i;
        		break;
        	}
        }
        
		if(forms.window("//forms:window[(@name='NAVIGATOR')]").exists())
		{
			System.out.println("oracle_exit_app: Forms window found");
			forms
			.window("//forms:window[(@name='NAVIGATOR')]")
			.selectMenu("File|Exit Oracle Applications");

			forms
			.choiceBox("//forms:choiceBox")
			.clickButton("OK");
    	
			if(web.exists("/web:window[@index='"+splCount+"']/web:document[@index='0']/web:img[@alt='IMPORTANT: Do not close this window. Closing this window will cause Oracle Forms-based applications to close immediately, losing any unsaved data. This window may be minimized safely at any time and may be closed once all work in Oracle Forms-based applications is complete.']"))
			{
				web.window("/web:window[@index='"+splCount+"']/web:document[@index='0']/web:img[@alt='IMPORTANT: Do not close this window. Closing this window will cause Oracle Forms-based applications to close immediately, losing any unsaved data. This window may be minimized safely at any time and may be closed once all work in Oracle Forms-based applications is complete.']").close();
			}
		}
		else
		{
			System.out.println("oracle_exit_app: Forms window NOT found");
		}
	}
	
	/**
	 * This function closes all browser windows. 
	 * This is required to set up the initial base state of a test.
	 * @throws Exception
	 */
	public void oracle_close_all_browsers() throws Exception
	{
		browser.closeAllBrowsers();
	}
	
	/**
	 * This function resets all the global variables to null.
	 */
	public void cleanGlobal()
	{
		getVariables().clearVariables();
	}
	
	/**
	 * This function retrieves a value for a global variable.
	 * @param var
	 * @return
	 */
	public String getGlobal(String var) throws AbstractScriptException
	{
		return getVariables().get(var, Variables.Scope.GLOBAL);
	}
	
	/**
	 * This function sets a value for a global variable
	 * @param varname
	 * @param value
	 * @throws AbstractScriptException
	 */
	public void setGlobal(String varname, String value) throws AbstractScriptException
	{
		getVariables().set(varname, value, Variables.Scope.GLOBAL);
	}
	
	/**
	 * This functions deletes a global variable   
	 * @param var
	 */
	public void delGlobal(String var)
	{
		getVariables().remove(var, Variables.Scope.GLOBAL);
	}
	
	/**
	 * This user-defined function sets the initial condition for Oracle Apps at the Forms Navigator window.  
	 * It ensures that the Function tab is activated and the Navigator window menus are in collapse state
	 * @throws Exception
	 */
	public void oracle_form_initial_condition() throws AbstractScriptException
	{
		forms
			.textField("//forms:textField[(@name='NAVIGATOR_TYPE_0')]")
			.setFocus();
		
		forms
			.button("//forms:button[(@name='NAV_CONTROLS_COLLAPSE_ALL_0')]")
			.click();
	}
	
	/**
	 * This method navigates to the nth level on the Forms navigator specified by the user as a single string seperated by ",".  
	 * If a choice requires a comma, then enclose choice in double quotes:<br>
	 * Tree View Example: choice1,"choice2a,choice2b",choice3<br>
	 * Java Code Example: EBSLibrary.oracle_navigator_select("choice1,\"choice2a,choice2b\",choice3")
	 * @param commaSeperatedChoices A single String seperated by ","(comma)
	 * @throws AbstractScriptException
	 */
	public void oracle_navigator_select(String commaSeperatedChoices) throws AbstractScriptException
	{
		//Previous split
		//String choices[] = commaSeperatedChoices.split(",");
		
		//New String to resolve issue if a comma is required in the choices.
		//Bug 10064757 (mark v)
		String choices[] = commaSeperatedChoices.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		for(int i=0; i<choices.length;i++)
		{
			choices[i]=choices[i].replace('"', ' ');
			choices[i]=choices[i].trim();
		}

		oracle_navigator_select_processor(choices);
	}
	
	/**
	 * Internal Use Only. Cannot be used in the script
	 * @param choices
	 * @throws AbstractScriptException
	 */
	private void oracle_navigator_select_processor(String...choices) throws AbstractScriptException
	{
		String selectItem = null;
		String delimiter = "|"; //Added By Miao
		String matchFormat = getSettings().get("ft.matchFormat");
	
		for(int i=0; i<choices.length; i++)
		{
			if(i == 0)
			{
				selectItem = choices[i];
			}
			else
			{
				// Changing Miao's update based on Bug 14182168	
				//Added by Miao begin
                //If we use regular expression to match path, then we should escape "|"
				// 			
//                if ("wildcardThenRegex".equals(matchFormat) || "Regex".equals(matchFormat))
//                {
////                   delimiter = "\\|";
//                	 selectItem = selectItem+"\\|"+choices[i];
//                }
//                else
//                {
//                	selectItem = selectItem+delimiter+choices[i];
//                }
                //Added By Miao End
//				selectItem = selectItem+"|"+choices[i];
                selectItem = selectItem+delimiter+choices[i];
			}
			forms.textField("//forms:textField[(@name='NAVIGATOR_TYPE_0')]").setFocus();
			forms.treeList("//forms:treeList[(@name='NAVIGATOR_LIST_0')]").selectItem(selectItem);
			System.out.println(matchFormat);
			System.out.println(selectItem);	
		}
	}

	/**
	 * This function is used in the Framework Navigation Page and navigate to the menu function. Accepts the table heading (menuPath) as first argument and Choice(menuChoice) under that heading as second.<br><br>
	 * Non-Tree Framework Navigation - Initial Condition:<br>
	 * - Responsibility page is displayed.<br> 
	 * <br><br>
	 * Tree View Framework Navigation Menu - Initial Condition:<br>
	 * - Responsibility tree selection is expanded<br>
	 * - Only 1 Responsibility is collapsed in the Tree View UI<br>
	 * <br><br>
	 * 
	 * For EBS Tree View Framework enter fw_menuPath separated by "," or " : ". If a menu path requires a "," or " : "(note the space(s)) in its value then enclose it in double quotes.
	 * <br><br>
	 * Example:<br>
	 * EBSLibrary.oracle_navigation_menu("fw_menuPath","fw_menuChoice")<br><br>
	 * 
	 * EBSLibrary.oracle_navigation_menu("menuPath0 : menuPath1 : menuPath2","Choice")<br>
	 * EBSLibrary.oracle_navigation_menu("menuPath0, menuPath1, menuPath2","Choice")<br>
	 * EBSLibrary.oracle_navigation_menu("Negotiation" ,"Quote");<br>
	 * <br><br>
	 * Examples with "," or " : " in the menu value<br>
	 * EBSLibrary.oracle_navigation_menu("menuPath0,\"menuPath, with comma 1/",menuPath2","Choice")<br>
	 * EBSLibrary.oracle_navigation_menu("Mass Information eXchange: MIX","BEE Spreadsheet Interface")<br>
	 * EBSLibrary.oracle_navigation_menu("\"Orders, Returns\"", "Order Organizer")<br>
	 * <br><br>
	 * @param fw_menuPath (Non-Tree View)Heading on the navigation page. (Tree View)Menu path under which the choice needs to be clicked on.
	 * @param fw_menuChoice Choice to be clicked on to launch form or proceed further
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void oracle_navigation_menu(String fw_menuPath, String fw_menuChoice) throws Exception
	{
		info("oracle_navigation_menu: \""+fw_menuPath+"\", \""+fw_menuChoice+"\"");
		String pageindex = web.getFocusedWindow().getAttribute("index");
		String fw_Parent = "";
		String mSep="#";
		boolean isNewViewEnabled = web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:table[@id='menuTable']");
		boolean isParentVisible = web.element("//web:img[@alt='collapse']").exists();
		
		//added to support R12.1.2 new Framwork UI Tree view
		if(isNewViewEnabled)
		{
//			New Code
			if (isParentVisible)  //Check for expanded responsibility
			{
				System.out.println("Parent Is Visible");
				// Get the Parent by looking for img alt=collapse.  Only works if 1 resp is collapsed
				// Removed getImgText methods with below statement to improve performance - Bug 13552806
				fw_Parent = web.element("//web:img[@alt='collapse']").getParent().getAttribute("text");
			}
			else
			{
				System.out.println("Parent is NOT Visible");
				fail("Failed Initial Condition for tree view framework");
			}
			
			String mPath=fw_Parent+mSep;
		
			if (!fw_menuPath.isEmpty()){ // If menu path is not empty then split the path
				String sMenu[] = fw_menuPath.split(" : (?=([^\"]*\"[^\"]*\")*[^\"]*$)|,(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				for (int j=0;j<sMenu.length;j++){
					sMenu[j]=sMenu[j].replace('"', ' ');
					sMenu[j]=sMenu[j].trim();
					mPath = mPath+sMenu[j]+mSep;
				}
			}
			if (fw_Parent.equals(fw_menuPath)){ // If Resp is equal to menu path then we know clicking on the resp will navigate to choice.
				web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+fw_Parent+"']").click();
				web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			}else{
				mPath = mPath+fw_menuChoice;
				String[] mMenu=mPath.split("#");
				clickRespLinkViaJs(fw_think, mMenu); // Call to external clickRespLinkViaJs
				web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
			}
		}
		else //If 12.1.2 new UI view is not enabled
		{	
//			System.out.println("oracle_navigation_menu: Static Framework UI");
			//Click on the heading
			web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:span[@text='"+fw_menuPath+"']").click();
			//Create the list of the elements with tag <TD> in the parent table of Heading 
			List<DOMElement> elementsInTable = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:span[@text='"+fw_menuPath+"']").getParent().getParent().getParent().getElementsByTagName("TD");
		
			//Initialize the boolean variable as false which means the heading is not found. 
			boolean headFound = false;
		
			//read through the list
			for(int i=0; i<elementsInTable.size(); i++)
			{
				if(elementsInTable.get(i).getAttribute("text")!=null)
				{
					//If heading is found and the choice is found as well
					if(elementsInTable.get(i).getAttribute("text").equals(fw_menuChoice) && headFound)
					{
						List<DOMElement> elemInTD = elementsInTable.get(i).getElementsByTagName("A");
						//Click on this element and break the for loop
						elemInTD.get(0).click();
						break;
					}
					//Find the heading
					if(elementsInTable.get(i).getAttribute("text").equals(fw_menuPath))
					{
						headFound = true;//If heading is found set the variable to true.
					}
				}
			}
			web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
		}
		delay(2000);
	}
	
	/**
	 * Closes the form window with the specified Title 
	 * @param title Title of the form Window
	 * @throws AbstractScriptException
	 */
	public void oracle_formWindow_close(String title) throws AbstractScriptException
	{	
		try
		{
			forms.window("//forms:window[@title='"+title+"']").close();
		}
		catch (Exception ofwc)
		{
			// Get active form window title
			String fName = forms.window("//forms:window[(@active='true')]").getTitle();
			System.out.println("Title Mismatch:");
			System.out.println("User Submitted Title - \""+title+"\"");
			System.out.println("Current Active Title -  \""+fName+"\"");
			System.err.println("Caught Exception: "
					+ ofwc.getMessage());
		}
	}
	
	/**
	 * This method checks if the user passed message is present in the Form Status Bar.<br>
	 * It also add the validation results to the results of the run. User can staore the value of validation as boolean for other code.
	 * @param msg Message which need to check in the Form Status Bar. 
	 * @return True if user passed message is present in the Form Status Bar else false.
	 * @throws Exception
	 */
	
	public boolean oracle_statusbar_msgck(String msg) throws Exception
	{
		String msg1=null;
		msg1 = forms.getStatusBarMessage();
		boolean isPresent = msg1.contains(msg);
		
		this.addValidation(msg, isPresent);
		
		return isPresent;
	}
	
	private void addValidation(String msg, boolean passed) throws AbstractScriptException
	{
		if(passed) {
	        info("PASSED: Validation of String \""+ msg+"\" Passed");
		}
	    else {
	        warn("FAILED: Validation of String \""+ msg+"\" Failed");
	    }
	}
	
	/**
	 * This user defined method clicks on the web object in the Column specified by the user and in the row<br>
	 * where the unique identifier falls.<br>
	 * Works for the window which is in focus.<br>
	 * Here is the table which defines the usage for state options:<br>
	 * 
	 * <table border = 1 rules = "all" ><tr><td></td><td>CheckBox</td><td>Link</td><td>Radio</td></tr><tr><td>Null</td><td>0</td><td>1</td><td>1</td></tr><tr><td>ON</td><td>1</td><td>0</td><td>0</td></tr><tr><td>OFF</td><td>1</td><td>0</td><td>0</td></tr><tr><td>Toggle</td><td>1</td><td>0</td><td>0</td></tr></table>
	 * 
	 * @param uniqueIdentifier A unique identifier passed as String
	 * @param col The column number where the link,image,checkbox,radio button is located; col starting from 0.
	 * @param state The object state options. Blank will default to null. See table for more info
	 *  
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void oracle_table_objClick(String uniqueIdentifier, int col, String state) throws Exception
	{
		delay(2000);
		String pageindex = web.getFocusedWindow().getAttribute("index");
		boolean isStateEmpty = state.isEmpty();
		int docindex = 0; // (10.6.0 - mv) Separating from pageindex in event docindex differs from pageindex.
	
		boolean docxPassed = false; // Bug 13964751
		while (docindex < 10)
		{
			boolean docxExist = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:td[@text='"+uniqueIdentifier+"']").exists();			
			if (!docxExist)
			{
				System.out.println("oracle_table_objClick - Locate Doc Index: (index "+docindex+") Not found.");
				docindex++;
				System.out.println("oracle_table_objClick - Locate Doc Index: Trying next index ("+docindex+")");
				if(docindex > 10)
					{
					break;
					}
			}else{
				System.out.println("oracle_table_objClick - Locate Doc Index: (index "+docindex+") Found.");
				docxPassed = true;
				break;
			}
		}
		if (!docxPassed)
		{
			fail("oracle_table_objClick: Failed to find document object index");
		}		
		
		// (10.6.0 - mv) Replacing pageindex with docindex in web:document objpaths.
		DOMElement row_of_element = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:td[@text='"+uniqueIdentifier+"']").getParent();
		//DOMElement row_of_element = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:td[@text='"+uniqueIdentifier+"']").getParent();
		//web.element("/web:window[@index='0']/web:document[@index='0']/web:td[@text='"+uniqueIdentifier+"']").click();
		List<DOMElement> elements_in_row = row_of_element.getElementsByTagName("TD");
		System.out.println("state is defined as: "+state);
		
		if(state.equalsIgnoreCase("null")||isStateEmpty)
		{
			if(elements_in_row.get(col).getElementsByTagName("A").size()!=0)
				((DOMElement)elements_in_row.get(col).getElementsByTagName("A").get(0)).click();
			else if(elements_in_row.get(col).getElementsByTagName("INPUT").size()!=0)
			{
				if(((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).getAttribute("type").equalsIgnoreCase("radio"))
				{
					((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).click();
				}
			}
		}
		else
		{
			if(elements_in_row.get(col).getElementsByTagName("INPUT").size()!=0)
			{
				if(((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).getAttribute("type").equalsIgnoreCase("checkbox"))
				{
					String elementName = ((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).getAttribute("name");
					String checkx = ((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).getAttribute("index");
					int formx = 0; // (12.0.1 - bug10388429 - Fix for changing forms index) 

					boolean formxPassed = false; // Bug 13964751
					while (formx < 10) // (12.0.1 - bug10388429 - Fix for changing forms index. Catch exception) 
					{
						boolean formxExist = web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+checkx+"']").exists(1, TimeUnit.SECONDS);
						if (!formxExist)
						{
							System.out.println("oracle_table_objClick - Locate Form Index: (index "+formx+") Not found.");
							formx++;
							System.out.println("oracle_table_objClick - Locate Form Index: Trying next index ("+formx+")");
							if(formx > 10)
								{
								break;
								}
						}else{
							System.out.println("oracle_table_objClick - Locate Form Index: (index "+formx+") Found.");
							formxPassed = true;
							break;
						}
					}
					// Checkbox
					if(state.equalsIgnoreCase("on")&&formxPassed){					
						web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+checkx+"']")
						.check(true);
					}
					else if(state.equalsIgnoreCase("off")&&formxPassed){
						web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+checkx+"']")
						.check(false);
					}
					else if(state.equalsIgnoreCase("toggle")&&formxPassed){
						web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+checkx+"']")
						.click();
					}else{
						fail("oracle_table_objClick: Failed to find form object");
					}
				}
			}
		}
	}
	
	/**
	 * This method selects the responsibility and then clicks on the choice under the menupath.<br>
	 * For EBS Tree View Framework enter fw_menuPath separated by "," or " : ". If a menu path requires a "," or " : "(note the space(s)) in its value then enclose it in double quotes.
	 * 
	 * <br><br>
	 * Example:<br>
	 * EBSLibrary.oracle_homepage_nav("fw_resp","fw_menuPath","fw_menuChoice")<br><br>
	 * 
	 * EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0 : menuPath1 : menuPath2","Choice")<br>
	 * EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0, menuPath1, menuPath2","Choice")<br>
	 * EBSLibrary.oracle_homepage_nav("Order Management Super User, Vision Operations (USA)","Negotiation" ,"Quote");<br>
	 * <br><br>
	 * Examples with "," or " : " in the menu value<br>
	 * EBSLibrary.oracle_homepage_nav("Responsibility", "menuPath0,\"menuPath, with comma 1/",menuPath2","Choice")<br>
	 * EBSLibrary.oracle_homepage_nav("Human Resources, Vision Enterprises","Mass Information eXchange: MIX","BEE Spreadsheet Interface")<br>
	 * EBSLibrary.oracle_homepage_nav("Order Management Super User, Vision Operations (USA)","\"Orders, Returns\"", "Order Organizer")<br>
	 * <br><br><br>
	 * @param fw_resp Responsibility on the Oracle Application Home Page.  Wildcard can be used. 
	 * @param fw_menuPath Menu path under which the choice needs to be clicked on.
	 * @param fw_menuChoice Choice to be clicked on to launch form or proceed further
	 * 
	 */
	public void oracle_homepage_nav(String fw_resp, String fw_menuPath, String fw_menuChoice) throws Exception
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
		boolean isWildCard = fw_resp.contains("*"); // Bug 13400208	
		if (isWildCard){
			System.out.println("Wildcard Initiated on: "+fw_resp);
			String fw_resp_wc = web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+fw_resp+"']").getAttribute("text");
			fw_resp = fw_resp_wc;
			System.out.println("WildCard matched the following: "+fw_resp);
		}
		boolean isNewViewEnabled = web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:table[@id='menuTable']");
		if (isNewViewEnabled){
//			New Code
			System.out.println("Tree-View Framework Found");
			String mSep="#";
			String mPath=fw_resp+mSep;
			if (!fw_menuPath.isEmpty()){ // If menu path is not empty
				String sMenu[] = fw_menuPath.split(" : (?=([^\"]*\"[^\"]*\")*[^\"]*$)|,(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				for (int j=0;j<sMenu.length;j++){
					sMenu[j]=sMenu[j].replace('"', ' ');
					sMenu[j]=sMenu[j].trim();
					mPath = mPath+sMenu[j]+mSep;
				}
			}
			mPath = mPath+fw_menuChoice;
			String[] mMenu=mPath.split("#");
			// Call clickRespLinkViaJs Method
//			System.out.println(mPath);
			clickRespLinkViaJs(fw_think, mMenu);	
		}
		else{		
		/*Click on Responsibility on the Home Page*/
		web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+fw_resp+"']").click();
		
		/*Call oracle_navigation_menu()*/
		if(!fw_menuPath.trim().equals("")) //[by SAJOHRI on 24-Nov-2009{{Drop 9 Addition}}]Added to Address the issue if menupath and choice are null
		oracle_navigation_menu(fw_menuPath, fw_menuChoice);
		}
		think(10);
	}
	
	/**
	 * This method has the flexibility on working on any date.<br> 
	 * The date can be passed as one of the parameter. <br>
	 * This function should return the calculated dates based on date passed as parameters.
	 * Currently this metthod works only with the input date in format dd-MMM-yyyy e.g (21-JAN-1982)
	 * Once this function is called, the result is stored in the environment variable "oracle_nw_date".
	 * To get the value of this variable the following statement needs to be executed:
	 *<b> getVariables().get("oracle_nw_date");</b>
	 * Or the vaue can be directly stored into the String as follows:
	 * <b>String result = EBSLibrary.oracle_date_manipulation("12-Jan-1982", "15", "6", "4");</b>
	 * @param ddMMMyyyy The input Date in the format DD-MMM-YYYY 
	 * @param days Number of days to the added
	 * @param months Number of months to be added
	 * @param years Number of years to be added
	 * @return The Date as String in the same format as it was passed as input (DD-MMM-YYYY)
	 * @throws Exception
	 */
	public String oracle_date_manipulation(String ddMMMyyyy, String days, String months, String years) throws Exception
	{
		String result = null;
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date myDate = (Date)formatter.parseObject(ddMMMyyyy);
		//Date date = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(myDate);
		//System.out.println("Current Date: "+formatter.format(date.getTime()));
		date.add(Calendar.DATE, Integer.parseInt(days));
		date.add(Calendar.MONTH, Integer.parseInt(months));
		date.add(Calendar.YEAR, Integer.parseInt(years));
		
		//System.out.println("New Date: "+formatter.format(date.getTime()));
		result = formatter.format(date.getTime());
		getVariables().set("oracle_nw_date", result);
		return result;
	}
	
	/**
	 * oracle_fwNav_Wait sets the delay for the tree view framework ui navigation when using oracle_homepage_nav or oracle_navigation_menu.  
	 * Sets the wait time between clicking/expanding the tree view navigation.  The default is 20 seconds(20000 milliseconds)
	 * @param fwWaitTime milliseconds between each clicking/expanding. Please ensure clickInterval should large than the load time of child nodes.
	 * @throws Exception
	 */
	public void oracle_fwNav_Wait(int fwWaitTime) throws Exception{
		fw_think = fwWaitTime;
		return;
	}
		
	/**
	 * Click RespList Links via Javascript
	 * @param clickInterval milliseconds between each clicking/expanding. Please ensure clickInterval should large than the load time of child nodes.
	 * @param linkText Text array to be expanded/clicked.
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
	private boolean clickRespLinkViaJs(long clickInterval, String... linkText) throws AbstractScriptException
	{
		String ebsRespJsp; //Bug 16246847 SKYROS Support
		if (linkText.length==0) return false;
		try{
			if (isSkyros()){
				ebsRespJsp = "{{@resourceFile(OATS_EBSTreeViewClickFunctionsSkyRos.js)}}";
			}else{
				ebsRespJsp = "{{@resourceFile(OATS_EBSTreeViewClickFunctions.js)}}";
			}
			String jsContent =  eval(ebsRespJsp);
//			String jsContent =  eval("{{@resourceFile(OATS_EBSTreeViewClickFunctions.js)}}");
			DOMElement respListTable = web.element("//web:table[@id='respList']");
			respListTable.waitFor(); // Bug Fix 13104130 
//			JOptionPane.showMessageDialog(null,"Checking");
			DOMDocument document =  respListTable.getDocument();
			document.executeJavaScript(jsContent);
			String arrArg = "new Array(";
			for(int i=0; i< linkText.length;++i){
				arrArg+= (i==0?"":",") + "\""+ linkText[i] +"\"";
			}
			arrArg +=")";
			String exeScript ="OATS_EBSTreeViewClickRespLink('respList',"+arrArg+","+clickInterval+");";
			document.executeJavaScript(exeScript);
			Thread.sleep(linkText.length * clickInterval);
			return true;
		}catch(Exception ex){
			if (ex instanceof AbstractScriptException){
				throw (AbstractScriptException)ex;
			}else{
				throw PlaybackException.createPlaybackException(ex.getLocalizedMessage());
			}
		}
	}
	/**
	 * This method will invoke your URL based on a defined xml file.
	 * Will launch browser and navigate to your specified URL.
	 * 
	 * @param filename The path and filename of your environment xml file
	 * @param url The Application URL to be invoked as defined in the xml file
	 */
	public void oracle_invoke_env(String filename, String url) throws Exception
	{	
		if (!filename.isEmpty() && !url.isEmpty()){
			XML xml = utilities.loadXML(filename);
			String[] envFile = xml.findByXPath("//Variable[Name='"+url+"']/Value/text()");
			
			if(envFile.length>1){
				warn("oracle_invoke_url: Duplicate entries for "+url);
			}
			else if (envFile.length == 0){
				warn("oracle_invoke_url: Cannot Find "+url);
			}
			else{
				String urlVal = envFile[0];
				oracle_launch_url(urlVal);
				// Bug 14490790
				getVariables().set(url, urlVal, Variables.Scope.GLOBAL);
				info(getVariables().get(url));
			}
		}else{
			fail("oracle_invoke_url: Empty Parameter");
		}		
	}	
//	@SuppressWarnings("unchecked")
	private void oracle_launch_url(String urlPath) throws Exception
	{
		browser.launch();
		String pageindex = web.getFocusedWindow().getAttribute("index");
		web.window("/web:window[@index='"+pageindex+"']").navigate(urlPath);
	}	
	/**
	 * This method will return Value of Variable based on a defined xml file.
	 *  
	 * @param oFileName The Path and Filename of your xml file.
	 * @param oVarName The Name Value of variable defined in your xml file.
	 */
//	@SuppressWarnings("unchecked")
	public String oracle_xml_value(String oFileName, String oVarName) throws Exception
	{
		if (!oFileName.isEmpty() && !oVarName.isEmpty()){
			if (XmlExist(oFileName)){
				XML xml = utilities.loadXML(oFileName);
				String[] oXmlVal = xml.findByXPath("//Variable[Name='"+oVarName+"']/Value/text()");
				if (oXmlVal.length>1 || oXmlVal.length == 0){
					warn("oracle_xml_value: XML File is empty or you have duplicate entries");
					return null;
				}
				else{
					String oValue = oXmlVal[0];
					return oValue;
				}
			}
			else{
				System.out.println("oracle_xml_value: Cannot find XML Path");
				return null;
			}
		}else{
			fail("oracle_xml_value: Error empty parameter");
			return null;
		}
	}	

	private boolean XmlExist(String xmlPath) throws Exception
	{
		File f = new File(xmlPath);
		InputStream is;
		if (f.exists()){
//			System.out.println("XmlExist: XML File Exists");
			return true;
		}
		else{
			System.out.println("XmlExist: Cannot find XML File");
			return false;
		}
	}
	
	private boolean isQosRun() throws Exception
	{
			String farmIdPath = "C:\\admin\\Fusion_OATS_Resources\\farmId.xml";
			if (XmlExist(farmIdPath)){
				String getFarmId = oracle_xml_value(farmIdPath, "farm_ident");
				if (getFarmId.equals("qos")){
					return true;
				}
				else{
					return false;
				}
			}else{
				System.out.println("isQOsRun: Cannot find XML File");
				return false;
			}
	}

	private boolean isLbRun() throws Exception
	{
		String farmIdPath = "C:\\admin\\Fusion_OATS_Resources\\farmId.xml";
		if (XmlExist(farmIdPath)){
			String getFarmId = oracle_xml_value(farmIdPath, "farm_ident");
			if (getFarmId.equals("loadbalance")){
				return true;
			}
			else{
				return false;
			}
		}else{
			System.out.println("isQOsRun: Cannot find XML File");
			return false;
		}		
	}
	private boolean isSkyros() throws Exception
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		web.window("/web:window[@index='"+pageindex+"']").waitForPage(null,true);
		if (web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:img[@src='*/OA_HTML/cabo/images/skyros/t.gif']")){
//			System.out.println("isSkyros TRUE");
			return true;
		}else{
//			System.out.println("isSkyros FALSE");
			return false;
			
		}
	}
	
}


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

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.UIManager;

import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
public class EBS {
	private int fw_think = 20000;
	IteratingVUserScript ivu;// Object for accessing the functions of all given by Openscript for each script.
	WebDomService web;
	FormsService forms;
	UtilitiesService utilities;
	BrowserService browser;

	
	//EBS	EBSLibrary	=	new EBS(web,forms.,this,browser,utilities);
	public EBS(WebDomService web,FormsService frm,IteratingVUserScript ivu, BrowserService browser,UtilitiesService utilities)
	{
		this.web		=	web;
		this.forms		=	frm;
		this.ivu		=	ivu;
		this.browser	=	browser;
		this.utilities	=	utilities;
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
			/*Getting the user input for the URLs for Apps, jsp, and istore*/
			String oracle_php_url = oracle_input_dialog("Please enter the ICXINDEX or AppsLogin URL");
			String oracle_jsp_url = oracle_input_dialog("Please enter the JSP Login URL");
			String oracle_istore_url = oracle_input_dialog("Please enter the iStore Login URL");
			
			/*Setting the URLs' as session variables i.e. oracle_php_url, oracle_jsp_url,
			 * oracle_istore_url and oracle_server_url. These variables will be available 
			 * for the whole session of the script run*/  
			ivu.getVariables().set("oracle_php_url", oracle_php_url);
			ivu.getVariables().set("oracle_jsp_url", oracle_jsp_url);
			ivu.getVariables().set("oracle_istore_url", oracle_istore_url);
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
		/*Getting the user input for DB details */
		String db_name = oracle_input_dialog("Please enter database name for SQL connection");
		String db_DSN = oracle_input_dialog("Please enter database DSN name for SQL connection");
		String db_user = oracle_input_dialog("Please enter database username for SQL connection");
		String db_password = oracle_input_dialog("Please enter database password for SQL connection");
		String db_server = oracle_input_dialog("Please enter database server for SQL connection");
		String db_port = oracle_input_dialog("Please enter database port number for SQL connection");
		
		/*Setting the values to the environmental variables*/
		ivu.getVariables().set("env_dbq", db_name);
		ivu.getVariables().set("env_dsn", db_DSN);
		ivu.getVariables().set("env_user", db_user);
		ivu.getVariables().set("env_pw", db_password);
		ivu.getVariables().set("env_server", db_server);
		ivu.getVariables().set("env_port", db_port);
		
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
	 * Method addFailedResult(stepName, error)<br> 
	 * This method adds the Failed result for the step which is passed as the argument.<br>
	 * The step name should be exact as mentioned in the beginStep(stepname); method.<br>
	 * The call to this method can be made only after the step for which this method is being called.<br>
	 * This method also adds the comment as error in the results for the this step which is user specified.
	 * 
	 * @param stepName The name of the step we are setting the failed result
	 * @param error The error comment for this step
	 * @throws AbstractScriptException 
	 */
	public void addFailedResult(String stepName, String error) throws AbstractScriptException
	{
		ivu.fail(error);
	}
	
	/** 
	 * Method addPassedResult(stepName, Comments) 
	 * This method adds the comment to the passed step.
	 * The step name should be exact as mentioned in the beginStep(stepname); method.
	 * The call to this method can be made only after the step for which this method is being called.
	 * 
	 * @param stepName The name of the step we are setting the result
	 * @param comments The passed comment for this step
	 * @throws AbstractScriptException 
	 */
	public void addPassedResult(String stepName, String comments) throws AbstractScriptException
	{
		ivu.info(comments);
	}	
	
	public void oracle_php_login(String user, String password) throws AbstractScriptException
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		
		//Bug 10095918 - Pause on Exception fix
		web.window("/web:window[@index='"+pageindex+"']").waitForPage(null);
		
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
}

	/**
	 * This method launches a new web browser and navigates to oracle PHP URL
	 * @throws Exception 
	 */
	public void oracle_launch_php_url() throws Exception
	{
		try{
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_php_url"));
		}
		catch(PlaybackException e)
		{
			browser.launch();
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_php_url"));
		}
	}

	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception 
	 */
	public void oracle_launch_istore_url() throws Exception //[Added by SAJOHRI] [04-JAN-2010][Requested by Amy]
	{
		try{
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_istore_url"));
		}
		catch(PlaybackException e)
		{
			browser.launch();
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_istore_url"));
		}
	}

	/**
	 * This method launches a new web browser and navigates to oracle iStore URL
	 * @throws Exception 
	 */
	public void oracle_launch_jsp_url() throws Exception //[Added by SAJOHRI] [04-JAN-2010][Requested by Amy]
	{
		try{
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_jsp_url"));
		}
		catch(PlaybackException e)
		{
			browser.launch();
			web.window("/web:window[@index='0']").navigate(ivu.getVariables().get("oracle_jsp_url"));
		}
	}

	/**
	 * This method logs the user in with the username and password and selects the responsibility from
	 * oracle application's home page. 
	 * @param user
	 * @param password
	 * @param resp Responsibility to select
	 */
	public void oracle_php_signon(String user, String password, String resp) throws AbstractScriptException
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		oracle_php_login(user, password);
		//Bug 10095918 - Pause on Exception fix
		web.window("/web:window[@index='"+pageindex+"']").waitForPage(null);
		web
		.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+resp+"']")
		.click();
		ivu.think(5);
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

		ivu.delay(10000);

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
		boolean isImagePresent=false;
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
				isImagePresent = true;
				splCount = i;
				break;
			}
		}

		//UIf the image is present that means the forms are open. Exit the forms now.
		if(forms.window("//forms:window[(@name='NAVIGATOR')]").exists())
		{
			forms
			.window(61, "//forms:window[(@name='NAVIGATOR')]")
			.selectMenu("File|Exit Oracle Applications");

			forms
			.choiceBox(63, "//forms:choiceBox")
			.clickButton("OK");
		}
		if(isImagePresent)
		{
			web.window("/web:window[@index='"+splCount+"']/web:document[@index='0']/web:img[@alt='IMPORTANT: Do not close this window. Closing this window will cause Oracle Forms-based applications to close immediately, losing any unsaved data. This window may be minimized safely at any time and may be closed once all work in Oracle Forms-based applications is complete.']").close();
		}

	}

	/**
	 * This function closes up to 5 IE browsers windows. 
	 * This is required to set up the initial base state of a test.
	 * @throws AbstractScriptException 
	 */
	public void oracle_close_all_browsers() throws AbstractScriptException
	{	
		// (10.5.0 - mv) Added instPath to dynamically generate path to plugin folder
		String instPath = System.getProperty("osgi.syspath").replaceAll("[\\\\]", "\\\\\\\\");
		System.out.println(instPath);
		Runtime rt = Runtime.getRuntime() ;
		try {
			Process p = rt.exec(instPath +"\\oracle_ebslibrary_support_files\\oracle_close_all_browsers.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ivu.delay(5000);
		browser.clearBrowser();//[added by SAJOHRI 1/Dec/2009{{Drop 9 Addition}}]Added to resolve the issue where this method throws error when called twice.
	}

	/**
	 * This function resets all the global variables to null.
	 */
	public void cleanGlobal()
	{
		ivu.getVariables().clearVariables();
	}

	/**
	 * This function retrieves a value for a global variable.
	 * @param var
	 * @return
	 */
	public String getGlobal(String var)
	{
		return ivu.getVariables().get(var);
	}

	/**
	 * This function sets a value for a global variable
	 * @param varname
	 * @param value
	 * @throws AbstractScriptException
	 */
	public void setGlobal(String varname, String value) throws AbstractScriptException
	{
		ivu.getVariables().set(varname, value);
	}

	/**
	 * This functions deletes a global variable   
	 * @param var
	 */
	public void delGlobal(String var)
	{
		ivu.getVariables().remove(var);
	}

	/**
	 * This user-defined function sets the initial condition for Oracle Apps at the Navigator window.  
	 * It ensures that the Function tab is activated and the Navigator window menus are in collapse state
	 * @throws Exception
	 */
	public void oracle_form_initial_condition() throws AbstractScriptException
	{
		forms
		.textField(89,"//forms:textField[(@name='NAVIGATOR_TYPE_0')]")
		.setFocus();

		forms
		.button(90,"//forms:button[(@name='NAV_CONTROLS_COLLAPSE_ALL_0')]")
		.click();
	}

	/**
	 * This method navigates to the nth level on the Forms navigator specified by the user as a single string<br>
	 * seperated by ",".
	 * @param commaSeperatedChoices A single String seperated by ","(comma)
	 * @throws AbstractScriptException
	 */
	public void oracle_navigator_select(String commaSeperatedChoices) throws AbstractScriptException
	{
		//Bug 10064757 (mv)
		String choices[] = commaSeperatedChoices.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		for(int i=0; i<choices.length;i++)
		{
			choices[i]=choices[i].replace('"', ' ');
			choices[i]=choices[i].trim();
		}
		System.out.println("Length:"+ choices.length);
		oracle_navigator_select_processor(choices);
	}

	/**
	 * Internal Use Only. Dont use in the script
	 * @param choices
	 * @throws AbstractScriptException
	 */
	private void oracle_navigator_select_processor(String...choices) throws AbstractScriptException
	{
		System.out.println(choices);
		String selectItem = null; 
		
		String delimiter = "|"; //Added By       Miao
		for(int i=0; i<choices.length; i++)
		{
			if(i == 0)
				selectItem = choices[i];
			else
			{
				String matchFormat =       ivu.getSettings().get("ft.matchFormat");
				if("wildcardThenRegex".equals(matchFormat) ||       "Regex".equals(matchFormat))
				{
					 delimiter = "\\|";
				}
				selectItem = selectItem+delimiter+choices[i];
			}	
			System.out.println("selectItem:"+selectItem);
			forms.textField("//forms:textField[(@name='NAVIGATOR_TYPE_0')]").setFocus();
			forms.treeList("//forms:treeList[(@name='NAVIGATOR_LIST_0')]").selectItem(selectItem);
			
		}
		
	}

	/**
	 * This function is used in the Framework Navigation Page and navigate to the menu function. Accepts the table heading (menuPath) as first argument and Choice(menuChoice) under that heading as second.<br><br>
	 * Non-Tree Framework Navigation - Initial Condition:<br>
	 * - Responsibility page is displayed.<br> 
	 * <br><br>
	 * Tree View Framework Navigation Menu - Initial Condition:<br>
	 * - Responsibility tree selection is collapsed<br>
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
		String pageindex = web.getFocusedWindow().getAttribute("index");
		boolean isNewViewEnabled = web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:table[@id='menuTable']");

		//added to support R12.1.2 new Framwork UI Tree view
		if(isNewViewEnabled)
		{
			//		New Code
			// Get the Parent by looking for collapsed menu.  Only works if 1 resp is collapsed
			DOMElement fw_eResp = getImgElement("collapse");
			String fw_Parent = getImgText(fw_eResp);
			String mSep="#";
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
				web.window("/web:window[@index='"+pageindex+"']").waitForPage(null);
			}else{
				mPath = mPath+fw_menuChoice;
				String[] mMenu=mPath.split("#");
				// Close the reponsibility
				web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+fw_Parent+"']").click();
				web.window("/web:window[@index='"+pageindex+"']").waitForPage(null);

				// Call to external clickRespLinkViaJs
				clickRespLinkViaJs(fw_think, mMenu);
			}
		}
		else //If 12.1.2 new UI view is not enabled
		{	
			//		System.out.println("oracle_navigation_menu: Static Framework UI");
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
		}
		ivu.think(10);
	}


	/**
	 * Closes the form window with the specified Title 
	 * @param title Title of the form Window
	 * @throws AbstractScriptException
	 */
	public void oracle_formWindow_close(String title) throws AbstractScriptException
	{
		//	forms.window("//forms:window[(@title='"+title+"')]").close();
		try
		{
			//String oPath = "//forms:window[@title='"+title+".*']"; // RegEx not resolving
			//String oPath = "//forms:window[@title='"+title+"']";
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
		if(passed)
			ivu.info("Validation of String \"" + msg + "\" Passed");
		else {
			ivu.warn("Validation of String \""+ msg+"\" Failed");
		}
	}

	/**
	 * This user defined method clicks on the object in the Column specified by the user and in the row<br>
	 * where the unique identifier falls.<br>
	 * Works for the window which is in focus.<br>
	 * Here is the table which defines the usage:
	 * <table border = 2 rules = "all" ><tr><td></td><td>CheckBox</td><td>Link</td><td>Radio</td></tr><tr><td>Null</td><td>0</td><td>1</td><td>1</td></tr><tr><td>ON</td><td>1</td><td>0</td><td>0</td></tr><tr><td>OFF</td><td>1</td><td>0</td><td>0</td></tr><tr><td>Toggle</td><td>1</td><td>0</td><td>0</td></tr></table>
	 * @param uniqueIdentifier A unique identifier passed as String
	 * @param col The column number where the image or the link is, starting from 0.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void oracle_table_objClick(String uniqueIdentifier, int col, String state) throws Exception
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		int docindex = 0; // (10.6.0 - mv) Separating from pageindex in event docindex differs from pageindex.
		while (docindex< 5) // (10.6.0 - mv) Catching the exception if docindex is not 0 then find the correct index.
		{
			try
			{
				if (web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:td[@text='"+uniqueIdentifier+"']").getParent().equals(null))
				{
					System.out.println("Exception Found with: "+docindex);
				}
				else
				{
					System.out.println("Exception NOT Encountered with: "+docindex);
					break;
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception Found with document: "+docindex+". trying next index");
				docindex++;
			}
		}
		// (10.6.0 - mv) Replacing pageindex with docindex in web:document objpaths.
		DOMElement row_of_element = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:td[@text='"+uniqueIdentifier+"']").getParent();
		//DOMElement row_of_element = web.element("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:td[@text='"+uniqueIdentifier+"']").getParent();
		//web.element("/web:window[@index='0']/web:document[@index='0']/web:td[@text='"+uniqueIdentifier+"']").click();
		List<DOMElement> elements_in_row = row_of_element.getElementsByTagName("TD");
		if(state.equalsIgnoreCase("null"))
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
					String index = ((DOMElement)elements_in_row.get(col).getElementsByTagName("INPUT").get(0)).getAttribute("index");
					int formx = 0; // (12.0.1 - bug10388429 - Fix for changing forms index) 
					while (formx< 5) // (12.0.1 - bug10388429 - Fix for changing forms index. Catch exception) 
					{
						try
						{
							if(state.equalsIgnoreCase("on")){					
								web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+index+"']")
								.check(true);
								break;
							}
							else if(state.equalsIgnoreCase("off")){
								web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+index+"']")
								.check(false);
								break;
							}
							else if(state.equalsIgnoreCase("toggle")){
								web.checkBox("/web:window[@index='"+pageindex+"']/web:document[@index='"+docindex+"']/web:form[@index='"+formx+"']/web:input_checkbox[@name='"+elementName+"' or @index='"+index+"']")
								.click();
								break;
							}
						}
						catch (Exception e)
						{
							System.out.println("Exception Found with Form index: "+formx+". trying next index");
							formx++;
						}
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
	 * <br><br>
	 * @param fw_resp Responsibility on the Oracle Application Home Page
	 * @param fw_menuPath Menu path under which the choice needs to be clicked on.
	 * @param fw_menuChoice Choice to be clicked on to launch form or proceed further
	 * 
	 */
	public void oracle_homepage_nav(String fw_resp, String fw_menuPath, String fw_menuChoice) throws Exception
	{
		String pageindex = web.getFocusedWindow().getAttribute("index");
		boolean isNewViewEnabled = web.exists("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:table[@id='menuTable']");
		if (isNewViewEnabled){
			//		New Code
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
			if (fw_resp.equals(fw_menuPath)){ // If Resp is equal to menu path then we know clicking on the resp will navigate away.
				System.out.println("Responsibility and Path Match!");
				mPath = fw_resp;
				String[] mMenu=mPath.split("#");
				// Call clickRespLinkViaJs Method
				clickRespLinkViaJs(fw_think, mMenu);	
			}else{
				mPath = mPath+fw_menuChoice;
				String[] mMenu=mPath.split("#");
				// Call clickRespLinkViaJs Method
				clickRespLinkViaJs(fw_think, mMenu);	
			}
		}
		else{		
			/*Click on Responsibility on the Home Page*/
			web.link("/web:window[@index='"+pageindex+"']/web:document[@index='"+pageindex+"']/web:a[@text='"+fw_resp+"']").click();

			/*Call oracle_navigation_menu()*/
			if(!fw_menuPath.trim().equals("")) //[by SAJOHRI on 24-Nov-2009{{Drop 9 Addition}}]Added to Address the issue if menupath and choice are null
				oracle_navigation_menu(fw_menuPath, fw_menuChoice);
		}
		ivu.think(10);
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
		ivu.getVariables().set("oracle_nw_date", result);
		return result;
	}

	/**
	 * Get the date of the format DD-MON-YYYY
	 * @param time time in miliseconds
	 */
	public String getDate(long time) throws Exception {
		String projDate = "";
		Date date = new Date(time);
		String dateString = date.toString();
		int length = dateString.length();

		String month = dateString.substring(4, 7);
		String day = dateString.substring(8, 10);
		String year = dateString.substring(length-4, length);

		projDate = day + "-" + month + "-" + year;
		return projDate;
	}

	public int getMonthNum(String mon){
		String temp = mon.toLowerCase();

		if("jan".equals(temp))
			return 1;
		else if("feb".equals(temp))
			return 2;
		else if("mar".equals(temp))
			return 3;
		else if("apr".equals(temp))
			return 4;
		else if("may".equals(temp))
			return 5;
		else if("jun".equals(temp))
			return 6;
		else if("jul".equals(temp))
			return 7;
		else if("aug".equals(temp))
			return 8;
		else if("sep".equals(temp))
			return 9;
		else if("oct".equals(temp))
			return 10;
		else if("nov".equals(temp))
			return 11;

		else
			return 12;
	}
	////////////////////////////Click Links via Javascript/////////////////////////
	private static String EBS_TREEVIEW_CLICK_FUNCTIONS_PATH = "C:\\OracleATS\\openScript\\plugins\\oracle_ebslibrary_support_files\\OATS_EBSTreeViewClickFunctions.js";
	/**
	 * Click RespList Links via Javascript
	 * @param clickInterval milliseconds between each clicking/expanding. Please ensure clickInterval should large than the load time of child nodes.
	 * @param linkText Text array to be expanded/clicked.
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	private boolean clickRespLinkViaJs(long clickInterval, String... linkText) throws AbstractScriptException
	{
		if (linkText.length==0) return false;
		try{
			byte[] fileContent=utilities.getFileService().readBytesFromFile(EBS_TREEVIEW_CLICK_FUNCTIONS_PATH);
			String jsContent =  new String(fileContent);
			DOMElement respListTable = web.element("//web:table[@id='respList']");
			respListTable.waitFor();
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
	 * oracle_fwNav_Wait sets the delay for the tree view framework ui navigation when using oracle_homepage_nav or oracle_navigation_menu.  
	 * Sets the wait time between clicking/expanding the tree view navigation.  The default is 20 seconds(20000 milliseconds)
	 * @param fwWaitTime milliseconds between each clicking/expanding. Please ensure clickInterval should large than the load time of child nodes.
	 * @throws Exception
	 */
	public void oracle_fwNav_Wait(int fwWaitTime) throws Exception{
		fw_think = fwWaitTime;
		return;
	}


	//Used by oracle_navigation_meu to find collapsed responsibility
	/**
	 * For Internal Use Only.
	 */	
	@SuppressWarnings("unchecked")
	private DOMElement getImgElement(String rootimgtext) throws Exception
	{
		//	Find the responsibility that is collapsed
		DOMElement rspListTbl = web.element("//web:table[@id='respList']");
		List<DOMElement> imgElements = rspListTbl.getElementsByTagName("img");
		if (imgElements.size()>0){
			for (DOMElement ielement : imgElements){
				String alt=ielement.getAttribute("alt");
				if (alt != null && alt.equals(rootimgtext)){
					return ielement.getParent();					
				}
			}
		}
		return null;
	}

	// Used by oracle_navigation_meu to get collapse responsibility text
	/**
	 * For Internal Use Only.
	 */	
	private String getImgText(DOMElement ielement) throws Exception
	{
		//	Get Parent text of the collapsed responsibility	
		String fw_sResp = ielement.getAttribute("text");
		return fw_sResp;

	}	
}

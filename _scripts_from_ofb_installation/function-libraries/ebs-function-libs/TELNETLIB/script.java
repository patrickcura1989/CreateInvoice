import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import oracle.oats.scripting.modules.basic.api.Arg;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.ebs.telnet.api.TelnetTerminal;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.applet.api.*;

public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;	
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	@ScriptService oracle.oats.scripting.modules.applet.api.AppletService applet;
	@ScriptService oracle.oats.scripting.modules.formsFT.api.FormsService forms;
	
	// Set debug > 0 to enabled debug message, set debug > 1 to enbaled sleep message
	private static final int debug = 1; 
	
	//private TelnetTerminal terminal = new TelnetTerminal();
	private static TelnetTerminal terminal = null;
	private int screenBufferTime; // unit in msec, screen redraw buffer time
	
	private static final int DEFAULT_TIMEOUT = 60; 
	private static final String DEFAULT_CHANGE_ORG_PATH = "Inquiry;Item";
	
	// String finals for menu options
	private static final String M_LOGOUT = "Logout";
	private static final String M_CHANGE_ORG = "Change Org";
	private static final String M_CHANGE_RESP = "Change Responsibi";
	
	// String finals for title names
	private static final String T_FIELD_DETAILS = "Field Detail Page";	
	private static final String T_DEFAULT_DEVICE = "Device List";
	private static final String T_RESP = "Choose Resp";
	
	private static final String S_ALREADY_SIGNED_ON = "Already Signed On";
	@Deprecated
	private static final String[] W_COMPLETE_MSGS = {
		"Required Field",
		"Txn Success",
		"Already Signed On",
		"loaded",
		"Invalid Value",
		"Processed",
		"No result",
		"Dock Door",
		"Enter Name",		
		"Group of picking",
		"To SN",
		"<",	
		"Legal characters",
		"Unexpected error"
	};
	   
	private static final String[] W_WAIT_MSGS = {
		"Please wait",
		"Connecting",
	};
	
	// String final for screen line messages
	private static final int I_TITLE_LINE = 0;
	private static final int I_STATUS_LINE = 15;
	private static final int I_DETAILS_LINE = 2;
	
	
	public void initialize() throws Exception {}
	public void run() throws Exception { 
		Method[] mlist= new script().getClass().getMethods();
		for(int i=0; i<mlist.length; i++)
			System.out.println(mlist[i].getName()+"    "+ mlist[i].getParameterTypes().length);
	}
	public void finish() throws Exception {}

	/**
	 * Check if the service is initialized or not
	 * @return true if the service is initialized
	 */
	public Boolean isTelnetServiceInitialized() {
		if (terminal == null) return false;
		   
		terminal.bringToFront();
		return true;
	}
	
	/**
	 * Initizlize the telnet service
	 */
	public void initializeTelnetService() throws Exception {
		if (terminal == null)
			terminal = new TelnetTerminal();
		terminal.Launch();
		screenBufferTime = 1000;	
		if (debug > 0) System.out.println("TelnetService: Screen Buffer: " + screenBufferTime);
		
		String telnetserver	=	javax.swing.JOptionPane.showInputDialog("Provide Telent Server With Host");
		getVariables().set("TELNET_SERVER", telnetserver);
		info("TELNET Url:" + telnetserver);
	}
	
	/**
	 * Connect to the server given the host and port
	 * @param host the server host name or ip
	 * @param port the server conenction port
	 */
	private void connect(@Arg("host")String host, @Arg("port")String port) throws Exception {
		if (debug > 0)System.out.println("TelnetService: Connecting to " + host + " : " + port);
		
		terminal.connect(host, Integer.parseInt(port));
		waitForTitle(T_DEFAULT_DEVICE, DEFAULT_TIMEOUT);			
		buttonPress();
		sleep(screenBufferTime);
	}
	/**
	 * Login given user ,pwd
	 * @param user user name
	 * @param pwd password 
	 */	
	public void login( @Arg("user")String user, @Arg("pwd")String pwd)  throws Exception
	{
		login(getVariables().get("TELNET_SERVER"),user,pwd);
	}
	/**
	 * Login given server, user ,pwd
	 * @param server format: host:port
	 * @param user user name
	 * @param pwd password 
	 */
	public void login(@Arg("server")String server, @Arg("user")String user, @Arg("pwd")String pwd) 
		throws Exception {
		int index = server.indexOf(":");
		if (index < 0) 
			if (debug > 0) fail("TelnetService: Server string format error: " + server);
		
		String host = server.substring(0, index);
		String port = server.substring(index + 1);
		
		connect(host, port);
		
		terminal.bringToFront();
		
		set(user);
		set(pwd);
		
		if (getStatusBar().contains(S_ALREADY_SIGNED_ON)) {
			
			for (int i = 0; i < 100; i++) {
				System.out.println("User: " + user + " is already logged in, please change to other user.");
				
				sleep(1000);
				
				if (!getStatusBar().contains(S_ALREADY_SIGNED_ON))
					break;
			}
						
		}
		
		if (getSectionTitle().contains("Warning")) {
			setWithoutEnter("Y");
			functionKey("3");
			ctrl("N");
		}
	}
	
	/**
	 * Bring to front the telnet service window
	 * @throws Exception
	 */
	public void setFocus() throws Exception {
		terminal.bringToFront();
	}
	
	/**
	 * Type the given string message and press enter button
	 * @param msg the message to type in the terminal
	 */	
	public void set(@Arg("msg")String msg) throws Exception {
		if (debug > 0) System.out.println("TelnetService: typing " + msg);
		
		char[] ca = msg.toCharArray();
		if(msg!=null)
		if(msg.toUpperCase().startsWith("#F"))
		{
			//System.out.println("Enter function keys");
			if(msg.toUpperCase().equals("#F3"))
				terminal.sendKeyMessage((char)KeyEvent.VK_F3);
			else if(msg.toUpperCase().equals("#F4"))		
			{
				//System.out.println("F4 is pressed");
				//terminal.sendKeyMessage((char)KeyEvent.VK_F4);
				terminal.sendKeyMessage(0, KeyEvent.VK_F4, KeyEvent.CHAR_UNDEFINED);
			}
		}
		else if(msg.toUpperCase().startsWith("#ENTER"))
		{
			//System.out.println("Enter is pressed");
			terminal.sendKeyMessage((char)KeyEvent.VK_ENTER);
			//terminal.sendKeyMessage(0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
		}
		else if(msg.toUpperCase().startsWith("#ESC"))
		{
			esc();
		}
			
		else
		{
			for (char c : ca) 
			{
				terminal.sendKeyMessage(c);
			}
			
			sleep(100);
			
			buttonPress();
		}
	}
	
	/**
	 * Type the given string message without pressing enter button
	 * @param msg the message to type in the terminal
	 */	
	public void setWithoutEnter(@Arg("msg") String msg) throws Exception {
		if (debug > 0) System.out.println("TelnetService: typing " + msg);
		
		char[] ca = msg.toCharArray();
		
		for (char c : ca) {
			terminal.sendKeyMessage(c);
		}
		
		sleep(100);
	}
		
	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete
	 * There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commit() throws Exception {
		if (debug > 0)System.out.println("TelnetService: Commit-Press Enter Key");
		
		terminal.sendKeyMessage((char)KeyEvent.VK_ENTER);
		
		sleep(3000);
		
		waitForActionComplete();
	}
	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete
	 * There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commitExpandAndVerify(String verifyText) throws Exception {
		if (debug > 0)System.out.println("TelnetService: Commit-Press Enter Key");
		
		terminal.sendKeyMessage((char)KeyEvent.VK_ENTER);
		
		sleep(3000);
		
		waitForActionComplete();
		String status	=	getFullStatus();
		
		if(verifyText.equals(status) || status.contains(verifyText))
			info("Message :" + verifyText + "is displayed");
		else
			reportFailure("Message :" + verifyText + "is not displayed, Actual message is : " + status);
	}		
	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete
	 * There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commitAndVerify(String verifyText) throws Exception {
		if (debug > 0)System.out.println("TelnetService: Commit-Press Enter Key");
		
		terminal.sendKeyMessage((char)KeyEvent.VK_ENTER);
		
		sleep(3000);
		
		waitForActionComplete();
		String status	=	getStatusBar();
		if(verifyText.equals(status) || status.contains(verifyText))
			info("Message :" + verifyText + "is displayed");
		else
			reportFailure("Message :" + verifyText + "is not displayed, Actual message is : " + status);
	}	
	
	/**
	 * Press the return button and sleep for 500 msec
	 */
	public void buttonPress() throws Exception {		
		if (debug > 0)System.out.println("TelnetService: Press Enter Key");
		
		terminal.sendKeyMessage((char)KeyEvent.VK_ENTER);
		
		waitForActionComplete();
	}	
	
	/**
	 * Press key with control down
	 * note: only support control key with 'A'-'Z' combinations
	 * @param msg the key to press
	 */
	public void ctrl(@Arg("msg")String msg) throws Exception {
				
		if (msg.length() != 1) {
			if (debug > 0) System.err.println("TelnetService: " + "Control key: " + msg + "is not correct.");
			return;
		}
		
		if (debug > 0) System.out.println("TelnetService: " + "Control Key + " + msg);
		
		sleep(100);
				
		char[] ca = msg.toUpperCase().toCharArray();
		
		int key = ca[0] - 'A' + 1;
		
		if (key >= 1) {			
			terminal.sendKeyMessage(KeyEvent.CTRL_DOWN_MASK, (char)key);
			
		} else {
			if (debug > 0) System.err.println("TelnetService: " + "Control key: " + msg + "is not correct.");
			return;
		}
				
		waitForActionComplete();
	} 
	
	
	/**
	 * Press key ESC
	 */
	public void esc() throws Exception {
		if (debug > 0) System.out.println("TelnetService: ESC");
		
		System.err.println("ESC COde : "+ KeyEvent.VK_ESCAPE);
		terminal.sendKeyMessage((char)KeyEvent.VK_ESCAPE);think(1);
				
		//terminal.sendKeyMessage((char)84);
		//terminal.sendKeyMessage((char)27);
	} 
	
	/**
	 * Contrl and perss enter button
	 */
	public void ctrlAndEnter(@Arg("msg")String msg) throws Exception  {
		if (debug > 0) System.out.println("TelnetService: Control and enter");
		
		ctrl(msg);
		buttonPress();
	}
	
	/**
	 * Press the down key
	 */
	public void skipDown() throws Exception {
		if (debug > 0) System.out.println("TelnetService: Skip Down");
		terminal.sendKeyMessage(0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
		
		waitForActionComplete();
	}
	
	/**
	 * Press the up key
	 */
	public void skipUp() throws Exception {
		if (debug > 0) System.out.println("TelnetService: Skip Up");
		terminal.sendKeyMessage(0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
		
		//waitForActionComplete();
	}
	
	public void functionKey(String key) throws Exception {
		int num = Integer.parseInt(key);
		
		int keyCode = KeyEvent.VK_F1 + (num - 1);
		terminal.sendKeyMessage(0, keyCode, KeyEvent.CHAR_UNDEFINED);
	}
	
	public void selectOption(String option) throws Exception {
		String[] scrLines = getScreen().split("\n");
		boolean hasIndex	=	false;
		boolean hasRegular	=	false;
		int selIndex		=	0;
		int foundIndex		=	0;
		String optArr[]	=	option.split(";");
		
		
		if(optArr.length==2)
		{
			hasIndex	=	true;
			selIndex	=	Integer.parseInt(optArr[1]);
			option		=	optArr[0];
		}	
		
		if(optArr[0].endsWith("*"))
		{
			hasRegular		=	true;
			option		=	optArr[0].substring(0,optArr[0].length()-1);
		}
			
		
		for (int i = 1; i < 11; i++) {				
			String match = scrLines[i];
			int start = match.indexOf("<");
			int end = match.indexOf(">");
			if (start < 0) continue; // not listed option, "<" not found
			
			if (end > 0) // ">" found, remove it
				match = match.substring(0, end);
			
			match = match.trim(); //remove blank chars
			if (option.equals(match.substring(start + 1)) || (hasRegular && match.substring(start + 1).contains(option))    ) 
			{
				if(hasIndex && selIndex!=foundIndex)
				{
					foundIndex++;
					continue;
				}
				terminal.sendKeyMessage(match.substring(0, 1).toCharArray()[0]); // Press the leading number
				
				if (debug > 0) System.out.println("TelnetService: option selected: " + scrLines[i]);
				
				///waitForActionComplete();
				boolean found = waitForTitle(option, 1);
				if (!found)waitForActionComplete();
				break;
			}				
		}		
		
	}
	
	/**
	 * Go to the top field - Cheney, 2011-08-02
	 */
	public void gotoTopField() throws Exception {
		if (debug > 0) System.out.println("TelnetService: Go to top field");
		for(int i=0;i<10;i++)
		{
			terminal.sendKeyMessage(0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
		}
		waitForActionComplete();
	}
	
	/**
	 * Set the screen buffer time, unit in msec
	 * @param msec screen buffer time, unit in msec
	 */
	public void setScreenBufferTime(@Arg("msec")int msec) {
		this.screenBufferTime = msec;
	}
	/**
	 * Get all the texts in the terminal screen
	 * @return Screen text
	 */
	public String getScreen() throws Exception {
		sleep(screenBufferTime); // Need to wait for the GUI to redraw
		
		return terminal.getScreenText();
	}
	
	/**
	 * Get the section title, meaning the first line in the screen
	 * @return the screen title
	 */
	public String getSectionTitle() throws Exception {
		String[] lines = getScreen().split("\n");
				
		if (debug > 0) System.out.println("TelnetService: Title: " + lines[I_TITLE_LINE]);
		
		return lines[I_TITLE_LINE].trim();
	}
	/**
	 * verify field value
	 * @param name name of the field
	 * @return true or false
	 * @throws Exception
	 */	
	
	public boolean verifyFieldValue(String fieldName,String expected) throws Exception
	{
		boolean retVal	=	false;
		String fieldValue	=	getFieldValue(fieldName);
		if(fieldValue!=null && !"".equalsIgnoreCase(fieldValue))
		{
			if(fieldValue.equals(expected))
				retVal	=	true;
		}
		else if ( fieldValue == expected )
		{
			retVal	=	true;
		}
		if(retVal==true)
			info("Expected value :" + expected + " is present in the field:" + fieldName);
		else
			reportFailure("Expected value :" + expected + " is not present in the field:" + fieldName);
		return retVal;
	}

	/**
	 * verify Status bar value
	 * @param name name of the field
	 * @return true or false
	 * @throws Exception
	 */	
	
	public boolean expandVerifyStatusBarValue(String expected) throws Exception
	{
		boolean retVal	=	false;
		String status	=	getFullStatus();
		//System.out.println("Status Returned:" + status);
		
		if(status!=null && !"".equalsIgnoreCase(status))
		{
			if(status.equals(expected) || status.contains(expected))
				retVal	=	true;
		}
		else if ( status.equals(expected ) || status.contains(expected))
		{
			retVal	=	true;
		}
		if(retVal==true)
			info("Expected value :" + expected + " matches with status bar message");
		else
			reportFailure("Expected value :" + expected + " does not match to status bar message");
		return retVal;
	}	
	/**
	 * verify Status bar value
	 * @param name name of the field
	 * @return true or false
	 * @throws Exception
	 */	
	
	public boolean verifyStatusBarValue(String expected) throws Exception
	{
		boolean retVal	=	false;
		String status	=	getStatusBar();
		if(status!=null && !"".equalsIgnoreCase(status))
		{
			if(status.equals(expected) || status.contains(expected))
				retVal	=	true;
		}
		else if ( status.equals(expected ) || status.contains(expected))
		{
			retVal	=	true;
		}
		if(retVal==true)
			info("Expected value :" + expected + " matches with status bar message");
		else
			reportFailure("Expected value :" + expected + " does not match to status bar message");
		return retVal;
	}
		
	/**
	 * get field value
	 * @param name name of the field
	 * @return field value, if not found, return ""
	 * @throws Exception
	 */
	public String getFieldValue(String fieldName) throws Exception {
		String[] lines = getScreen().split("\n");
		String value = "";
		
		// code to handle the multiple occurences of field name.
		
		String []fieldnames = fieldName.split(";");
		int occ = 0;
		
		if(fieldnames.length>1)
		{
			occ = Integer.parseInt(fieldnames[1]);
		}
		info("Occurrence:::>" + occ + "<:::");
		int counter = 0;
		for (String line : lines) {
			
			if (line.contains(">"))
				line = line.replace(">", ":");
			
			if (line.contains("]"))
				line = line.replace("]", ":");
			
			if (!line.contains(":"))
				continue;
						
			String[] lineValue = line.split(":");
			info("Field::::>"+lineValue[0]+"<:::");			
			if (lineValue[0].trim().equals(fieldnames[0])) {
				if(counter==occ)
				{
					value = lineValue[1].trim();
					break;
				}	
				
				counter++;
				info("Counter:::>"+counter+"<:::");
			}
				
		}
		
		return value;
	}
	/**
	 * Get the section status bar message
	 * @return status bar message
	 */
	public String getStatusBar() throws Exception {
		String[] lines = getScreen().split("\n");
		
		if (debug > 0) System.out.println("TelnetService: Status bar: " + lines[I_STATUS_LINE]);
		
		return lines[I_STATUS_LINE].trim();		
	}
	
	/**
	 * Get Full stasus message
	 * @return full status message
	 * @throws Exception
	 */
	public String getFullStatus() throws Exception {
		ctrl("B");
		
		String[] lines = getScreen().split("\n");
		
		String msg = "";
		
		for (int i = I_TITLE_LINE + 2; i < I_STATUS_LINE; i++) {
			msg = msg + lines[i].trim() + "";
			//System.out.println("Content @ "+i+":"+ msg);
		}
		//System.out.println("After For loop:"+ msg);
		buttonPress();
		//System.out.println("After button press:"+ msg);
		if (debug > 0) System.out.println("TelnetService: Full Status message: " + lines[I_STATUS_LINE]);
		//System.out.println("From getFullStatus function:" + msg.trim());
		return msg.trim();
	}
	
	/**
	 * Wait for text to be show in the screen
	 * @param text text to be waited
	 * @param timeout wait for timeout, unit second
	 * @return true if text is found, false timeout reached.
	 */
	public boolean waitForScreen(@Arg("text")String text, @Arg("timeout")int timeout) throws Exception {
		for (int i = 0; i < (timeout * 10); i++) {
			if (terminal.getScreenText().contains(text)) return true;
			
			sleep(100);
		}
		
		return false;
	}
	
	/**
	 *  Wait for status bar message
	 * @param text message to match
	 * @param timeout unit in sec
	 * @return true if text is found, false timeout reached.
	 */
	public boolean waitForStatus(@Arg("text")String text, @Arg("timeout")int timeout) throws Exception {
		for (int i = 0; i < (timeout * 10); i++) {
			String[] lines = terminal.getScreenText().split("\n");
			
			if (lines[I_STATUS_LINE].contains(text)) return true;	
			
			sleep(100);
		}
		
		return false;
	}
	
	/**
	 * Wait for title bar message
	 * @param text title to match
	 * @param timeout unit in sec
	 * @return true if text is found, false timout reached.
	 */
	public boolean waitForTitle(@Arg("text")String text, @Arg("timeout")int timeout) throws Exception {
		for (int i = 0; i < (timeout * 10); i++) {
			String[] lines = terminal.getScreenText().split("\n");
			
			if (lines[I_TITLE_LINE].contains(text)) return true;	
			
			sleep(100);
		}
		
		return false;
	}
	
	/**
	 * Switch responsibility
	 * @param resp responsibility to switch
	 */
	public void switchResp(@Arg("resp")String resp) throws Exception {
		if (this.getSectionTitle().contains(script.T_RESP)) {
			this.navigateByName(resp);
			return;
		}
		
		this.ctrl("N");
		this.navigateByName(M_CHANGE_RESP + ";" + resp);		
	}
	
	/**
	 * Change Org, if Change Org option not found in Main menu, navigate to Inquiry->Item will be used to change org
	 * @param org organization to change
	 */
	public void changeOrg(@Arg("org")String org) throws Exception {
		if (debug > 0) System.out.println("TelnetService: Change org to :" + org);
		
		this.ctrl("N");
		if (this.getScreen().contains(M_CHANGE_ORG)) {
			this.navigateByName(M_CHANGE_ORG);
			this.set(org);
		} else {
			this.navigateByName(DEFAULT_CHANGE_ORG_PATH);
			this.set(org);
			this.ctrl("N");
		}
	}
	
	/**
	 * Get current field's title
	 * @return current field's title
	 */
	public String getCurrField() throws Exception {
		//this.ctrl("A");
		// Type Ctrl+A without waiting
		terminal.sendKeyMessage(KeyEvent.CTRL_DOWN_MASK, (char)1);
		
		boolean found = waitForTitle(T_FIELD_DETAILS, 5);
		if(!found) // If not fould retype ctrl+a again to try
			ctrl("A");
		
		
		String[] lines = terminal.getScreenText().split("\n");
		//String[] lines = getScreen().split("\n");
		
		terminal.sendKeyMessage(KeyEvent.CTRL_DOWN_MASK, (char)1);
		
		String fieldDetails = lines[I_DETAILS_LINE];
		int index = fieldDetails.indexOf(":");
		if (index < 0) 
		{
			System.out.println("Current Text: " + fieldDetails.trim());
			return fieldDetails.trim();
		}
		
		if (debug > 0) System.out.println("TelnetService: curr field: " + fieldDetails.trim().substring(0, index));
		
		return fieldDetails.trim().substring(0, index);
	}
	
	/**
	 * Get Previsous line's field title, for confirm senarios
	 * @return preevisous line's field title
	 * @throws Exception
	 */
	public String getPreviousField() throws Exception {
		char uniqueChar = '*';
		String title = "";
		
		terminal.sendKeyMessage(uniqueChar);sleep(100);
		
		String[] lines = terminal.getScreenText().split("\n");
		
		for(int i = 1; i < lines.length; i++) {
			if (lines[i].contains("*")) {
				title = lines[i-1].split(":")[0].trim();
				break;
			}
		}
		
		if (debug > 0) System.out.println("TelnetSerivce: " + "Previous Field - " + title);
		
		ctrl("K");
		
		return title;
	}
	
	/**
	 * Get current field's value
	 * @return current field's value
	 */
	public String getCurrFieldValue() throws Exception {
		ctrl("A");
		waitForTitle(T_FIELD_DETAILS, 5);
		
		String[] lines = getScreen().split("\n");
		
		ctrl("A");
		
		String fieldDetails = lines[I_DETAILS_LINE];
		int index = fieldDetails.indexOf(":");
		if (index < 0) {
			if (debug > 0) System.err.println("TelnetSerivce: " + "Current Field error - " + fieldDetails);
			return "";
		}			
		
		if (debug > 0) System.out.println("TelnetSerivce: " + "Current Field - " + fieldDetails.trim().substring(index + 1));
		return fieldDetails.trim().substring(index + 1);
	}		
	
	/**
	 * Get Previsous line's field value, for confirm senarios
	 * @return preevisous line's field value
	 * @throws Exception
	 */
	public String getPreviousFieldValue() throws Exception {
		char uniqueChar = '*';
		String value = "";
		
		terminal.sendKeyMessage(uniqueChar);sleep(100);
		
		String[] lines = terminal.getScreenText().split("\n");
		
		for(int i = 1; i < lines.length; i++) {
			if (lines[i].contains("*")) {
				value = lines[i-1].split(":")[1].trim();
				break;
			}
		}
		
		if (debug > 0) System.out.println("TelnetSerivce: " + "Previous Field Value - " + value);
		
		ctrl("K");
		
		return value;
	}
	
	/**
	 * Navigate menu by given path
	 * Note: if the menu name contains no ">", the last char will be removed
	 * @param path the path to navigate, seperated by ";"
	 */
	public void navigateByName(@Arg("path")String path) throws Exception {
		String[] paths = path.split(";");
		String[] scrLines;
		boolean regularised	=	false;
		boolean hasIndex	=	false;
		int currIndex		=	0;
		int expIndex		=	0;
		
		for (int pi = 0; pi < paths.length; pi++) {
			hasIndex		=	false;
			currIndex		=	0;
			expIndex		=	0;
			if (debug > 0) System.out.println("TelnetService: selecting menu: " + paths[pi]);
			
			regularised	=	false; // reset regularised to false
			
			scrLines = getScreen().split("\n");
			for (int i = 1; i < scrLines.length; i++) {				
				String match = scrLines[i];
				int start = match.indexOf("<");
				int end = match.indexOf(">");
				if (start < 0) continue; // not listed option, "<" not found
				
				if (end > 0) // ">" found, remove it
					match = match.substring(0, end);				
								
				String expectedPath = paths[pi];
				String expArr[]	=	expectedPath.split(";");
				
				if(expArr.length==2)
				{
					hasIndex	=	true;
					expIndex	=	Integer.parseInt(expArr[1]);
				}
				
				expectedPath	=	expArr[0];
				if (expectedPath.endsWith("*")) // check if the given path has * in the ending so that we can consider that path name is regulariesed
				{
					expectedPath = expectedPath.substring(0, expectedPath.length() - 1);
					regularised = true;
				}
				if (expectedPath.length() >= 18) // the menu name is too long to show the '>' char
				{
					expectedPath = expectedPath.substring(0, expectedPath.length() - 1);
				}
												
				match = match.trim(); //remove blank chars
				if (match.length() >= 20) // the menu name is too long to show the '>' char
				{
					match = match.substring(0, match.length() - 1);
				}			
				System.out.println("Match:"+ match);
				if (expectedPath.equals(match.substring(start + 1))  || (regularised && match.contains(expectedPath)) ) 				
				{
					if(hasIndex && currIndex	!=expIndex )
					{
						currIndex++;
						continue;
					}
					//System.out.println("Nav Found" + expectedPath);
					terminal.sendKeyMessage(match.substring(0, 1).toCharArray()[0]); // Press the leading number
					
					if (debug > 0) System.out.println("TelnetService: menu selected: " + scrLines[i]);
					
					///waitForActionComplete();
					//System.out.println("1");
					boolean found = waitForTitle(paths[pi], 1);
					//System.out.println("2"+ found);
					if (!found)
					{	
						//System.out.println("3 Not Found");
						waitForActionComplete();
						//System.out.println("4 After Wait");
					}
					//System.out.println("5");
					break;
				}				
			}
					
		}
		
		//waitForActionComplete();
		//sleep(500);
	}
	
	/**
	 * Log out of the current session
	 */
	public void logout() throws Exception {				
		if (getSectionTitle().contains("Responsi")) 
			buttonPress();
		
		ctrl("N");
		ctrl("C");
		buttonPress();
		
		if (this.getSectionTitle().contains("Drop Connection")){
			terminal.sendKeyMessage('Y');
		}
		if (debug > 0) System.out.println("TelnetService: Logged out..");
	}
	
	/**
	 * Close the terminal
	 */
	public void close() throws Exception {		
		terminal.close();
		
		if (debug > 0) System.out.println("TelnetService: Closed");
		
		terminal = null;
	}
		
	/**
	 * Sleep for a time
	 * @param timeout
	 */
	private void sleep(int timeout) throws Exception {
		if (debug > 1) System.out.println("TelnetService: Sleep for " + timeout + "sec");
		
		Thread.sleep(timeout);
		
	}
	
	/**
	 * Wait for command to complete
	 */
	public void waitForActionComplete() throws Exception {			
		String status;
		boolean waitRequired;
		for (int i = 0; i < DEFAULT_TIMEOUT; i++) {
			
			// think for 1 sec for the Please wait meg to pop up
			sleep(1000);
			
			String[] lines = terminal.getScreenText().split("\n");
			
			status = lines[I_STATUS_LINE].trim();
			
			if (debug > 0) System.out.println("TelnetService: Status bar: " + status);
						
			waitRequired = false;
			for (String msg : W_WAIT_MSGS) {
				if (status.toLowerCase().contains(msg.toLowerCase())) {
					if (debug > 0) System.out.println("Waiting for command to complete: " + status);
					waitRequired = true;					
					sleep(1000);
					break;	
				}
			}
			
			if (!waitRequired) {				
				return;
			}
			
		}
		
		// Timeout reached
		System.err.println("Timeout reached waiting for command to complete.");
	}



	

	
}

//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

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
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;

public class TELNETLIB extends FuncLibraryWrapper
{

	public void initialize() throws AbstractScriptException {
		checkInit();
		callFunction("initialize");
	}

	public void run() throws AbstractScriptException {
		checkInit();
		callFunction("run");
	}

	public void finish() throws AbstractScriptException {
		checkInit();
		callFunction("finish");
	}

	/**
	 * Check if the service is initialized or not
	 * @return  true if the service is initialized
	 */
	public Boolean isTelnetServiceInitialized() throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("isTelnetServiceInitialized");
	}

	/**
	 * Initizlize the telnet service
	 */
	public void initializeTelnetService() throws AbstractScriptException {
		checkInit();
		callFunction("initializeTelnetService");
	}

	/**
	 * Login given user ,pwd
	 * @param user  user name
	 * @param pwd  password 
	 */
	public void login(@Arg("user") String user, @Arg("pwd") String pwd)
			throws AbstractScriptException {
		checkInit();
		callFunction("login", user, pwd);
	}

	/**
	 * Login given server, user ,pwd
	 * @param server  format: host:port
	 * @param user  user name
	 * @param pwd  password 
	 */
	public void login(@Arg("server") String server, @Arg("user") String user,
			@Arg("pwd") String pwd) throws AbstractScriptException {
		checkInit();
		callFunction("login", server, user, pwd);
	}

	/**
	 * Bring to front the telnet service window
	 * @throws Exception
	 */
	public void setFocus() throws AbstractScriptException {
		checkInit();
		callFunction("setFocus");
	}

	/**
	 * Type the given string message and press enter button
	 * @param msg  the message to type in the terminal
	 */
	public void set(@Arg("msg") String msg) throws AbstractScriptException {
		checkInit();
		callFunction("set", msg);
	}

	/**
	 * Type the given string message without pressing enter button
	 * @param msg  the message to type in the terminal
	 */
	public void setWithoutEnter(@Arg("msg") String msg)
			throws AbstractScriptException {
		checkInit();
		callFunction("setWithoutEnter", msg);
	}

	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commit() throws AbstractScriptException {
		checkInit();
		callFunction("commit");
	}

	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commitExpandAndVerify(String verifyText)
			throws AbstractScriptException {
		checkInit();
		callFunction("commitExpandAndVerify", verifyText);
	}

	/**
	 * Similar with buttonPress, but will wait for 5 secs before wait for action complete There are senarios that Please Wait msg is deplayed to show after button is pressed
	 * @throws Exception
	 */
	public void commitAndVerify(String verifyText)
			throws AbstractScriptException {
		checkInit();
		callFunction("commitAndVerify", verifyText);
	}

	/**
	 * Press the return button and sleep for 500 msec
	 */
	public void buttonPress() throws AbstractScriptException {
		checkInit();
		callFunction("buttonPress");
	}

	/**
	 * Press key with control down note: only support control key with 'A'-'Z' combinations
	 * @param msg  the key to press
	 */
	public void ctrl(@Arg("msg") String msg) throws AbstractScriptException {
		checkInit();
		callFunction("ctrl", msg);
	}

	/**
	 * Press key ESC
	 */
	public void esc() throws AbstractScriptException {
		checkInit();
		callFunction("esc");
	}

	/**
	 * Contrl and perss enter button
	 */
	public void ctrlAndEnter(@Arg("msg") String msg)
			throws AbstractScriptException {
		checkInit();
		callFunction("ctrlAndEnter", msg);
	}

	/**
	 * Press the down key
	 */
	public void skipDown() throws AbstractScriptException {
		checkInit();
		callFunction("skipDown");
	}

	/**
	 * Press the up key
	 */
	public void skipUp() throws AbstractScriptException {
		checkInit();
		callFunction("skipUp");
	}

	public void functionKey(String key) throws AbstractScriptException {
		checkInit();
		callFunction("functionKey", key);
	}

	public void selectOption(String option) throws AbstractScriptException {
		checkInit();
		callFunction("selectOption", option);
	}

	/**
	 * Go to the top field - Cheney, 2011-08-02
	 */
	public void gotoTopField() throws AbstractScriptException {
		checkInit();
		callFunction("gotoTopField");
	}

	/**
	 * Set the screen buffer time, unit in msec
	 * @param msec  screen buffer time, unit in msec
	 */
	public void setScreenBufferTime(@Arg("msec") int msec)
			throws AbstractScriptException {
		checkInit();
		callFunction("setScreenBufferTime", msec);
	}

	/**
	 * Get all the texts in the terminal screen
	 * @return  Screen text
	 */
	public String getScreen() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getScreen");
	}

	/**
	 * Get the section title, meaning the first line in the screen
	 * @return  the screen title
	 */
	public String getSectionTitle() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSectionTitle");
	}

	/**
	 * verify field value
	 * @param name  name of the field
	 * @return  true or false
	 * @throws Exception
	 */
	public boolean verifyFieldValue(String fieldName, String expected)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyFieldValue", fieldName, expected);
	}

	/**
	 * verify Status bar value
	 * @param name  name of the field
	 * @return  true or false
	 * @throws Exception
	 */
	public boolean expandVerifyStatusBarValue(String expected)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("expandVerifyStatusBarValue", expected);
	}

	/**
	 * verify Status bar value
	 * @param name  name of the field
	 * @return  true or false
	 * @throws Exception
	 */
	public boolean verifyStatusBarValue(String expected)
			throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyStatusBarValue", expected);
	}

	/**
	 * get field value
	 * @param name  name of the field
	 * @return  field value, if not found, return ""
	 * @throws Exception
	 */
	public String getFieldValue(String fieldName)
			throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getFieldValue", fieldName);
	}

	/**
	 * Get the section status bar message
	 * @return  status bar message
	 */
	public String getStatusBar() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getStatusBar");
	}

	/**
	 * Get Full stasus message
	 * @return  full status message
	 * @throws Exception
	 */
	public String getFullStatus() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getFullStatus");
	}

	/**
	 * Wait for text to be show in the screen
	 * @param text  text to be waited
	 * @param timeout  wait for timeout, unit second
	 * @return  true if text is found, false timeout reached.
	 */
	public boolean waitForScreen(@Arg("text") String text,
			@Arg("timeout") int timeout) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("waitForScreen", text, timeout);
	}

	/**
	 * Wait for status bar message
	 * @param text  message to match
	 * @param timeout  unit in sec
	 * @return  true if text is found, false timeout reached.
	 */
	public boolean waitForStatus(@Arg("text") String text,
			@Arg("timeout") int timeout) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("waitForStatus", text, timeout);
	}

	/**
	 * Wait for title bar message
	 * @param text  title to match
	 * @param timeout  unit in sec
	 * @return  true if text is found, false timout reached.
	 */
	public boolean waitForTitle(@Arg("text") String text,
			@Arg("timeout") int timeout) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("waitForTitle", text, timeout);
	}

	/**
	 * Switch responsibility
	 * @param resp  responsibility to switch
	 */
	public void switchResp(@Arg("resp") String resp)
			throws AbstractScriptException {
		checkInit();
		callFunction("switchResp", resp);
	}

	/**
	 * Change Org, if Change Org option not found in Main menu, navigate to Inquiry->Item will be used to change org
	 * @param org  organization to change
	 */
	public void changeOrg(@Arg("org") String org)
			throws AbstractScriptException {
		checkInit();
		callFunction("changeOrg", org);
	}

	/**
	 * Get current field's title
	 * @return  current field's title
	 */
	public String getCurrField() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrField");
	}

	/**
	 * Get Previsous line's field title, for confirm senarios
	 * @return  preevisous line's field title
	 * @throws Exception
	 */
	public String getPreviousField() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getPreviousField");
	}

	/**
	 * Get current field's value
	 * @return  current field's value
	 */
	public String getCurrFieldValue() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrFieldValue");
	}

	/**
	 * Get Previsous line's field value, for confirm senarios
	 * @return  preevisous line's field value
	 * @throws Exception
	 */
	public String getPreviousFieldValue() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getPreviousFieldValue");
	}

	/**
	 * Navigate menu by given path Note: if the menu name contains no ">", the last char will be removed
	 * @param path  the path to navigate, seperated by ";"
	 */
	public void navigateByName(@Arg("path") String path)
			throws AbstractScriptException {
		checkInit();
		callFunction("navigateByName", path);
	}

	/**
	 * Log out of the current session
	 */
	public void logout() throws AbstractScriptException {
		checkInit();
		callFunction("logout");
	}

	/**
	 * Close the terminal
	 */
	public void close() throws AbstractScriptException {
		checkInit();
		callFunction("close");
	}

	/**
	 * Wait for command to complete
	 */
	public void waitForActionComplete() throws AbstractScriptException {
		checkInit();
		callFunction("waitForActionComplete");
	}

}


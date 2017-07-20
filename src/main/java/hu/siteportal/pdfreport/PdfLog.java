package hu.siteportal.pdfreport;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An extension for TestNG's Reporter.
 * Adds more convenient logging interface.
 *
 * TODO: Add screenshots capturing.
 * TODO: Add "multy errors" support, see http://seleniumexamples.com/blog/guide/using-soft-assertions-in-testng/
 */
public class PdfLog {
    private static final Logger logger = LoggerFactory.getLogger(PdfLog.class);
	protected static List<String> actionList = new ArrayList<String>();
    protected static List<String> expResultList = new ArrayList<String>();    
    protected static List<String> actResultList = new ArrayList<String>();
    protected static List<String> statusList = new ArrayList<String>();
	
	private static String newLine = "\r\n </br>";

	public static void actionStep(String message){
		
		PdfLogList.setLogStatus(message, actionList);
	}
	
	
	public static void setStepStatus(String message){
		PdfLogList.setLogStatus(message, statusList);
	}
	
	
	public static void expectedResultStep(String message) {
		PdfLogList.setLogStatus(message, expResultList);
	}
	
	public static void actualResultStep(String message) {
		PdfLogList.setLogStatus(message, actResultList);	
	}
	
	public static List<String> getActionList(){
		return actionList;
	}
	
	public static List<String> getExpectedResultList(){
		return expResultList;
	}
	
	public static List<String> getActualResultList(){
		return actResultList;
	}
	
	public static List<String> getStatusList(){
		return statusList;
	}
	
	public static void setDriver(WebDriver driver){
		PdfLogging.setDriver(driver);
	}
	
	public static void clearActionResultStatusList() {
		getActionList().clear();
		getExpectedResultList().clear();
		getActualResultList().clear();
		getStatusList().clear();	
	}
}
package SeleniumTestPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import java.util.*;

public class Collector {
	
	public static String[] outputCode = {
		"Error: Unable to take model",
		"Successfully taken model",
		"Error: Already took model",
		"Error: Moderated content",
		"Error: Model not for sale"
	};
	
	public static HashMap<String, Integer> priceContainerCode = new HashMap<String, Integer>();
	
	public static void InitializeCollector() {
		priceContainerCode.put("This item is available in your inventory.", 2);
		priceContainerCode.put("This item has been moderated.", 3);
		priceContainerCode.put("This item is not currently for sale.", 4);
	}
	
	
	public static String[] collectItems(String currentProfile,
	String userdataPath, String driverPath, String[] URLList) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-dir=" + userdataPath);
		options.addArguments("profile-directory=" + currentProfile);
		System.setProperty("webdriver.chrome.driver", driverPath);
		
		String[] outputList = new String[URLList.length];
		
		WebDriver driver = new ChromeDriver(options);
		
        for (int i=0; i<URLList.length; i++) {
			String currentURL = URLList[i];
			int code = CheckPage(driver, currentURL);
			String output = outputCode[code] + ": (" + (i+1) + ": " + currentURL + ")";
			System.out.println(output);
			outputList[i] = output;
        }
        
        System.out.println("done.");
        driver.quit();
        
        return outputList;
	}
	
	public static int CheckPage(WebDriver driver, String currentURL) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
            driver.get(currentURL);
            
            /*
            wait = new WebDriverWait(driver, 2); // Stall Selenium for X seconds for new page to fully load
			try { wait.until(presenceOfElementLocated(By.className("does-not-exist.stalling"))); }
            catch(TimeoutException error) {}
            */
			
			wait = new WebDriverWait(driver, 2);
            try { 
            	WebElement priceContainer = wait.until(presenceOfElementLocated(By.className("item-first-line")));
	            if (priceContainer != null && priceContainerCode.get(priceContainer.getText()) != null) {
	            	return priceContainerCode.get(priceContainer.getText()).intValue();
	            };
            }
            catch(TimeoutException error) {}
            
            
			wait = new WebDriverWait(driver, 5);
            try { wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg"))); }
            catch(TimeoutException error) { return 0; }
            WebElement buyButton = wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg")));
            
            try { buyButton.click(); }
            catch(Exception error) { System.out.println(error); return 0; }
            
            try { wait.until(presenceOfElementLocated(By.id("confirm-btn"))); }
            catch(TimeoutException error) { return 0; }
            WebElement confirmButton = wait.until(presenceOfElementLocated(By.id("confirm-btn")));
            
			try { confirmButton.click(); }
        	catch(Exception error) { return 0; }
			
			try { wait.until(presenceOfElementLocated(By.className("on"))); }
            catch(TimeoutException error) { return 0; }
			WebElement confirmation = wait.until(presenceOfElementLocated(By.className("on")));
			
			wait = new WebDriverWait(driver, 2); // Stall Selenium for X seconds
			try { wait.until(presenceOfElementLocated(By.className("does-not-exist.stalling"))); }
            catch(TimeoutException error) { return 1; }
	    }
        finally {};
        return 1;
	}
}

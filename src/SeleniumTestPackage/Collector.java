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
	
	public static String[] collectItems(String[] URLList) {
		String currentProfile = "Profile 2"; // Needs to change
		String driverPath = "C:/Users/19258/Programs/chromedriver.exe"; // Needs to change
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-dir=C:/Users/19258/AppData/Local/Google/Chrome/User Data"); // Needs to change
		options.addArguments("profile-directory=" + currentProfile);
		System.setProperty("webdriver.chrome.driver", driverPath);
		
		String[] outputList = new String[URLList.length];
		
		WebDriver driver = new ChromeDriver(options);
		
        for (int i=0; i<URLList.length; i++) {
			String currentURL = URLList[i];
			boolean taken = CheckPage(driver, currentURL);
			String output = "Cannot/Already took Model: (" + (i+1) + ": " + currentURL + ")";
			if (taken)
				output = "Successfully taken Model: (" + (i+1) + ": " + currentURL + ")";
			System.out.println(output);
			outputList[i] = output;
        }
        
        System.out.println("done.");
        driver.quit();
        
        return outputList;
	}
	
	public static boolean CheckPage(WebDriver driver, String currentURL) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
            driver.get(currentURL);
            
            wait = new WebDriverWait(driver, 2); // Stall Selenium for X seconds for new page to fully load
			try { wait.until(presenceOfElementLocated(By.className("does-not-exist.stalling"))); }
            catch(TimeoutException error) {}
            
            try { wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg"))); }
            catch(TimeoutException error) { return false; }
            WebElement buyButton = wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg")));
            
            try { buyButton.click(); }
            catch(Exception error) { System.out.println(error); return false; }
            
            try { wait.until(presenceOfElementLocated(By.id("confirm-btn"))); }
            catch(TimeoutException error) { return false; }
            WebElement confirmButton = wait.until(presenceOfElementLocated(By.id("confirm-btn")));
            
			try { confirmButton.click(); }
        	catch(Exception error) { return false; }
			
			try { wait.until(presenceOfElementLocated(By.className("on"))); }
            catch(TimeoutException error) { return false; }
			WebElement confirmation = wait.until(presenceOfElementLocated(By.className("on")));
			
			wait = new WebDriverWait(driver, 2); // Stall Selenium for X seconds
			try { wait.until(presenceOfElementLocated(By.className("does-not-exist.stalling"))); }
            catch(TimeoutException error) { return true; }
	    }
        finally {};
        return true;
	}
}

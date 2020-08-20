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
import java.util.concurrent.TimeUnit;

public class MultiwindowTest {
	
	public static ArrayList<WebDriver> driverList = new ArrayList<WebDriver>();
	
	public static void main(String[] args) {
		String[] URLList = new String[] { // Needs to change
				"https://www.roblox.com/library/29126833",
				"https://www.roblox.com/library/398833052",
				"https://www.roblox.com/library/705389768",
				"https://www.roblox.com/library/1247885188",
				"https://www.roblox.com/library/405440164",
				"https://www.roblox.com/library/884418549",
				"https://www.roblox.com/library/1586363937",
				"https://www.roblox.com/library/21571150",
				"https://www.roblox.com/library/1450906699",
				"https://www.roblox.com/library/2796961522",
				"https://www.roblox.com/library/730901777",
				"https://www.roblox.com/library/918821166",

















				








				


				
		};
		
		String currentProfile = "Profile 2"; // Needs to change
		String driverPath = "C:/Users/19258/Programs/chromedriver.exe"; // Needs to change
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-dir=C:/Users/19258/AppData/Local/Google/Chrome/User Data"); // Needs to change
		options.addArguments("profile-directory=" + currentProfile);
		System.setProperty("webdriver.chrome.driver", driverPath);
		
        for (int i=0; i<URLList.length; i++) {
			String currentURL = URLList[i];
			boolean taken = CheckPage(options, currentURL);
			if (taken) {
				System.out.println("Successfully taken Model: (" + (i+1) + ": " + currentURL + ")");
			}
			else {
				System.out.println("Cannot/Already took Model: (" + (i+1) + ": " + currentURL + ")");
			}
        }
        
        System.out.println("done.");
        // driver.quit();
        
        WebDriver firstDriver = driverList.get(0);
        WebDriverWait wait = new WebDriverWait(firstDriver, 3); // Stall Selenium for X seconds
		try { wait.until(presenceOfElementLocated(By.className("does-not-exist.stalling"))); }
        catch(TimeoutException error) {}
        
        for (int i=0; i<driverList.size(); i++) {
        	CloseDriver(i, driverList.get(i));
        }
	}
	
	public static void CloseDriver(int index, WebDriver driver) {
		if (index == 0) {
			
		}
		driver.quit();
	}
	
	public static boolean CheckPage(ChromeOptions options, String currentURL) {
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		driverList.add(driver);
		
		try {
			driver.get(currentURL);
            
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
			
	    }
        finally {
			
        };
        return true;
	}
}

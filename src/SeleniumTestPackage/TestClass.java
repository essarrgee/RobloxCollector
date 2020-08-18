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

public class TestClass {
	public static void main(String[] args) {
		String[] URLList = new String[] { // Needs to change
			"https://www.roblox.com/library/5556175315/Sign",
			"https://www.roblox.com/library/543535520/HD-SkyBox-HiRes-by-Stormchaserlukas1090", 
			"https://www.roblox.com/library/5408841490/BoomBox",
		};
		
		String currentProfile = "Profile 2"; // Needs to change
		String chromeDriver = "C:/Users/19258/Programs/chromedriver.exe"; // Needs to change
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-dir=C:/Users/19258/AppData/Local/Google/Chrome/User Data"); // Needs to change
		options.addArguments("profile-directory=" + currentProfile);
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		
		WebDriver driver = new ChromeDriver(options);
        for (int i=0; i<URLList.length; i++) {
			String currentURL = URLList[i];
			boolean taken = CheckPage(driver, currentURL);
			if (taken) {
				System.out.println("Successfully taken Model: (" + currentURL + ")");
			}
			else {
				System.out.println("Cannot/Already took Model: (" + currentURL + ")");
			}
        }
        
        driver.quit();
	}
	
	public static boolean CheckPage(WebDriver driver, String currentURL) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 8);
            driver.get(currentURL);
            
            try { wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg"))); }
            catch(TimeoutException error) { return false; }
            WebElement buyButton = wait.until(presenceOfElementLocated(By.className("btn-fixed-width-lg")));
            
            try { buyButton.click(); }
            catch(Exception error) { return false; }
            
            try { wait.until(presenceOfElementLocated(By.id("confirm-btn"))); }
            catch(TimeoutException error) { return false; }
            WebElement confirmButton = wait.until(presenceOfElementLocated(By.id("confirm-btn")));
            
			try { confirmButton.click(); }
        	catch(Exception error) { return false; }
			
			try { wait.until(presenceOfElementLocated(By.className("on"))); }
            catch(TimeoutException error) { return false; }
			WebElement confirmation = wait.until(presenceOfElementLocated(By.className("on")));
	    }
        finally {};
        return true;
	}
}

package com.qa.satyam;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class TestNgToDo {

    private RemoteWebDriver driver;
    private String Status = "failed";

    @Before
    public void setup() throws MalformedURLException {
        String username = System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY");
        String hub = "hub.lambdatest.com/wd/hub";

        if (username == null || authkey == null) {
            username = "satyam0711";
            authkey = "oTwI7nduWZDjeaMqoW9K3EWZucPp1tQCYucYT2zWMTgOxABzCW";
            System.out.println("Warning: Using hardcoded credentials. Consider setting environment variables.");
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "Flipkart Test");
        caps.setCapability("name", "flipkartAutomationTest");
        caps.setCapability("plugin", "git-testng");

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + "@" + hub), caps);
    }

    @Test
    public void flipkartAutomationTest() throws Exception {
        driver.get("https://www.flipkart.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Search for iPhone 16
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("iPhone 16");
        searchBox.sendKeys(Keys.RETURN);
        Thread.sleep(5000);

        // Click on the first product
        WebElement firstProduct = driver.findElement(By.xpath(
                "/html[1]/body[1]/div[1]/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/a[1]/div[3]/div[1]/div[1]"));
        firstProduct.click();

        // Switch to new tab
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }
        Thread.sleep(3000);

        // Add to Cart with fallback locators
        if (!driver.findElements(By.cssSelector(".KRzcNw")).isEmpty()) {
            driver.findElement(By.cssSelector(".KRzcNw")).click();
        } else if (!driver.findElements(By.cssSelector("button[class='QqFHMw vslbG+ In9uk2']")).isEmpty()) {
            driver.findElement(By.cssSelector("button[class='QqFHMw vslbG+ In9uk2']")).click();
        } else {
            System.out.println("Add to Cart button not found.");
            Assert.fail("Failed to add to cart");
        }
        Thread.sleep(5000);

        // Take Screenshot
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destFile = new File("screenshot_of_cart.png");

        if (destFile.exists()) {
            destFile.delete();
        }

        Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot saved as 'screenshot_of_cart.png'");

        Status = "passed";
    }

    @After
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }
}

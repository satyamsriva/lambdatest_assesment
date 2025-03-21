
package com.qa.satyam;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class Flipkart {

    @Test
    public void FlipkartAutomationTest() {

        // Set up the chromeDriver (if not set in PATH)
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        // Create chromeOptions object (optional, can be used to add arguments to Edge)
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        // Initialize chromeDriver with options
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open the flipkart.com site
            driver.get("https://www.flipkart.com/");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Find Search Box and Search for "iPhone 16"
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("iPhone 16");
            searchBox.sendKeys(Keys.RETURN);

            // Wait for results to load
            Thread.sleep(5000);

            // Click on the first product
            WebElement firstProduct = driver.findElement(By.xpath(
                    "/html[1]/body[1]/div[1]/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/a[1]/div[3]/div[1]/div[1]"));
            firstProduct.click();

            // Switch to new tab
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }

            // Click "Add to Cart"
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

            // Wait for Cart to Load
            Thread.sleep(5000);

            // Take Screenshot of Cart
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get("screenshot_of_cart.png"));

            System.out.println("Screenshot saved as 'screenshot_of_cart.png'");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

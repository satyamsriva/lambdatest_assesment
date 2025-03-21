package com.qa.satyam;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class SeleniumEdgeExample {
    public static void main(String[] args) {
        // Set up the EdgeDriver (if not set in PATH)
        System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        // Create EdgeOptions object (optional, can be used to add arguments to Edge)
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless"); // Example: Run Edge in headless mode
        options.addArguments("--remote-allow-origins=*");

        // Initialize EdgeDriver with options
        WebDriver driver = new EdgeDriver(options);

        // Navigate to a website
        driver.get("https://www.example.com");

        // Perform actions on the page (e.g., clicking elements, scraping data)
        System.out.println("Title: " + driver.getTitle());

        // Close the browser
        driver.quit();
    }
}

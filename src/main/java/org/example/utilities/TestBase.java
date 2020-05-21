package org.example.utilities;

import org.example.pages.LandingPage;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase extends PageBase {
  private static final ConfigReader configReader = new ConfigReader();
  public static LandingPage landingPage = null;

  @BeforeMethod
  public void setUp(ITestResult testResult) {
    logger = reports.startTest(testResult.getMethod().getMethodName());
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    landingPage = new LandingPage(driver);
    driver.launchApp();
  }

  @BeforeSuite
  public void initBase() throws Exception {
    initialize();
  }

  @AfterMethod
  public void closeApp(ITestResult testResult) throws IOException {
    try {
      getScreenshot(testResult, driver);
    } catch (AssertionError e) {
      e.printStackTrace();
      System.out.println("UI VALIDATION FAILED - NO SCREENSHOT");
    }
    reports.endTest(logger);
    reports.flush();

    driver.closeApp();
  }

  @AfterSuite
  public static void afterSuite() throws Exception {
    if (driver != null) {
      driver.quit();
    }
    if (service != null) {
      service.stop();
    }
  }
}

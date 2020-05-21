package org.example.utilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PageBase {
  public static AppiumDriverLocalService service;
  public static AndroidDriver<?> driver = null;
  public static final String taskKill = "taskkill /F /IM node.exe";
  public static final String emulatorName = "Pixel3aAPI28";
  public static ExtentReports reports;
  public static ExtentTest logger;
  public static String imagePath;
  private static final String dateFormat = "yyyyMMdd";
  private static final String reportFormat = ".html";
  private static final String imageFormat = ".png";
  private static final String errorValidationMessage = "Validation Failed...";

  public void initialize() throws IOException, InterruptedException {
    startServer();

    if (service == null || !service.isRunning()) {
      throw new AppiumServerHasNotBeenStartedLocallyException(
          "An appium server node is not started!");
    }

    File appDir =
        new File(
            System.getProperty("user.dir")
                + ConfigReader.getApkFileLocation());
    DesiredCapabilities cap = new DesiredCapabilities();

    String deviceName = ConfigReader.getDeviceName();
    //        String deviceName = System.getProperty("deviceName");

    if (deviceName.contains("Emulator")) {
      startEmulator();
    }

    cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
    cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
    cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
    cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9");
    cap.setCapability(MobileCapabilityType.APP, appDir.getAbsolutePath());
    driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), cap);

    // ExtentReport Initialization
    reports =
        new ExtentReports(
            "Reports/MobileAutomationReport"
                + new SimpleDateFormat(dateFormat).format(new Date())
                + reportFormat,
            true);
  }

  public AppiumDriverLocalService startServer() throws IOException, InterruptedException {
    boolean flag = checkIfServerIsRunning(4723);

    if (flag) {
      killServer();
    }

    service = AppiumDriverLocalService.buildDefaultService();
    service.start();

    return service;
  }

  public void killServer() throws IOException, InterruptedException {
    Runtime.getRuntime().exec(taskKill);
    Thread.sleep(3000);
  }

  public static boolean checkIfServerIsRunning(int port) {
    boolean isServerRunning = false;
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.close();
    } catch (IOException e) {
      // If control comes here, then it means the port is in use
      isServerRunning = true;
    } finally {
      serverSocket = null;
    }

    return isServerRunning;
  }

  public static void startEmulator() throws IOException, InterruptedException {
    Runtime.getRuntime()
        .exec(
            System.getProperty("user.dir")
                + ConfigReader.getStartEmulatorPath());
    Thread.sleep(6000);
  }

  public static void getScreenshot(ITestResult testResult, WebDriver driver) throws IOException {
    if (testResult.getStatus() == ITestResult.FAILURE) {
      // Initialize Screenshot
      File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String destinationPath =
          System.getProperty("user.dir")
              + "\\Reports\\Screenshots\\"
              + testResult.getName()
              + new SimpleDateFormat(dateFormat).format(new Date())
              + imageFormat;
      File destination = new File(destinationPath);
      FileUtils.copyFile(src, destination);
      imagePath = logger.addScreenCapture(destinationPath);
      logger.log(LogStatus.FAIL, ">>>", imagePath);
      System.out.println(errorValidationMessage);
    }
  }
}

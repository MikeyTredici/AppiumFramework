package org.example.listeners;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.example.utilities.PageBase;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class MyListener extends TestListenerAdapter {
  public static ExtentReports reports;

  @Override
  public void onTestSuccess(ITestResult result) {
    System.out.println("on test success");
    PageBase.logger.log(LogStatus.PASS, result.getMethod().getMethodName() + "-->  Test is passed");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    System.out.println("on test failure");
    PageBase.logger.log(LogStatus.FAIL, result.getMethod().getMethodName() + " -->  Test is failed");
    PageBase.logger.log(LogStatus.FAIL, result.getThrowable());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    System.out.println("on test skipped");
    PageBase.logger.log(LogStatus.SKIP, result.getMethod().getMethodName() + "-->  Test is skipped");

  }
}

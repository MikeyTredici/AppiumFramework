package org.example.listeners;

import org.example.utilities.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
  private static final ConfigReader configReader = new ConfigReader();
  private static final String RETRY_COUNT = "0";

  private int counter = 0;
  private Integer RETRY_LIMIT = Integer.parseInt(RETRY_COUNT);

  @Override
  public boolean retry(ITestResult result) {
    if (counter < RETRY_LIMIT) {
      counter++;
      return true;
    }

    return false;
  }
}

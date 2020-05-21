package org.example.utilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.example.utilities.ExpectedCondition.ELEMENT_TO_BE_CLICKABLE;
import static org.example.utilities.ExpectedCondition.VISIBILITY_OF;

public class MobileAppUtil extends TestBase {

  // Method for tapping element
  public static void tapElement(MobileElement element) {
    TouchAction action = new TouchAction(driver);
    action.tap(new TapOptions().withElement(new ElementOption().withElement(element))).perform();
  }

  // Methods for Scrolling
  public static void scrollToText(AndroidDriver driver, String text) {
    driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"" + text + "\"));");
  }

  public static void scrollDown() {
    Dimension dimension = driver.manage().window().getSize();

    Double scrollHeightStart = dimension.getHeight() * 0.5;
    int scrollStart = scrollHeightStart.intValue();

    Double scrollHeightEnd = dimension.getHeight() * 0.2;
    int scrollEnd = scrollHeightEnd.intValue();

    new TouchAction(driver).press(PointOption.point(0, scrollStart)).waitAction(WaitOptions.waitOptions(ofSeconds(2)))
        .moveTo(PointOption.point(0, scrollEnd)).release().perform();
  }

  public static void retryWaitFor(AndroidDriver driver, MobileElement mobileElement , ExpectedCondition expectedCondition) {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    int attempts = 0;
    while (attempts < 2) {
      try {
        if (ELEMENT_TO_BE_CLICKABLE.equals(expectedCondition)) {
          wait.until(ExpectedConditions.elementToBeClickable(mobileElement));
          break;
        } else if (VISIBILITY_OF.equals(expectedCondition)) {
          wait.until(ExpectedConditions.visibilityOf(mobileElement));
          break;
        }
      } catch (StaleElementReferenceException e) {
      }
      attempts++;
    }
  }

  // Method to retry test step to avoid StaleElementReferenceException while Getting text from element
  public static String retryFindGetText(MobileElement mobileElement, String attribute) {
    boolean result = false;
    int attempts = 0;
    String x = null;
    while (attempts < 2) {
      try {
        x = mobileElement.getAttribute(attribute);
        while (x == null) {
          x = mobileElement.getAttribute(attribute);
          result = true;
          break;
        }
        result = true;
        break;
      } catch (StaleElementReferenceException e) {
      }
      attempts++;
    }
    return x;
  }

  // Method to retry test step to avoid StaleElementReferenceException while tapping on an element
  public static void retryTap(MobileElement element) {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    TouchAction action = new TouchAction(driver);
    int attempts = 0;
    while (attempts < 2) {
      try {
        action.tap(new TapOptions().withElement(new ElementOption().withElement(element))).perform();
        break;
      } catch (StaleElementReferenceException e) {
      }
      attempts++;
    }
  }

  public static void waitForElementToDisappear(AndroidDriver driver, String elementId) {
    WebElement waitElement = null;

    //checking if loading indicator was found and if so we wait for it to
    //disappear
    FluentWait<AndroidDriver> fwait = new FluentWait<AndroidDriver>(driver)
        .withTimeout(ofSeconds(8))
        .pollingEvery(ofMillis(500))
        .ignoring(NoSuchElementException.class)
        .ignoring(TimeoutException.class);

    //First checking to see if the element is found
    //we catch and throw no exception here in case it wasn't
    try {
      waitElement = fwait.until(new Function<AndroidDriver, WebElement>() {
        public WebElement apply(AndroidDriver driver) {
          return driver.findElement(By.id(elementId));
        }
      });
    } catch (Exception e) {
    }

    //Checking if element was found and if so we wait for it to
    //disappear
    if (waitElement != null) {
      WebDriverWait wait = new WebDriverWait(driver, 120);
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(elementId)));
    }
  }
}

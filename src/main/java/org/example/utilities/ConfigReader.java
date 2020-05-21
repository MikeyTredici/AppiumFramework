package org.example.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

  private static String deviceName;
  private static String apkFileLocation;
  private static String startEmulatorPath;

  public ConfigReader() {
    FileInputStream fis;
    Properties properties = new Properties();
    try {
      fis = new FileInputStream("Data\\config.properties");
      properties.load(fis);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Unable to read the config.properties file...");
      return;
    }

    deviceName = properties.getProperty("device_name");
    apkFileLocation = properties.getProperty("apk_file_location");
    startEmulatorPath = properties.getProperty("start_emulator_path");
  }

  public static String getDeviceName() {
    return deviceName;
  }

  public static String getApkFileLocation() {
    return apkFileLocation;
  }

  public static String getStartEmulatorPath() {
    return startEmulatorPath;
  }
}

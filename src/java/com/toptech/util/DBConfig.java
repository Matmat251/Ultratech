package com.toptech.util;
import java.util.Properties;
import java.io.InputStream;
public class DBConfig {
  private static Properties props = new Properties();
  static {
    try (InputStream in = DBConfig.class.getResourceAsStream("/db.properties")) {
      props.load(in);
    } catch (Exception e) { e.printStackTrace(); }
  }
  public static String get(String key) { return props.getProperty(key); }
}

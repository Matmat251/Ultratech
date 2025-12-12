package com.ultratech.util;

import java.util.Properties;
import java.io.InputStream;

public class DBConfig {
    private static Properties props = new Properties();
    
    static {
        try (InputStream in = DBConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println("No se pudo encontrar db.properties");
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
    
    public static String get(String key) { 
        return props.getProperty(key); 
    }
    
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}

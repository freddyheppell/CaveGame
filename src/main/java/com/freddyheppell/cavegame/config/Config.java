package com.freddyheppell.cavegame.config;

import com.freddyheppell.cavegame.save.SaveManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and manages the game's configuration
 */
public class Config {
    public static final String CONFIG_FILE_NAME = "CaveGame.properties";
    private static Properties properties;
    private static final Logger logger = LogManager.getLogger();


    public static void loadConfiguration() throws IOException {
        logger.info("Loading default configuration");
        InputStream inDefault = Config.class.getClassLoader().getResourceAsStream("CaveGame.default.properties");

        properties = new Properties();
        properties.load(inDefault);
        inDefault.close();

        logger.info("Default configuration loaded, containing {} properties", properties.size());

        File overrideFile = new File(SaveManager.getSavePath(), "CaveGame.properties");
        if (overrideFile.exists()) {
            logger.info("An override file exists, loading it");
            InputStream inOverride = new FileInputStream(overrideFile);
            properties.load(inOverride);
            logger.info("Loaded override file, there are now {} config options", properties.size());
        }
    }

    public static String getString(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static int getInt(String propertyName) {
        return Integer.parseInt(getString(propertyName));
    }

    public static float getFloat(String propertyName) {
        return Float.parseFloat(getString(propertyName));
    }

    public static long getLong(String propertyName) {
        return Long.getLong(getString(propertyName));
    }

    public static char getChar(String propertyName) {
        return getString(propertyName).charAt(0);
    }

    public static boolean getBoolean(String propertyName) {
        return Boolean.valueOf(getString(propertyName));
    }
}

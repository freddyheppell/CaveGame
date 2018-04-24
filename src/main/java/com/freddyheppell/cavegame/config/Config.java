package com.freddyheppell.cavegame.config;

import com.freddyheppell.cavegame.save.SaveManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

/**
 * Loads and manages the game's configuration
 */
public class Config {
    public static final String CONFIG_FILE_NAME = "CaveGame.properties";
    private static Properties properties;
    private static final Logger logger = LogManager.getLogger();


    /**
     * Load the main and override config file
     *
     * @throws IOException If the override file could not be copied correctly
     */
    public static void loadConfiguration() throws IOException {
        logger.info("Loading default configuration");
        InputStream inDefault = Config.class.getClassLoader().getResourceAsStream("CaveGame.default.properties");

        properties = new Properties();
        properties.load(inDefault);
        inDefault.close();

        logger.info("Default configuration loaded, containing {} properties", properties.size());

        File overrideFile = new File(SaveManager.getSavePath(), "CaveGame.properties");
        if (!overrideFile.exists()) {
            SaveManager.checkSaveFolder(new File(SaveManager.getSavePath()));
            if (!overrideFile.createNewFile()) {
                logger.error("Unable to create override file template");
                return;
            }

            logger.info("Copying override file template");
            InputStream inOverride = Config.class.getClassLoader().getResourceAsStream("CaveGame.properties");

            java.nio.file.Files.copy(
                    inOverride,
                    overrideFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            inOverride.close();

        } else if (overrideFile.exists()) {
            logger.info("An override file exists, loading it");
            InputStream inOverride = new FileInputStream(overrideFile);
            properties.load(inOverride);
            logger.info("Loaded override file, there are now {} config options", properties.size());
        }
    }

    /**
     * Get a string property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static String getString(String propertyName) {
        return properties.getProperty(propertyName);
    }

    /**
     * Get a string property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static int getInt(String propertyName) {
        return Integer.parseInt(getString(propertyName));
    }

    /**
     * Get a float property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static float getFloat(String propertyName) {
        return Float.parseFloat(getString(propertyName));
    }

    /**
     * Get a long property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static long getLong(String propertyName) {
        return Long.parseLong(getString(propertyName));
    }

    /**
     * Get a char property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static char getChar(String propertyName) {
        return getString(propertyName).charAt(0);
    }

    /**
     * Get a boolean property from configuration
     *
     * @param propertyName The name of the property
     * @return The value of the property
     */
    public static boolean getBoolean(String propertyName) {
        return Boolean.valueOf(getString(propertyName));
    }

    public static String getConfigurationHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] byteHash = Base64.getEncoder().encode(
                    digest.digest(properties.toString().getBytes(StandardCharsets.UTF_8))
            );

            return new String(byteHash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is required to be in all Java implementations,
            // so this is unlikely to happen
            throw new RuntimeException("Unable to get hash algorithm");
        }
    }

    public static boolean verifyHash(String otherHash) {
        return getConfigurationHash() == otherHash;
    }
}

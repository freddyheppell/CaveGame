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
    private static final String CONFIG_FILE_NAME = "CaveGame.properties";

    private static final String CONFIG_OVERRIDE_NAME = "CaveGame.default.properties";

    /**
     * The instance of Java's properties class the configuration is loaded into
     */
    private static Properties properties;

    private static final Logger logger = LogManager.getLogger();


    /**
     * Load the main and override config file
     *
     * @throws IOException If the override file could not be copied correctly
     */
    public static void loadConfiguration() throws IOException {
        logger.info("Loading default configuration");
        // Get the properties file included within the jar
        InputStream inDefault = Config.class.getClassLoader().getResourceAsStream(CONFIG_OVERRIDE_NAME);

        // Create a new instance of Properties class
        properties = new Properties();
        // Load the default properties
        properties.load(inDefault);
        inDefault.close();

        logger.info("Default configuration loaded, containing {} properties", properties.size());

        // Get the override file
        File overrideFile = new File(SaveManager.getSavePath(), CONFIG_FILE_NAME);
        if (!overrideFile.exists()) {
            // If it doesn't exist, first check that the outer folder exists
            SaveManager.checkSaveFolder(new File(SaveManager.getSavePath()));
            // Then create the file, this returns false if it fails
            if (!overrideFile.createNewFile()) {
                // It doesn't actually cause a problem if this file couldn't be created
                // so just log it and return
                logger.error("Unable to create override file template");
                return;
            }

            logger.info("Copying override file template");
            // Get the template for the override file from the jar file
            InputStream inOverride = Config.class.getClassLoader().getResourceAsStream("CaveGame.properties");

            // Copy the file to its correct location
            java.nio.file.Files.copy(
                    inOverride,
                    overrideFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            // and close it
            inOverride.close();

        } else if (overrideFile.exists()) {
            // If it does exist, load the file
            logger.info("An override file exists, loading it");
            InputStream inOverride = new FileInputStream(overrideFile);
            // This merges the properties with the defaults
            // New properties will be added, modified properties will be changed
            // Everything else will remain as default
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

    /**
     * Get the base64 encoded SHA 256 hash of the configuration
     *
     * @return a base64-encoded hash
     */
    public static String getConfigurationHash() {
        try {
            // Use the SHA-256 hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Encode the hash as base64
            byte[] byteHash = Base64.getEncoder().encode(
                    // This method takes byte[], so convert the config file into a string and then into
                    // unicode bytes
                    digest.digest(properties.toString().getBytes(StandardCharsets.UTF_8))
            );

            return new String(byteHash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is required to be in all Java implementations, so this shouldn't happen
            throw new RuntimeException("Unable to get hash algorithm");
        }
    }
}

package it.unitn.disi.wp.cup.config.loader;

import it.unitn.disi.wp.cup.config.exception.ConfigException;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Load a configuration file from 'resources' folder
 *
 * @author Carlo Corradini
 */
public final class ConfigLoader {

    public static final String ENCODING = "UTF-8";
    public static final char LIST_DELIMITER = ',';
    public static final boolean THROW_EXCEPTION_ON_MISSING = true;
    public static final boolean INCLUDES_ALLOWED = true;

    /**
     * Given a configuration File Name, load it and return properties accessible
     * with getters
     *
     * @param fileName Name of the File
     * @return PropertiesConfiguration Object
     * @throws ConfigException If cannot Load the Configuration file
     */
    public static PropertiesConfiguration get(String fileName) throws ConfigException {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(
                PropertiesConfiguration.class).configure(new Parameters().properties()
                .setFileName(fileName)
                .setEncoding(ENCODING)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(LIST_DELIMITER))
                .setThrowExceptionOnMissing(THROW_EXCEPTION_ON_MISSING)
                .setIncludesAllowed(INCLUDES_ALLOWED));
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException ex) {
            throw new ConfigException("Unable to get the Configuration file: " + ex.getMessage());
        }
    }
}

package config.loader;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Caricamento in memoria file di configurazione presente nella cartella
 * 'resources'
 *
 * @author Carlo Corradini
 */
public final class ConfigLoader {

    private static final String ENCODING = "UTF-8";
    private static final char LIST_DELIMITER = ',';
    private static final boolean THROW_EXCEPTION_ON_MISSING = true;
    private static final boolean INCLUDES_ALLOWED = true;

    /**
     * Dato il nome di un file di configurazione, lo carica e ritorna le
     * proprietà accessibili attraverso getters
     *
     * @param fileName Nome del File
     * @return Proprità
     */
    public static PropertiesConfiguration get(String fileName) {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(
                PropertiesConfiguration.class).configure(new Parameters().properties()
                .setFileName(fileName)
                .setEncoding(ENCODING)
                .setListDelimiterHandler(new DefaultListDelimiterHandler(LIST_DELIMITER))
                .setThrowExceptionOnMissing(THROW_EXCEPTION_ON_MISSING)
                .setIncludesAllowed(INCLUDES_ALLOWED));
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package config.manager;

import org.apache.commons.configuration2.PropertiesConfiguration;
import config.loader.ConfigLoader;

/**
 * Manager delle proprietà file di configurazione
 *
 * @author Carlo Corradini
 */
public abstract class ConfigManager {

    private final PropertiesConfiguration config;
    private final String category;

    /**
     * Crea un Manager dato il nome del file di configurazione e la categoria
     * padre
     *
     * @param fileName Nome del file
     * @param category Categoria padre delle proprietà, es. padre.figlio
     */
    public ConfigManager(String fileName, String category) {
        this.config = ConfigLoader.get(String.format("%s/%s", "config", fileName));
        this.category = category;
    }

    private String formatter(String key) {
        return String.format("%s.%s", category, key);
    }

    /**
     * Ritorna la stringa associata alla chiave
     *
     * @param key Chiave di configurazione
     * @return La stringa associata
     */
    protected String getString(String key) {
        return config.getString(formatter(key));
    }

    /**
     * Ritorna l'intero associato alla chiave
     *
     * @param key Chiave di configurazione
     * @return L'intero associato
     */
    protected int getInt(String key) {
        return config.getInt(formatter(key));
    }

    /**
     * Ritorna il booleano associato alla chiave
     *
     * @param key Chiave di configurazione
     * @return Il booleano associato
     */
    protected boolean getBoolean(String key) {
        return config.getBoolean(formatter(key));
    }

    /**
     * Ritorna il double associato alla chiave
     *
     * @param key Chiave di configurazione
     * @return Il double associato
     */
    protected double getDouble(String key) {
        return config.getDouble(formatter(key));
    }

}

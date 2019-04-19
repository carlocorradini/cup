package config.manager;

/**
 * Manager configurazione Database
 *
 * @author Carlo Corradini
 */
public final class DatabaseManager extends ConfigManager {

    private static final String FILE_NAME = "database.properties";
    private static final String CATEGORY = "db";

    /**
     * Crea un DatabaseManager
     */
    public DatabaseManager() {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Ritorna l'host del database
     *
     * @return Host DB
     */
    public String getHost() {
        return super.getString("host");
    }

    /**
     * Ritorna la porta del database
     *
     * @return Port DB
     */
    public int getPort() {
        return super.getInt("port");
    }

    /**
     * Ritorna l'username di accesso
     *
     * @return Username DB
     */
    public String getUsername() {
        return super.getString("username");
    }

    /**
     * Ritorna la password di accesso
     *
     * @return Password DB
     */
    public String getPassword() {
        return super.getString("password");
    }

}

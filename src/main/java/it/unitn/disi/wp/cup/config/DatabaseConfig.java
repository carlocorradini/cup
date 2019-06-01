package config;

/**
 * Database Configuration
 *
 * @author Carlo Corradini
 */
public final class DatabaseConfig extends ConfigManager {

    private static final String FILE_NAME = "database.properties";
    private static final String CATEGORY = "db";

    /**
     * Create a Database Configuration
     */
    public DatabaseConfig() {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Return the host of the Database
     *
     * @return Host DB
     */
    public String getHost() {
        return super.getString("host");
    }

    /**
     * Return the port of the Database
     *
     * @return Port DB
     */
    public int getPort() {
        return super.getInt("port");
    }

    /**
     * Return the username of the Database
     *
     * @return Username DB
     */
    public String getUsername() {
        return super.getString("username");
    }

    /**
     * Return the password of the Database
     *
     * @return Password DB
     */
    public String getPassword() {
        return super.getString("password");
    }

}

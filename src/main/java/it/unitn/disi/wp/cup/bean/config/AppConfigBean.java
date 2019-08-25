package it.unitn.disi.wp.cup.bean.config;

import it.unitn.disi.wp.cup.config.AppConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Permits the access to web pages of the App Configuration
 *
 * @author Carlo Corradini
 * @see AppConfig
 */
@Named("app")
@ApplicationScoped
public final class AppConfigBean implements Serializable {
    private static final long serialVersionUID = 7987302702812972518L;

    /**
     * @see AppConfig#getName()
     */
    public String getName() {
        return AppConfig.getName();
    }

    /**
     * @see AppConfig#getDomain()
     */
    public String getDomain() {
        return AppConfig.getDomain();
    }

    /**
     * @see AppConfig#getHostname()
     */
    public String getHostname() {
        return AppConfig.getHostname();
    }

    /**
     * @see AppConfig#getAuthorName()
     */
    public String getAuthorName() {
        return AppConfig.getAuthorName();
    }

    /**
     * @see AppConfig#getAuthorList()
     */
    public List<String> getAuthorList() {
        return AppConfig.getAuthorList();
    }

    /**
     * @see AppConfig#getColor()
     */
    public String getColor() {
        return AppConfig.getColor();
    }

    /**
     * @see AppConfig#getSinceDay()
     */
    public static int getSinceDay() {
        return AppConfig.getSinceDay();
    }

    /**
     * @see AppConfig#getSinceMonth()
     */
    public static int getSinceMonth() {
        return AppConfig.getSinceMonth();
    }

    /**
     * @see AppConfig#getSinceYear()
     */
    public int getSinceYear() {
        return AppConfig.getSinceYear();
    }

    /**
     * @see AppConfig#getInfoEmail()
     */
    public String getInfoEmail() {
        return AppConfig.getInfoEmail();
    }

    /**
     * @see AppConfig#getInfoPhone()
     */
    public String getInfoPhone() {
        return AppConfig.getInfoPhone();
    }

    /**
     * @see AppConfig#getInfoPlaceAddress()
     */
    public String getInfoPlaceAddress() {
        return AppConfig.getInfoPlaceAddress();
    }

    /**
     * @see AppConfig#getInfoPlaceNumber()
     */
    public String getInfoPlaceNumber() {
        return AppConfig.getInfoPlaceNumber();
    }

    /**
     * @see AppConfig#getInfoPlaceCap()
     */
    public String getInfoPlaceCap() {
        return AppConfig.getInfoPlaceCap();
    }

    /**
     * @see AppConfig#getInfoPlaceCity()
     */
    public String getInfoPlaceCity() {
        return AppConfig.getInfoPlaceCity();
    }

    /**
     * @see AppConfig#getConfigAvatarMaxFileSize()
     */
    public int getConfigAvatarMaxFileSize() {
        return AppConfig.getConfigAvatarMaxFileSize();
    }
}

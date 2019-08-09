package it.unitn.disi.wp.cup.bean.config;

import it.unitn.disi.wp.cup.config.AuthConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Permits the access to web pages of the Auth Configuration
 * The Configuration is filtered leaving only NON sensible information
 *
 * @author Carlo Corradini
 * @see AuthConfig
 */
@Named("auth")
@ApplicationScoped
public final class AuthConfigBean implements Serializable {
    private static final long serialVersionUID = 3487302701712952518L;

    /**
     * @see AuthConfig#getPasswordMinLength()
     */
    public int getPasswordMinLength() {
        return AuthConfig.getPasswordMinLength();
    }

    /**
     * @see AuthConfig#getPasswordMaxLength()
     */
    public int getPasswordMaxLength() {
        return AuthConfig.getPasswordMaxLength();
    }
}

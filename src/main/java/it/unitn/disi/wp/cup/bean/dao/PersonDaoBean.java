package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Person Bean
 *
 * @author Carlo Corradini
 * @see Person
 */
@ManagedBean(name = "person")
@RequestScoped
public final class PersonDaoBean implements Serializable {

    private static final long serialVersionUID = -4058980644509062209L;
    private Person authPerson = null;

    /**
     * Initialize the {@link PersonDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (context.getRequest() instanceof HttpServletRequest) {
            authPerson = AuthUtil.getAuthPerson((HttpServletRequest) context.getRequest());
        }
    }

    /**
     * Return the Authenticated person in the current session
     *
     * @return The Authenticated person in the current session, null otherwise
     */
    public Person getAuthPerson() {
        return authPerson;
    }
}

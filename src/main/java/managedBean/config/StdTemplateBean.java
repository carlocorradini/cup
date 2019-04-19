package managedBean.config;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import config.manager.StdTemplateManager;

/**
 * Permette l'accesso tramite ManagedBean al Manager di Template Standard
 *
 * @see StdTemplateManager
 * @author Carlo Corradini
 */
@ManagedBean(name = "stdTemplate")
@ApplicationScoped
public class StdTemplateBean extends StdTemplateManager implements Serializable {

    private static final long serialVersionUID = 7987304702812982518L;

}

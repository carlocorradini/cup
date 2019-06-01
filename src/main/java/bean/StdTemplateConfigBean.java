package bean;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import config.StdTemplateConfig;

/**
 * Permits the access to web pages of the Standard Template Configuration
 *
 * @see StdTemplateConfig
 * @author Carlo Corradini
 */
@ManagedBean(name = "stdTemplate")
@ApplicationScoped
public class StdTemplateConfigBean extends StdTemplateConfig implements Serializable {

    private static final long serialVersionUID = 7987304702812982518L;

}

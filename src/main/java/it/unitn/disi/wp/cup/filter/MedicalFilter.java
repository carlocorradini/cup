package it.unitn.disi.wp.cup.filter;

import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter that check if the doctor specialist is authenticated (and authorized)
 *
 * @author Carlo Corradini
 */
@WebFilter(
        urlPatterns = {"/service/restricted/medical/*"},
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
)
public final class MedicalFilter implements Filter {
    private static final String NAME = "MedicalFilter";
    private static final boolean DEBUG = true;
    private FilterConfig filterConfig = null;
    private boolean authenticated = false;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {
                log(NAME + ":Init filter");
            }
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Do Before Processing in doFilter
     *
     * @param servletRequest  The servlet request we are processing
     * @param servletResponse The servlet response we are creating
     * @throws IOException      If an input/output error occurs
     * @throws ServletException If a servlet error occurs
     */
    private void doBeforeProcessing(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse resp;
        Doctor doctor;
        DoctorSpecialist doctorSpecialist;
        if (DEBUG) {
            log(NAME + "DoBeforeProcessing");
        }

        if (servletRequest instanceof HttpServletRequest) {
            req = (HttpServletRequest) servletRequest;
            resp = (HttpServletResponse) servletResponse;

            doctor = AuthUtil.getAuthDoctor(req);
            doctorSpecialist = AuthUtil.getAuthDoctorSpecialist(req);
            if (doctor == null && doctorSpecialist == null) {
                resp.sendRedirect(resp.encodeRedirectURL(req.getServletContext().getContextPath() + "/signin/index.xhtml"));
            } else {
                authenticated = true;
            }
        }
    }

    /**
     * Do After Processing in doFilter
     *
     * @param servletRequest  The servlet request we are processing
     * @param servletResponse The servlet response we are creating
     * @throws IOException      If an input/output error occurs
     * @throws ServletException If a servlet error occurs
     */
    private void doAfterProcessing(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        if (DEBUG) {
            log(NAME + ":DoAfterProcessing");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Throwable problem = null;
        if (DEBUG) {
            log(NAME + ":doFilter()");
        }

        doBeforeProcessing(servletRequest, servletResponse);

        if (authenticated) {
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (IOException | ServletException | RuntimeException ex) {
                problem = ex;
                servletRequest.getServletContext().log("Impossible to propagate to the next filter", ex);
            }

            doAfterProcessing(servletRequest, servletResponse);

            if (problem != null) {
                if (problem instanceof ServletException) {
                    throw (ServletException) problem;
                }
                if (problem instanceof IOException) {
                    throw (IOException) problem;
                }
            }
        }
    }

    /**
     * Returns the filter configuration object for this filter
     *
     * @return the {@link FilterConfig filter configuration object}
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * Sets the filter configuration object for this filter
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Log the Filter process
     *
     * @param message The message to log
     */
    public void log(String message) {
        if (filterConfig != null) {
            filterConfig.getServletContext().log(message);
        } else {
            Logger.getLogger(message);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(NAME + "(");
        if (filterConfig == null) {
            sb.append(")");
        } else {
            sb.append(filterConfig);
            sb.append(")");
        }

        return sb.toString();
    }
}


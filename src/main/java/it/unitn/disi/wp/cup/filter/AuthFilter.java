
package it.unitn.disi.wp.cup.filter;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter that check if the person is authenticated (and authorized)
 *
 * @author Carlo Corradini
 */
@WebFilter(
        value = "/dashboard/*",
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
)
public final class AuthFilter implements Filter {

    private static final boolean DEBUG = true;
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {
                log("AuthFilter:Init filter");
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
        HttpSession session;
        HttpServletRequest req;
        HttpServletResponse resp;
        Person person = null;
        if (DEBUG) {
            log("AuthFilter:DoBeforeProcessing");
        }

        if (servletRequest instanceof HttpServletRequest) {
            req = (HttpServletRequest) servletRequest;
            resp = (HttpServletResponse) servletResponse;
            session = req.getSession(false);

            if (session != null) {
                person = (Person) session.getAttribute(AuthConfig.getSessionName());
            }
            if (person == null) {
                resp.sendRedirect(resp.encodeRedirectURL(req.getServletContext().getContextPath() + "/signin/index.xhtml"));
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
            log("AuthFilter:DoAfterProcessing");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Throwable problem = null;
        if (DEBUG) {
            log("AuthFilter:doFilter()");
        }

        doBeforeProcessing(servletRequest, servletResponse);

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

    /**
     * Returns the filter configuration object for this filter
     *
     * @return the {@link FilterConfig filter configuration object}
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
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
        StringBuilder sb = new StringBuilder("AuthFilter(");
        if (filterConfig == null) {
            sb.append(")");
        } else {
            sb.append(filterConfig);
            sb.append(")");
        }

        return sb.toString();
    }
}

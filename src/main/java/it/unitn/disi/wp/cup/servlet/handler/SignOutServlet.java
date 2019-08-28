package it.unitn.disi.wp.cup.servlet.handler;

import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for performing a Sign Out for all authenticated
 *
 * @author Carlo Corradini
 */
@WebServlet(value = "/signout.handler", name = "signOutHandler")
public final class SignOutServlet extends HttpServlet {

    private static final String AUTH_SIGN_OUT = "authSignOut";

    /**
     * Process Sign Out request.
     * If the Authenticated entity is a {@link Person} redirect to it's Sign In page.
     * If the Authenticated entity is a {@link HealthService} redirect to it's Sign In page.
     * Otherwise redirect to Home Page
     *
     * @param req  The request
     * @param resp The response
     * @throws ServletException If an input or output error is detected when the servlet handles the request
     * @throws IOException      If the request could not be handled
     */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isPerson = AuthUtil.getAuthPerson(req) != null;
        boolean isHealthService = AuthUtil.getAuthHealthService(req) != null;
        // Sign Out
        AuthUtil.signOut(req);

        if (isPerson) {
            req.setAttribute(AUTH_SIGN_OUT, true);
            req.getRequestDispatcher("/signin/index.xhtml").forward(req, resp);
        } else if (isHealthService) {
            resp.sendRedirect(req.getContextPath() + "/health_service/index.xhtml");
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.xhtml");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}

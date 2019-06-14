package it.unitn.disi.wp.cup.servlet.handler;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signout.handler")
public final class SignOutServlet extends HttpServlet {

    private static final String AUTH_SIGN_OUT = "authSignOut";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            Person person = (Person) session.getAttribute(AuthConfig.getSessionName());
            if (person != null) {
                session.removeAttribute(AuthConfig.getSessionName());
                session.invalidate();
                req.setAttribute(AUTH_SIGN_OUT, true);
            }
        }

        req.getRequestDispatcher("/signin/index.xhtml").forward(req, resp);
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

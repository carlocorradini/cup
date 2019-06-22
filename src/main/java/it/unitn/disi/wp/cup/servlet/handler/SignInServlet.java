package it.unitn.disi.wp.cup.servlet.handler;

import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/signin.handler")
public final class SignInServlet extends HttpServlet {
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_REMEMBER = "remember";
    private static final String AUTH_ERROR = "authError";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Person person;
        String email = req.getParameter(PARAM_EMAIL);
        String password = req.getParameter(PARAM_PASSWORD);
        boolean remember = req.getParameter(PARAM_REMEMBER) != null;

        person = AuthUtil.signIn(email, password, remember, req, resp);
        if (person == null) {
            req.setAttribute(AUTH_ERROR, true);
            req.getRequestDispatcher("/signin/index.xhtml").forward(req, resp);
        } else {
            resp.sendRedirect(resp.encodeRedirectURL(super.getServletContext().getContextPath() + "/dashboard/index.xhtml"));
        }
    }
}

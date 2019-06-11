package it.unitn.disi.wp.cup.servlet.handler;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.config.exception.ConfigException;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signin.handler")
public final class SignInServlet extends HttpServlet {
    private static final long serialVersionUID = 8393696396451465996L;
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String AUTH_ERROR = "authError";
    private PersonDAO personDAO;

    @Override
    public void init() throws ServletException {
        try {
            personDAO = DAOFactory.getDAOFactory(this).getDAO(PersonDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(PARAM_EMAIL);
        String password = req.getParameter(PARAM_PASSWORD);

        try {
            Person person = personDAO.getByEmailAndPassword(email, password);
            if (person == null) {
                req.setAttribute(AUTH_ERROR, true);
                req.getRequestDispatcher("/signin/index.xhtml").forward(req, resp);
            } else {
                req.getSession().setAttribute(AuthConfig.getSessionName(), person);
                resp.sendRedirect(resp.encodeRedirectURL(super.getServletContext().getContextPath() + "/dashboard/index.xhtml"));
            }
        } catch (DAOException ex) {
            req.getServletContext().log("Impossible to retrieve the Person", ex);
        } catch (ConfigException ex) {
            req.getServletContext().log("Impossible to retrieve the Auth Config", ex);
        }
    }
}

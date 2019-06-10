package it.unitn.disi.wp.cup.servlet.handler;

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
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private PersonDAO personDAO;

    @Override
    public void init() throws ServletException {
        try {
            personDAO = ((DAOFactory) super.getServletContext().getAttribute(DAOFactory.DAO_FACTORY)).getDAO(PersonDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(EMAIL);
        String password = req.getParameter(PASSWORD);

        try {
            Person person = personDAO.getByEmailAndPassword(email, password);
            if (person == null) {
                resp.sendRedirect(resp.encodeRedirectURL(super.getServletContext().getContextPath() + "/signin/index.xhtml"));
            } else {
                req.getSession().setAttribute("user", person);
                resp.sendRedirect(resp.encodeRedirectURL(super.getServletContext().getContextPath() + "/restricted/index.xhtml"));
            }
        } catch (DAOException ex) {
            req.getServletContext().log("Impossible to retrieve the Person", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

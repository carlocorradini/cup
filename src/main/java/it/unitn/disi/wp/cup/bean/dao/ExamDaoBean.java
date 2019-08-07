package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Exam;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exam Bean
 *
 * @author Carlo Corradini
 * @see Exam
 */
@Named("exam")
@RequestScoped
public final class ExamDaoBean implements Serializable {
    private static final long serialVersionUID = 2018730124403022209L;
    private static final Logger LOGGER = Logger.getLogger(ExamDaoBean.class.getName());
    private List<Exam> exams = Collections.emptyList();

    /**
     * Initialize the {@link ExamDaoBean}
     */
    @PostConstruct
    public void init() {
        ExamDAO examDAO;

        try {
            examDAO = DAOFactory.getDAOFactory().getDAO(ExamDAO.class);
            exams = examDAO.getAll();
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Exam DAO", ex);
        } catch (DAOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get the list of Exams", ex);
        }
    }

    /**
     * Return the List of available Exam for the prescription
     *
     * @return The List of Exams
     */
    public List<Exam> getExams() {
        return exams;
    }
}

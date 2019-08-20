package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Doctor Specialist Bean
 *
 * @author Carlo Corradini
 * @see DoctorSpecialist
 */
@Named("doctorSpecialist")
@RequestScoped
public final class DoctorSpecialistDaoBean implements Serializable {
    private static final long serialVersionUID = -4017230312605462207L;
    private static final Logger LOGGER = Logger.getLogger(DoctorSpecialistDaoBean.class.getName());
    private ExamDAO examDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;
    private DoctorSpecialist authDoctorSpecialist = null;

    /**
     * Initialize the {@link DoctorSpecialistDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (context.getRequest() instanceof HttpServletRequest) {
            authDoctorSpecialist = AuthUtil.getAuthDoctorSpecialist((HttpServletRequest) context.getRequest());
            if (authDoctorSpecialist != null) {
                try {
                    examDAO = DAOFactory.getDAOFactory().getDAO(ExamDAO.class);
                    prescriptionExamDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionExamDAO.class);
                } catch (DAOFactoryException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get DAO Factory", ex);
                }
            }
        }
    }

    /**
     * Return the authenticated Doctor Specialist in the current session
     *
     * @return The authenticated Doctor Specialist in the current session, null otherwise
     */
    public DoctorSpecialist getAuthDoctorSpecialist() {
        return authDoctorSpecialist;
    }

    /**
     * Return the {@link List list} of all assigned {@link PrescriptionExam Prescription Exam}
     * to the Authenticated {@link DoctorSpecialist Doctor Specialist}
     *
     * @return The {@link List list} of all assigned {@link PrescriptionExam}
     */
    public List<PrescriptionExam> getAssignedPrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authDoctorSpecialist != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllByDoctorSpecialistId(authDoctorSpecialist.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Assigned Prescription Exam for authenticated Doctor Specialist", ex);
            }
        }

        return exams;
    }

    /**
     * Return the {@link List list} of all assigned but not performed {@link PrescriptionExam Prescription Exam}
     * for the Authenticated {@link DoctorSpecialist Doctor Specialist}
     *
     * @return The {@link List list} of all assigned but never performed {@link PrescriptionExam Prescription Exam}
     */
    public List<PrescriptionExam> getAssignedToDoPrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authDoctorSpecialist != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllToDoByDoctorSpecialistId(authDoctorSpecialist.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Assigned but not performed Prescription Exam for the authenticated Doctor Specialist", ex);
            }
        }

        return exams;
    }

    /**
     * Return the {@link List} of {@link Exam exams} that the Authenticated {@link DoctorSpecialist Doctor Specialist}
     * is qualified
     *
     * @return The {@link List} of qualified {@link Exam exams} of the Authenticated {@link DoctorSpecialist}
     */
    public List<Exam> getQualifiedExams() {
        List<Exam> exams = Collections.emptyList();

        if (authDoctorSpecialist != null && examDAO != null) {
            try {
                exams = examDAO.getAllQualifiedByDoctorSpecialistId(authDoctorSpecialist.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of of qualified Exam of the authenticated Doctor Specialist", ex);
            }
        }

        return exams;
    }

    /**
     * Return the Number of {@link PrescriptionExam exams} assigned to the Authenticated {@link DoctorSpecialist doctor specialist}
     *
     * @return Number of {@link PrescriptionExam exams} assigned
     */
    public Long getAssignedPrescriptionExamCount() {
        long count = 0L;

        if (authDoctorSpecialist != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountByDoctorSpecialistId(authDoctorSpecialist.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the assigned Prescription Exam for the authenticated Doctor Specialist", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link PrescriptionExam exams} assigned but not performed to the Authenticated {@link DoctorSpecialist doctor specialist}
     *
     * @return Number of assigned but not performed {@link PrescriptionExam exams}
     */
    public Long getAssignedToDoPrescriptionExamCount() {
        long count = 0L;

        if (authDoctorSpecialist != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountToDoByDoctorSpecialistId(authDoctorSpecialist.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the assigned and not performed Prescription Exam for the authenticated Doctor Specialist", ex);
            }
        }

        return count;
    }
}

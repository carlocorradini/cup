package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.service.model.exception.ServiceModelException;
import it.unitn.disi.wp.cup.service.model.prescription.PrescriptionReportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Write {@link Report Report} Util Class
 *
 * @author Carlo Corradini
 */
public final class WriteReportUtil {

    private static final Logger LOGGER = Logger.getLogger(WriteReportUtil.class.getName());

    private static boolean configured = false;
    private static PersonDAO personDAO;
    private static HealthServiceDAO healthServiceDAO;
    private static ReportDAO reportDAO;
    private static PrescriptionExamDAO prescriptionExamDAO;
    private static ExamDAO examDAO;
    private static MedicineDAO medicineDAO;

    /**
     * Configure the class
     *
     * @param daoFactory The daoFactory to get information from
     * @throws NullPointerException If DAOFactory is null
     */
    public static void configure(final DAOFactory daoFactory) throws NullPointerException {
        if (daoFactory == null)
            throw new NullPointerException("DAOFactory cannot be null");
        try {
            personDAO = daoFactory.getDAO(PersonDAO.class);
            healthServiceDAO = daoFactory.getDAO(HealthServiceDAO.class);
            reportDAO = daoFactory.getDAO(ReportDAO.class);
            prescriptionExamDAO = daoFactory.getDAO(PrescriptionExamDAO.class);
            examDAO = daoFactory.getDAO(ExamDAO.class);
            medicineDAO = daoFactory.getDAO(MedicineDAO.class);
            configured = true;
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get from DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (!configured)
            throw new NullPointerException("Write Report Util has not been configured");
    }

    /**
     * Save the {@link Report Report} to the corresponding {@link PrescriptionExam Prescription Exam} if all is valid
     *
     * @param prescriptionReportModel The {@link PrescriptionReportModel Model} to verify and add
     * @return True if saved, false otherwise
     * @throws ServiceModelException If something is invalid or the Persistence throw an error
     */
    public static boolean write(PrescriptionReportModel prescriptionReportModel) throws ServiceModelException {
        boolean write = false;
        Report report;
        List<Exam> reportExams;
        List<Medicine> reportMedicines;
        PrescriptionExam prescription;
        Person patient;
        Person doctorSpecialist = null;
        HealthService healthService = null;
        isConfigured();

        if (prescriptionReportModel == null)
            throw new ServiceModelException("Prescription Report Model cannot be null", new NullPointerException("Prescription Report Model is null"));
        if (!prescriptionReportModel.isValid())
            throw new ServiceModelException("Prescription Report Model is invalid");
        try {
            if ((prescription = prescriptionExamDAO.getByPrimaryKey(prescriptionReportModel.getPrescriptionId())) == null)
                throw new ServiceModelException("The Prescription Exam Id is invalid");
            if (prescription.getReport() != null)
                throw new ServiceModelException("The Prescription Exam already has a Report");
            if ((patient = personDAO.getByPrimaryKey(prescription.getPersonId())) == null)
                throw new ServiceModelException("The Prescription Exam Patient is invalid");
            if (prescription.getExam().getSupported() && prescription.getHealthServiceId() == null)
                throw new ServiceModelException("The Prescription Exam is supported and does not have a Health Service");
            if (!prescription.getExam().getSupported() && prescription.getSpecialistId() == null)
                throw new ServiceModelException("The Prescription Exam is NOT supported and does not have a Doctor Specialist");
            if (prescription.getExam().getSupported() && (healthService = healthServiceDAO.getByPrimaryKey(prescription.getHealthServiceId())) == null)
                throw new ServiceModelException("The Prescription Exam is supported but no Health Service has been found");
            if (!prescription.getExam().getSupported() && (doctorSpecialist = personDAO.getByPrimaryKey(prescription.getSpecialistId())) == null)
                throw new ServiceModelException("The Prescription Exam is NOT supported but no Doctor Specialist has been found");
        } catch (DAOException ex) {
            throw new ServiceModelException("Unable to verify the Prescription Report Model", ex);
        }

        report = new Report();
        reportExams = new ArrayList<>();
        reportMedicines = new ArrayList<>();

        // Set Report Content
        report.setContent(prescriptionReportModel.getContent());

        // Find Valid Exams
        Exam exam;
        for (Long examId : prescriptionReportModel.getExams()) {
            try {
                if ((exam = examDAO.getByPrimaryKey(examId)) != null) {
                    // Exam exists
                    reportExams.add(exam);
                }
            } catch (DAOException ex) {
                throw new ServiceModelException("Error during Report Exam Verification", ex);
            }
        }
        report.setExams(reportExams);

        // Find Valid Medicines
        Medicine medicine;
        for (Long medicineId : prescriptionReportModel.getMedicines()) {
            try {
                if ((medicine = medicineDAO.getByPrimaryKey(medicineId)) != null) {
                    // Medicine Exists
                    reportMedicines.add(medicine);
                }
            } catch (DAOException ex) {
                throw new ServiceModelException("Error during Report Medicine Verification", ex);
            }
        }
        report.setMedicines(reportMedicines);

        try {
            // Add Report & set the generated ID
            report.setId(reportDAO.add(report));
            // Link the Report to the corresponding Prescription Exam
            prescription.setReport(report);
            // Set Paid
            if (!prescription.getPaid() && prescriptionReportModel.isPaid()) {
                prescription.setPaid(true);
            }
        } catch (DAOException ex) {
            throw new ServiceModelException("Unable to Save the Report and link to the Prescription Exam", ex);
        }

        // UPDATE Prescription Exam
        try {
            if (prescription.getReport() != null && prescription.getReport().getId() != null
                    && prescriptionExamDAO.update(prescription)
                    && (prescription = prescriptionExamDAO.getByPrimaryKey(prescription.getId())) != null) {
                // Send Email after updated to the current persistence state
                sendEmail(prescription, patient, healthService, doctorSpecialist);
                write = true;
            }
        } catch (DAOException ex) {
            throw new ServiceModelException("Unable to Update the Prescription Exam with the new Report", ex);
        }

        return write;
    }

    private static void sendEmail(PrescriptionExam prescription, Person patient, HealthService healthService, Person doctorSpecialist) {
        Report report = prescription.getReport();

        // Set the string based on the fact that is payed or not
        String strPagamento = "<font color=\"red\"><b>non</b> pagato</font>";
        if(prescription.getPaid()){
            strPagamento = "<font color=\"green\">pagato</font>";
        }
        // Generate the html string for the email
        String html =
                "<h1 style=\"color: #5e9ca0;\">Nuovo <span style=\"color: #2b2301;\">report</span>!</h1>" +
                "<p>" +
                    "Ciao <span style=\"color: #2b2301;\"><b>" + patient.getName() + "</b></span>!<br>" +
                    "È stato aggiunto un report riguardo il tuo esame n° " + prescription.getId() + ".<br>" +
                    "Ecco qui un breve riassunto:<br>" +
                    "<b>ID Report</b>: <span style=\"color: #2e6c80;\">" + report.getId() + "</span><br>" +
                    "<b>Data</b>: <span style=\"color: #2e6c80;\">" + report.getDateTime().toLocalDate() + "</span><br>" +
                    "<b>Ora</b>: <span style=\"color: #2e6c80;\">" + report.getDateTime().toLocalTime() + "</span><br>" +
                    "<b>Stato del pagamento</b>: <span style=\"color: #2e6c80;\">" + strPagamento + "</span><br>" +
                "</p>";

        // Add info
        // Executor
        html += "<p>" +
                "   <b>Esecutore dell'esame</b>:<br>";
        if (prescription.getExam().getSupported() && healthService != null) {
            html +=   "   <b>Tipo</b>: <span style=\"color: #2e6c80;\">servizio sanitario</span><br>"
                    + "   <b>Id</b>: <span style=\"color: #2e6c80;\">" + healthService.getId() + "</span><br>"
                    + "   <b>Nome</b>: <span style=\"color: #2e6c80;\">" + healthService.getProvince().getNameLongCapitalized() + "</span><br>";
        } else if (!prescription.getExam().getSupported() && doctorSpecialist != null) {
            html +=   "   <b>Tipo</b>: <span style=\"color: #2e6c80;\">dottore specialista</span><br>"
                    + "   <b>Id</b>: <span style=\"color: #2e6c80;\">" + doctorSpecialist.getId() + "</span><br>"
                    + "   <b>Nome</b>: <span style=\"color: #2e6c80;\">" + doctorSpecialist.getFullNameCapitalized() + "</span><br>";
        } else {
            html += "ERROR";
        }
        html += "</p>";

        // Report content
        html += "<p><b>Contenuto report</b>:" +
                    "<hr/>" +
                        report.getContent().replaceAll("(\r\n|\n)", "<br>") +
                    "<hr/>" +
                "</p>";

        // Suggested Exams
        html += "<p><b>Esami suggeriti</b>:<br>";

        if (report.getExams().isEmpty()) {
            html += "<font color=\"gainsboro\">nessun esame suggerito</font><br>";
        } else {
            html += "<span style=color: \"#2e6c80;\">";
            for (Exam e : report.getExams()) {
                html += "• " + e.getName() + "<br>";
            }
            html += "</span>";
        }
        html += "</p>";

        // Suggested Medicines
        html += "<p><b>Farmaci suggeriti</b>:<br>";

        if (report.getMedicines().isEmpty()) {
            html += "<font color=\"gainsboro\">nessun farmaco suggerito</font><br>";
        } else {
            html += "<span style=color: \"#2e6c80;\">";
            for (Medicine m : report.getMedicines()) {
                html += "• " + m.getName() + "<br>";
            }
            html += "</span>";
        }
        html += "</p>";

        EmailUtil.sendHTML(patient.getEmail(), AppConfig.getName().toUpperCase() + " nuovo report disponibile", html);
    }
}

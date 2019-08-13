package it.unitn.disi.wp.cup.util.pdf;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for generating PDF file from a {@link PrescriptionMedicine Presscription Medicine}
 *
 * @author Carlo Corradini
 */
public final class PrescriptionMedicinePDFUtil {

    private static final Logger LOGGER = Logger.getLogger(PrescriptionMedicinePDFUtil.class.getName());
    private static PersonDAO personDAO = null;
    private static DoctorDAO doctorDAO = null;

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
            doctorDAO = daoFactory.getDAO(DoctorDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get from DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (personDAO == null || doctorDAO == null)
            throw new NullPointerException("PersonDAO or DoctorDAO cannot be null");
    }

    /**
     * Generate a PDF given the {@link PrescriptionMedicine prescriptionMedicine} passed as parameter
     *
     * @param prescriptionMedicine The {@link PrescriptionMedicine prescriptionMedicine} to generate PDF from
     * @return A {@link ByteArrayOutputStream} representing the generated PDF
     * @throws NullPointerException If {@code prescriptionMedicine} is null
     */
    public static ByteArrayOutputStream generate(PrescriptionMedicine prescriptionMedicine) throws NullPointerException {
        isConfigured();
        if (prescriptionMedicine == null)
            throw new NullPointerException("PrescriptionMedicine is a mandatory fields");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Person person;
        Doctor doctor;
        Person doctorAsPerson;

        try {
            person = personDAO.getByPrimaryKey(prescriptionMedicine.getPersonId());
            doctor = doctorDAO.getByPrimaryKey(prescriptionMedicine.getDoctorId());
            doctorAsPerson = personDAO.getByPrimaryKey(prescriptionMedicine.getDoctorId());
            if (person != null
                    && doctor != null
                    && doctorAsPerson != null
                    && !person.equals(doctorAsPerson)) {
                // Person Exists
                // Doctor Exists
                // Person and Doctor are not the same
                output = generatePDF(person, doctorAsPerson, prescriptionMedicine);
            } else {
                throw new DAOException("Invalid Doctor and Person correlation");
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generate the Prescription Medicine PDF", ex);
        }

        return output;
    }

    private static ByteArrayOutputStream generatePDF(Person person, Person doctor, PrescriptionMedicine prescriptionMedicine) throws NullPointerException {
        isConfigured();
        if (person == null || doctor == null || prescriptionMedicine == null)
            throw new NullPointerException("Person, Doctor and PrescriptionMedicine are mandatory fields");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDDocument document = PDFUtil.getBaseDocument("Prescrizione N° " + prescriptionMedicine.getId(), "Prescrizione di " + person.getFiscalCode());
        PDPageContentStream contentStream;

        try {
            contentStream = new PDPageContentStream(document, document.getPage(0));
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 1000);
            contentStream.showText("Hello " + person.getFullNameCapitalized());
            contentStream.newLine();
            contentStream.showText("Devi Pagare: " + prescriptionMedicine.getTotalToPay());
            contentStream.endText();

            PDImageXObject image = PDImageXObject.createFromFileByContent(PDFUtil.getLOGO(), document);
            PDPageContentStream contents = new PDPageContentStream(document, document.getPage(0));
            contents.drawImage(image, 70, 250);

            contents.close();
            contentStream.close();

            document.save(output);
            document.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Medicine PDF", ex);
        }

        return output;
    }
}

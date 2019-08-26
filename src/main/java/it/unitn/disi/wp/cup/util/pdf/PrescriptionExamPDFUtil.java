package it.unitn.disi.wp.cup.util.pdf;

import com.alibaba.fastjson.JSON;
import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.DoctorSpecialistDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.ImageUtil;
import it.unitn.disi.wp.cup.util.QRCodeUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import javax.validation.constraints.Null;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for generating PDF file from a {@link PrescriptionExam Presscription Exam}
 *
 * @author Carlo Corradini
 */
public final class PrescriptionExamPDFUtil {

    private static final Logger LOGGER = Logger.getLogger(PrescriptionExamPDFUtil.class.getName());
    private static PersonDAO personDAO = null;
    private static DoctorDAO doctorDAO = null;
    private static DoctorSpecialistDAO doctorSpecialistDAO = null;

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
            doctorSpecialistDAO = daoFactory.getDAO(DoctorSpecialistDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get from DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (personDAO == null || doctorDAO == null || doctorSpecialistDAO == null)
            throw new NullPointerException("PersonDAO or DoctorDAO or DoctorSpecialistDAO cannot be null");
    }

    /**
     * Generate a PDF given the {@link PrescriptionExam prescriptionExam} passed as parameter
     *
     * @param prescriptionExam The {@link PrescriptionExam prescriptionExam} to generate PDF from
     * @return A {@link ByteArrayOutputStream} representing the generated PDF
     * @throws NullPointerException If {@code prescriptionMedicine} is null
     */
    public static ByteArrayOutputStream generate(PrescriptionExam prescriptionExam) throws NullPointerException {
        isConfigured();
        if (prescriptionExam == null)
            throw new NullPointerException("PrescriptionExam is a mandatory fields");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Person person;
        Doctor doctor;
        Person doctorAsPerson;
        Person doctorSpecialistAsPerson = null;

        try {
            person = personDAO.getByPrimaryKey(prescriptionExam.getPersonId());
            doctor = doctorDAO.getByPrimaryKey(prescriptionExam.getDoctorId());
            doctorAsPerson = personDAO.getByPrimaryKey(prescriptionExam.getDoctorId());
            if (prescriptionExam.getSpecialistId() != null) {
                doctorSpecialistAsPerson = personDAO.getByPrimaryKey(prescriptionExam.getSpecialistId());
            }

            if (person != null
                    && doctor != null
                    && doctorAsPerson != null
                    && !person.equals(doctorAsPerson)) {
                // Person Exists
                // Doctor Exists
                // Person and Doctor are not the same
                output = generatePDF(person, doctorAsPerson, doctorSpecialistAsPerson, prescriptionExam);
            } else {
                throw new DAOException("Invalid Doctor and Person correlation");
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generate the Prescription Exam PDF", ex);
        }

        return output;
    }

    private static ByteArrayOutputStream generatePDF(Person person, Person doctor, Person doctorSpecialist, PrescriptionExam prescriptionExam) throws NullPointerException {
        isConfigured();
        if (person == null || doctor == null || prescriptionExam == null)
            throw new NullPointerException("Person, Doctor and PrescriptionMedicine are mandatory fields");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDDocument document = PDFUtil.getBaseDocument("Prescrizione Esame N° " + prescriptionExam.getId(), "Prescrizione di " + person.getFiscalCode());
        PDPageContentStream contentStream;

        try {

            String title = "PRESCRIZIONE ESAME";
            PDFont font = PDType1Font.COURIER;
            PDFont fontBold = PDType1Font.COURIER_BOLD;
            int fontSize = 14;

            contentStream = new PDPageContentStream(document, document.getPage(0));
            PDImageXObject qrCode = JPEGFactory.createFromImage(document, QRCodeUtil.generate(JSON.toJSONString(prescriptionExam)));
            PDImageXObject logo = JPEGFactory.createFromImage(document, ImageUtil.getLOGO());
            PDImageXObject avatar = JPEGFactory.createFromImage(document,
                    ImageIO.read(new File(FilenameUtils.separatorsToUnix(ImageUtil.getImagePath() + person.getAvatar().getNameAsResource()))));
            contentStream.drawImage(avatar, 30, 670, 100, 100);
            contentStream.drawImage(logo, 480, 670, 100, 100);
            contentStream.drawImage(qrCode, 30, 550, 100, 100);
            contentStream.beginText();
            contentStream.setFont(font, 20);
            contentStream.setNonStrokingColor(new Color(231, 68, 35));
            contentStream.newLineAtOffset(220, 750);
            contentStream.showText(title);
            contentStream.endText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALY);
            String time = formatter.format(LocalTime.now());
            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.newLineAtOffset(480, 10);
            contentStream.showText("" + LocalDate.now() + "   " + time);
            contentStream.endText();


            if(doctorSpecialist.equals(null) && prescriptionExam.getReport().equals(null) && prescriptionExam.getDateTime() == null) {
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 680);
                contentStream.showText("REPORT NON ANCORA PRESENTE");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 660);
                contentStream.showText("CHIAMARE QUESTO NUMERO PER PRENOTARE UNA VISITA SPECIALISTICA : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + AppConfig.getInfoPhone());
                contentStream.endText();
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 640);
                contentStream.showText("ID ESAME: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + prescriptionExam.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 620);
                contentStream.showText("MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 600);
                contentStream.showText("ID MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.newLineAtOffset(150, 580);
                if (prescriptionExam.getPaid()) {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText("IL TICKET È STATO PAGATO");
                } else {
                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.showText("IL TICKET NON È STATO PAGATO");
                }
                contentStream.endText();
            }else if(prescriptionExam.getReport() == null){
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 680);
                contentStream.showText("ID ESAME: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + prescriptionExam.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 660);
                contentStream.showText("MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 640);
                contentStream.showText("ID MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 620);
                contentStream.showText("PAZIENTE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + person.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 600);
                contentStream.showText("ID PAZIENTE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + person.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 580);
                contentStream.showText("SPECIALISTA : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctorSpecialist.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 560);
                contentStream.showText("ID SPECIALISTA : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctorSpecialist.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 540);
                contentStream.showText("DATA PRESCRIZIONE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + prescriptionExam.getDateTime().toLocalDate());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.newLineAtOffset(150, 520);
                if (prescriptionExam.getPaid()) {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText("IL TICKET È STATO PAGATO");
                } else {
                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.showText("IL TICKET NON È STATO PAGATO");
                }
                contentStream.endText();

            }
            else{
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 680);
                contentStream.showText("ID ESAME: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + prescriptionExam.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 660);
                contentStream.showText("MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 640);
                contentStream.showText("ID MEDICO: ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctor.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 620);
                contentStream.showText("PAZIENTE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + person.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 600);
                contentStream.showText("ID PAZIENTE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + person.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 580);
                contentStream.showText("SPECIALISTA : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctorSpecialist.getFullNameCapitalized());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 560);
                contentStream.showText("ID SPECIALISTA : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + doctorSpecialist.getId());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 540);
                contentStream.showText("DATA PRESCRIZIONE : ");
                contentStream.setFont(font, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("" + prescriptionExam.getDateTime().toLocalDate());
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.newLineAtOffset(150, 520);
                if (prescriptionExam.getPaid()) {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText("IL TICKET È STATO PAGATO");
                } else {
                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.showText("IL TICKET NON È STATO PAGATO");
                }
                contentStream.endText();
            }
           
            contentStream.close();
            document.save(output);
            document.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Exam PDF", ex);
        }

        return output;
    }

    private static void riempiPdf(PDDocument document, PDPageContentStream contentStream, int x, int y, Color color, String stringa1, String stringa2, PDFont font, PDFont fontBold,int fontSize){
        try{
            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.setNonStrokingColor(color);
            contentStream.newLineAtOffset(x,y);
            contentStream.showText(stringa1);
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(color);
            contentStream.showText(stringa2);
            contentStream.endText();
        } catch (IOException ex) {
        LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Exam PDF", ex);
        }
    }

    private static void riempiPdf2(PDDocument document, PDPageContentStream contentStream, int x, int y, Color color, String stringa1, PrescriptionExam data, PDFont font, PDFont fontBold,int fontSize){

    }
}



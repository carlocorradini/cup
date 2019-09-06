package it.unitn.disi.wp.cup.util.pdf;

import com.alibaba.fastjson.JSON;
import it.unitn.disi.wp.cup.config.AppConfig;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.DoctorSpecialistDAO;
import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.EntitySanitizerUtil;
import it.unitn.disi.wp.cup.util.ImageUtil;
import it.unitn.disi.wp.cup.util.QRCodeUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private static HealthServiceDAO healthServiceDAO = null;

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
            healthServiceDAO = daoFactory.getDAO(HealthServiceDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get from DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (personDAO == null || doctorDAO == null || doctorSpecialistDAO == null || healthServiceDAO == null)
            throw new NullPointerException("Prescription Exam PDF Util has not been configured");
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
        HealthService healthService = null;

        try {
            person = personDAO.getByPrimaryKey(prescriptionExam.getPersonId());
            doctor = doctorDAO.getByPrimaryKey(prescriptionExam.getDoctorId());
            doctorAsPerson = personDAO.getByPrimaryKey(prescriptionExam.getDoctorId());
            if (prescriptionExam.getSpecialistId() != null) {
                doctorSpecialistAsPerson = personDAO.getByPrimaryKey(prescriptionExam.getSpecialistId());
            }
            if (prescriptionExam.getHealthServiceId() != null) {
                healthService = healthServiceDAO.getByPrimaryKey(prescriptionExam.getHealthServiceId());
            }

            if (person != null
                    && doctor != null
                    && doctorAsPerson != null
                    && !person.equals(doctorAsPerson)) {
                // Person Exists
                // Doctor Exists
                // Person and Doctor are not the same
                output = generatePDF(person, doctorAsPerson, doctorSpecialistAsPerson, healthService, prescriptionExam);
            } else {
                throw new DAOException("Invalid Doctor and Person correlation");
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generate the Prescription Exam PDF", ex);
        }

        return output;
    }

    private static ByteArrayOutputStream generatePDF(Person person, Person doctor, Person doctorSpecialist, HealthService healthService, PrescriptionExam prescriptionExam) throws NullPointerException {
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
            int fontSize = 12;

            File inavatar = new File(FilenameUtils.separatorsToUnix(ImageUtil.getImagePath() + person.getAvatar().getNameAsImage()));
            BufferedImage imageava = ImageIO.read(inavatar);
            float x = imageava.getWidth();
            float y = imageava.getHeight();

            contentStream = new PDPageContentStream(document, document.getPage(0));
            PDImageXObject qrCode = JPEGFactory.createFromImage(document,
                    QRCodeUtil.generate(JSON.toJSONString(EntitySanitizerUtil.sanitizePrescriptionExam((PrescriptionExam) prescriptionExam.clone()))));
            PDImageXObject logo = JPEGFactory.createFromImage(document, ImageUtil.getLOGO());
            PDImageXObject avatar = JPEGFactory.createFromImage(document, imageava);


            contentStream.drawImage(avatar,6,510,110,y/x * 108);
            contentStream.drawImage(logo, 495, 685, 100, 100);
            contentStream.drawImage(qrCode, 6, 676, 110, 110);

            contentStream.beginText();
            contentStream.setFont(font, 20);
            contentStream.setNonStrokingColor(new Color(231, 68, 35));
            contentStream.newLineAtOffset(220, 750);
            contentStream.showText(title);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(fontBold, 10);
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.newLineAtOffset(30, 10);
            contentStream.showText("© 2019,2019 CUP");
            contentStream.endText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALY);
            String time = formatter.format(LocalTime.now());
            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.newLineAtOffset(460, 10);
            contentStream.showText("" + LocalDate.now() + " " + time);
            contentStream.endText();


            if (prescriptionExam.getReport() == null
                    && prescriptionExam.getDateTime() == null
                    && doctorSpecialist == null
                    && healthService == null) { // PRIMO CASO : REPORT NON PRESENTE, DATA NON PRESENTE, SPECIALIST AND HEALTH SERVICE NON PRESENTI

                riempiPdf(document, contentStream, 150, 680, Color.BLACK, "ID PRESCRIZIONE: ", "" + prescriptionExam.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 660, Color.BLACK, "ID MEDICO: ", "" + doctor.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 640, Color.BLACK, "MEDICO: ", "" + doctor.getFullNameCapitalized(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 620, Color.BLACK, "ID PAZIENTE: ", "" + person.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 600, Color.BLACK, "PAZIENTE: ", "" + person.getFullNameCapitalized(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 580, Color.BLACK, "DATA PRESCRIZIONE: ", "" + prescriptionExam.getDateTimeRegistration().toLocalDate(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 560, Color.BLACK, "ID ESAME: ", "" + prescriptionExam.getExam().getId(), font, fontBold, fontSize);

                float cont = 540;
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, cont);
                contentStream.showText("ESAME: ");
                contentStream.endText();
                cont = longString(prescriptionExam.getExam().getName(),document,contentStream,200,cont,9,font,320,15);

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.newLineAtOffset(150, cont);
                if (prescriptionExam.getPaid()) {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText("TICKET: ");
                    contentStream.setNonStrokingColor(Color.GREEN);
                    contentStream.showText("PAGATO");
                } else {
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.showText("TICKET: ");
                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.showText("NON PAGATO");
                }
                contentStream.endText();
                cont -= 40;

                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.newLineAtOffset(50, cont);
                contentStream.showText("ATTENZIONE IL REPORT NON È ANCORA PRESENTE");
                contentStream.endText();
                cont -= 20;
                riempiPdf(document, contentStream, 50, ((int) cont), Color.BLACK, "PER PRENOTARE SI PREGA DI CHIAMARE IL SEGUENTE N° : ", "" + AppConfig.getInfoPhone(), font, fontBold, fontSize);

            } else if (prescriptionExam.getDateTime() != null && prescriptionExam.getReport() == null) { // SECONDO CASO: REPORT NON PRESENTE; DATA PRESENTE; SPECIALIST OR HEALTH SERVICE PRESENTI
                if (doctorSpecialist != null && healthService == null) {

                    riempiPdf(document, contentStream, 150, 680, Color.BLACK, "ID PRESCRIZIONE: ", "" + prescriptionExam.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 660, Color.BLACK, "ID MEDICO: ", "" + doctor.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 640, Color.BLACK, "MEDICO: ", "" + doctor.getFullNameCapitalized(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 620, Color.BLACK, "ID PAZIENTE: ", "" + person.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 600, Color.BLACK, "PAZIENTE: ", "" + person.getFullNameCapitalized(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 580, Color.BLACK, "DATA PRESCRIZIONE: ", "" + prescriptionExam.getDateTime().toLocalDate(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 560, Color.BLACK, "ID ESAME: ", "" + prescriptionExam.getExam().getId(), font, fontBold, fontSize);

                    float cont = 540;
                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(150, cont);
                    contentStream.showText("ESAME: ");
                    contentStream.endText();
                    cont = longString(prescriptionExam.getExam().getName(),document,contentStream,200,cont,9,font,320,15);

                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.newLineAtOffset(150, cont);
                    if (prescriptionExam.getPaid()) {
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.showText("TICKET: ");
                        contentStream.setNonStrokingColor(Color.GREEN);
                        contentStream.showText("PAGATO");
                    } else {
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.showText("TICKET: ");
                        contentStream.setNonStrokingColor(Color.RED);
                        contentStream.showText("NON PAGATO");
                    }
                    contentStream.endText();
                    cont -= 40;

                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(50, cont);
                    contentStream.showText("È STATO SCIELTO UNO SPECIALISTA PER LA VISITA");
                    contentStream.endText();
                    cont -= 20;
                    riempiPdf(document, contentStream, 50, ((int) cont), Color.BLACK, "ID SPECIALISTA: ", "" + doctorSpecialist.getId(), font, fontBold, fontSize);
                    cont -= 20;
                    riempiPdf(document, contentStream, 50, ((int) cont), Color.BLACK, "SPECIALISTA: ", "" + doctorSpecialist.getFullNameCapitalized(), font, fontBold, fontSize);

                } else if (healthService != null && doctorSpecialist == null) {

                    riempiPdf(document, contentStream, 150, 680, Color.BLACK, "ID PRESCRIZIONE: ", "" + prescriptionExam.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 660, Color.BLACK, "ID MEDICO: ", "" + doctor.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 640, Color.BLACK, "MEDICO: ", "" + doctor.getFullNameCapitalized(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 620, Color.BLACK, "ID PAZIENTE: ", "" + person.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 600, Color.BLACK, "PAZIENTE: ", "" + person.getFullNameCapitalized(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 580, Color.BLACK, "DATA PRESCRIZIONE: ", "" + prescriptionExam.getDateTime().toLocalDate(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 560, Color.BLACK, "ID ESAME: ", "" + prescriptionExam.getExam().getId(), font, fontBold, fontSize);

                    float cont = 540;
                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(150, cont);
                    contentStream.showText("ESAME: ");
                    contentStream.endText();
                    cont = longString(prescriptionExam.getExam().getName(),document,contentStream,200,cont,9,font,320,15);

                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.newLineAtOffset(150, cont);
                    if (prescriptionExam.getPaid()) {
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.showText("TICKET: ");
                        contentStream.setNonStrokingColor(Color.GREEN);
                        contentStream.showText("PAGATO");
                    } else {
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.showText("TICKET: ");
                        contentStream.setNonStrokingColor(Color.RED);
                        contentStream.showText("NON PAGATO");
                    }
                    contentStream.endText();

                    cont -= 40;

                    contentStream.beginText();
                    contentStream.setFont(fontBold, fontSize);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(50, cont);
                    contentStream.showText("RECARSI AL SERVIZIO SANITARIO PER LA VISITA");
                    contentStream.endText();
                    cont -= 20;
                    riempiPdf(document, contentStream, 50, ((int) cont), Color.BLACK, "SERVIZIO SANITARIO: ", "" + healthService.getProvince().getRegion().getNameCapitalized(), font, fontBold, fontSize);

                } else {
                    // ERRORE
                    LOGGER.log(Level.SEVERE, "unable to create PDF");
                }

            } else if (prescriptionExam.getReport() != null) { // 3 CASO REPORT PRESENT; SPECIALIST OR HEALTH SERVICE PRESENTI
                riempiPdf(document, contentStream, 150, 680, Color.BLACK, "ID REPORT: ", "" + prescriptionExam.getReport().getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 660, Color.BLACK, "ID PRESCRIZIONE: ", "" + prescriptionExam.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 640, Color.BLACK, "ID MEDICO: ", "" + doctor.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 620, Color.BLACK, "MEDICO: ", "" + doctor.getFullNameCapitalized(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 600, Color.BLACK, "ID PAZIENTE: ", "" + person.getId(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 580, Color.BLACK, "PAZIENTE: ", "" + person.getFullNameCapitalized(), font, fontBold, fontSize);
                if (doctorSpecialist != null) {
                    riempiPdf(document, contentStream, 150, 560, Color.BLACK, "ID SPECIALISTA: ", "" + doctorSpecialist.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 540, Color.BLACK, "SPECIALISTA: ", "" + doctorSpecialist.getFullNameCapitalized(), font, fontBold, fontSize);
                } else if (healthService != null) {
                    riempiPdf(document, contentStream, 150, 560, Color.BLACK, "ID SERVIZIO SANITARIO: ", "" + healthService.getId(), font, fontBold, fontSize);
                    riempiPdf(document, contentStream, 150, 540, Color.BLACK, "SERVIZIO SANITARIO: ", "" + healthService.getProvince().getRegion().getNameCapitalized(), font, fontBold, fontSize);
                }
                riempiPdf(document, contentStream, 150, 520, Color.BLACK, "DATA PRESCRIZIONE: ", "" + prescriptionExam.getDateTime().toLocalDate(), font, fontBold, fontSize);
                riempiPdf(document, contentStream, 150, 500, Color.BLACK, "ID ESAME: ", "" + prescriptionExam.getExam().getId(), font, fontBold, fontSize);
                float cont;
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(150, 480);
                contentStream.showText("ESAME: ");
                contentStream.endText();
                cont = longString(prescriptionExam.getExam().getName(),document,contentStream,200,480,9,font,320,15);
                cont -= 40;
                contentStream.beginText();
                contentStream.setFont(fontBold, fontSize);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(50, cont);
                contentStream.showText("CONTENUTO REPORT: ");
                contentStream.endText();
                cont -= 20;

                cont = longString(prescriptionExam.getReport().getContent(),document,contentStream,50,cont,fontSize,font,470,20);
                cont -= 10;
                if (!prescriptionExam.getReport().getExams().isEmpty()) {
                    contentStream.beginText();
                    contentStream.setFont(fontBold, 11);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(50, cont);
                    contentStream.showText("ESAMI CONSIGLIATI: ");
                    contentStream.endText();
                    cont -= 20;
                    for (Exam e : prescriptionExam.getReport().getExams()) {
                        contentStream.beginText();
                        contentStream.setFont(font, 11);
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.newLineAtOffset(50, cont);
                        contentStream.showText("• ");
                        contentStream.endText();
                        cont = longString(e.getName(),document,contentStream,60,cont,11,font,465,15);
                        cont -= 5;
                    }
                }
                if (!prescriptionExam.getReport().getMedicines().isEmpty()) {
                    contentStream.beginText();
                    contentStream.setFont(fontBold, 11);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.newLineAtOffset(50, cont);
                    contentStream.showText("MEDICINE CONSIGLIATE: ");
                    contentStream.endText();
                    cont -= 20;
                    for (Medicine e : prescriptionExam.getReport().getMedicines()) {
                        contentStream.beginText();
                        contentStream.setFont(font, 9);
                        contentStream.setNonStrokingColor(Color.BLACK);
                        contentStream.newLineAtOffset(50, cont);
                        contentStream.showText("• ");
                        contentStream.endText();;
                        cont = longString(e.getName(),document,contentStream,60,cont,11,font,465,15);
                        cont -= 5;
                    }
                }
            } else {
                // ERRORE
                LOGGER.log(Level.SEVERE, "unable to create PDF report");
            }

            contentStream.close();
            document.save(output);
            document.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Exam PDF", ex);
        }

        return output;
    }

    private static void riempiPdf(PDDocument document, PDPageContentStream contentStream, int x, int y, Color color, String stringa1, String stringa2, PDFont font, PDFont fontBold, int fontSize) {
        try {
            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.setNonStrokingColor(color);
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(stringa1);
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(color);
            contentStream.showText(stringa2);
            contentStream.endText();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Exam PDF", ex);
        }
    }

    private static float longString(String text, PDDocument document, PDPageContentStream contentStream, float startX, float startY, int fontSize, PDFont pdfFont,int width,int linespacing){
        try {

            List<String> lines = new ArrayList<String>();

            String myLine = "";
            String[] words = text.split(" ");
            for (String word : words) {

                if (!myLine.isEmpty()) {
                    myLine += " ";
                }

                // test the width of the current line + the current word
                int size = (int) (fontSize * pdfFont.getStringWidth(myLine + word) / 1000);
                if (size > width) {
                    // if the line would be too long with the current word, add the line without the current word
                    lines.add(myLine);

                    // and start a new line with the current word
                    myLine = word;
                } else {
                    // if the current line + the current word would fit, add the current word to the line
                    myLine += word;
                }
            }
            // add the rest to lines
            lines.add(myLine);

            for (String line : lines) {
                contentStream.beginText();
                contentStream.setFont(pdfFont, fontSize);
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText(line);
                contentStream.endText();

                startY -= linespacing;
            }

        }
            catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Exam PDF", ex);
        }
        return startY;
    }
}



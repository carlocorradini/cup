package it.unitn.disi.wp.cup.util.pdf;

import com.alibaba.fastjson.JSON;
import com.google.zxing.qrcode.encoder.QRCode;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
import it.unitn.disi.wp.cup.util.EntitySanitizerUtil;
import it.unitn.disi.wp.cup.util.ImageUtil;
import it.unitn.disi.wp.cup.util.QRCodeUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
            throw new NullPointerException("Prescription Medicine has not been configured");
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
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream;

        try {
            String title = "Ricetta Farmaceutica";
            PDFont font = PDType1Font.COURIER;
            PDFont fontBold = PDType1Font.COURIER_BOLD;
            int fontSize = 12;

            File inavatar = new File(FilenameUtils.separatorsToUnix(ImageUtil.getImagePath() + person.getAvatar().getNameAsImage()));
            BufferedImage imageava = ImageIO.read(inavatar);
            float x = imageava.getWidth();
            float y = imageava.getHeight();

            contentStream = new PDPageContentStream(document, document.getPage(0));
            PDImageXObject qrCode = JPEGFactory.createFromImage(document,
                    QRCodeUtil.generate(JSON.toJSONString(EntitySanitizerUtil.sanitizePrescriptionMedicine((PrescriptionMedicine) prescriptionMedicine.clone()))));
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
            contentStream.showText("© 2019, CUP");
            contentStream.endText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALY);
            String time = formatter.format(LocalTime.now());
            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.setNonStrokingColor(Color.GRAY);
            contentStream.newLineAtOffset(460, 10);
            contentStream.showText("" + LocalDate.now() + " " + time);
            contentStream.endText();

            riempiPdf(document, contentStream, 150, 680, Color.BLACK, "MEDICO: ", "" + doctor.getFullNameCapitalized(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 660, Color.BLACK, "IDENTIFICATIVO MEDICO: ", "" + doctor.getId(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 640, Color.BLACK, "PAZIENTE: ", "" + person.getFullNameCapitalized(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 620, Color.BLACK, "CODICE FISCALE PAZIENTE: ", "" + person.getFiscalCode(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 600, Color.BLACK, "DATA PRESCRIZIONE: ", "" + prescriptionMedicine.getDateTime().toLocalDate(), font, fontBold, fontSize);
            String time2 = formatter.format(prescriptionMedicine.getDateTime().toLocalTime());
            riempiPdf(document, contentStream, 150, 580, Color.BLACK, "ORA PRESCRIZIONE: ", "" + time2, font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 560, Color.BLACK, "IDENTIFICATIVO PRESCRIZIONE: ", "" + prescriptionMedicine.getId(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 540, Color.BLACK, "FARMACO: ", "" + prescriptionMedicine.getMedicine().getName(), font, fontBold, fontSize);
            riempiPdf(document, contentStream, 150, 520, Color.BLACK, "QUANTITÀ FARMACO: ", "" + prescriptionMedicine.getQuantity(), font, fontBold, fontSize);

            contentStream.beginText();
            contentStream.setFont(fontBold, fontSize);
            contentStream.newLineAtOffset(150, 500);
            if (prescriptionMedicine.getPaid()) {
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("PAGAMENTO ");
                contentStream.setNonStrokingColor(Color.GREEN);
                contentStream.showText("CONFERMATO");
            } else {
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText("PAGAMENTO ");
                contentStream.setNonStrokingColor(Color.RED);
                contentStream.showText("NON CONFERMATO");
            }
            contentStream.endText();

            contentStream.close();
            document.save(output);
            document.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to generatePDF a Prescription Medicine PDF", ex);
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

    public static BufferedImage resizeImage(BufferedImage img, Integer Width, Integer Height) {
        int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
        if (Width == 0) {
            Width = img.getWidth();
        }
        if (Height == 0) {
            Height = img.getHeight();
        }
        int fHeight = Height;
        int fWidth = Width;
        //Work out the resized width/height
        if (img.getHeight() > Height || img.getWidth() > Width) {
            fHeight = Height;
            int wid = Width;
            float sum = (float) img.getWidth() / (float) img.getHeight();
            fWidth = Math.round(fHeight * sum);
            if (fWidth > wid) {
                //rezise again for the width this time
                fHeight = Math.round(wid / sum);
                fWidth = wid;
            }
        }
        BufferedImage resizedImage = new BufferedImage(fWidth, fHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(img, 0, 0, fWidth, fHeight, null);
        g.dispose();
        return resizedImage;
    }

}

package it.unitn.disi.wp.cup.util.pdf;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

public final class ExcelUtil {
    /**
     * Generate a XLS given a date passed as parameter
     */
    public static ByteArrayOutputStream generate(LocalDateTime localDateTime) throws NullPointerException, IOException {
        if (localDateTime == null)
            throw new NullPointerException("PrescriptionExam is a mandatory fields");

        /* --- CREATING '.XLS' FILE --------------------------------------------------------------------------------- */
        // Declarations
        String[] columns = {"Data e Ora", "Farmaco", "Medico di base", "Paziente", "Ticket"};
        List<PrescriptionMedicine> prescriptionMedicines = null; // all the prescriptions of a given Provnice

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Create a Workbook
        Workbook workbook = new HSSFWorkbook(); // XSSFWorkbook() for generating '.xlsx' file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Report");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a cellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create a CellStyle for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy hh:mm"));

        // Create other rows and cells with prescriptions data
        int rowNum = 1;
        for (PrescriptionMedicine prescriptionMedicine: prescriptionMedicines) {
            Row row = sheet.createRow(rowNum++);

            // Format the date for the user output
            String formattedDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
            formattedDate = prescriptionMedicine.getDateTime().format(formatter);

            Cell date = row.createCell(0);
            date.setCellValue(formattedDate);
            date.setCellStyle(dateCellStyle);

            row.createCell(1).setCellValue(prescriptionMedicine.getMedicine().getName());

            row.createCell(2).setCellValue(prescriptionMedicine.getDoctorId());

            row.createCell(3).setCellValue(prescriptionMedicine.getPersonId());

            row.createCell(4).setCellValue(prescriptionMedicine.getTotalToPay()); // Intanto mettiamo il prezzo del farmaco, giusto per vedere se viene giusto
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++){
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        workbook.write(output);

        // Closing the workbook
        workbook.close();

        /* --- ENDING THE '.XLS' FILE ------------------------------------------------------------------------------- */
        // inizio delle cose che non capisco e che non so se devo lasciare o meno :(
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
            LOGGER.log(Level.SEVERE, "Unable to generate the Prescription Medicine XLS report", ex);
        }

        return output;
    }
}

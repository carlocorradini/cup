package it.unitn.disi.wp.cup.util.xls;

import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrescriptionExamXLSUtil {
    /**
     * Generate a XLS given a date passed as parameter
     *
     * @param prescriptionExams The list of prescripted exams
     */
    public static ByteArrayOutputStream generate(List<PrescriptionExam> prescriptionExams) throws NullPointerException, IOException {
        if (prescriptionExams == null)
            throw new NullPointerException("prescriptionExams is a mandatory fields");

        /* --- CREATING '.XLS' FILE --------------------------------------------------------------------------------- */
        // Declarations
        String[] columns = {"ID", "Data Prescrizione", "Data Erogazione", "Esame", "Medico di base", "Paziente", "Ticket"};
        String[] totTable = {"TOTALE Costi"};
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int costAlphabet = 0;

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

        int rowNum = 1;
        if(!prescriptionExams.isEmpty()) {
            // Not empty
            // Create other rows and cells with prescriptions data
            for (PrescriptionExam prescriptionExam : prescriptionExams) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                // ID
                row.createCell(col++).setCellValue(prescriptionExam.getExam().getId());

                // Format the date for the user output
                String formattedPrescriptionDate, formattedErogationDate;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
                formattedPrescriptionDate = prescriptionExam.getDateTimeRegistration().format(formatter);
                formattedErogationDate = prescriptionExam.getDateTime().format(formatter);

                // Service prescription date
                Cell prescriptionDate = row.createCell(col++);
                prescriptionDate.setCellValue(formattedPrescriptionDate);
                prescriptionDate.setCellStyle(dateCellStyle);

                // Service erogation date
                Cell erogationDate = row.createCell(col++);
                erogationDate.setCellValue(formattedErogationDate);
                erogationDate.setCellStyle(dateCellStyle);

                // Exam name
                row.createCell(col++).setCellValue(prescriptionExam.getExam().getName());

                // Doctor ID
                row.createCell(col++).setCellValue(prescriptionExam.getDoctorId());

                // Patient ID
                row.createCell(col++).setCellValue(prescriptionExam.getPersonId());

                // Price based on the dealer of the service
                costAlphabet = col;
                if(prescriptionExam.getHealthServiceId() != null) {
                    row.createCell(col++).setCellValue(Double.parseDouble("11"));
                }
                else if(prescriptionExam.getSpecialistId() != null) {
                    row.createCell(col++).setCellValue(Double.parseDouble("50"));
                }
                else if(prescriptionExam.getHealthServiceId() != null
                        && prescriptionExam.getSpecialistId() != null) {
                    row.createCell(col++).setCellValue("non ancora assegnato");
                }

                // create cells for the costs table
                if(rowNum-1 == 1 || rowNum-1 == 2){
                    col += 2;

                    row.createCell(col);
                }
            }
        }
        else{
            // ERROR
            Row row = sheet.createRow(1);

            row.createCell(1).setCellValue("Nessun esame presente");
        }

        // Build the resume of total costs table
        if(!prescriptionExams.isEmpty()){
            // Build the Total table. It contains the sum of all quantities and costs
            // NB: It works with maximum range of the basic alphabet columns
            int i = 0;
            int spacing = 1;

            // Header
            Cell cell = headerRow.createCell(columns.length + spacing + i);
            cell.setCellValue(totTable[i]);
            cell.setCellStyle(headerCellStyle);

            // Formula of SUM
            // Tot Costs
            cell = sheet.getRow(1).getCell(columns.length + spacing + i);
            String formula = "SUM("+ alphabet[costAlphabet] + "2:" + alphabet[costAlphabet] + rowNum + ")";
            cell.setCellFormula(formula);
            sheet.autoSizeColumn(columns.length + spacing + i);
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

        return output;
    }
}

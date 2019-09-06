package it.unitn.disi.wp.cup.util.xls;

import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrescriptionMedicineXLSUtil {
    /**
     * Generate a XLS given a date passed as parameter
     *
     * @param prescriptionMedicines The list of prescripted medicines
     */
    public static ByteArrayOutputStream generate(List<PrescriptionMedicine> prescriptionMedicines) throws NullPointerException, IOException {
        if (prescriptionMedicines == null)
            throw new NullPointerException("prescriptionMedicines is a mandatory fields");

        /* --- CREATING '.XLS' FILE --------------------------------------------------------------------------------- */
        // Declarations
        String[] columns = {"ID", "Data e Ora", "Farmaco", "Medico di base", "Paziente", "Costo/pz", "Quantità", "Costo Totale"};
        String[] totTable = {"TOTALE pezzi", "TOTALE Costi"};
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int quantityCol = 6;
        int costCol = 7;

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

        // Create cells title
        for(int i = 0; i < columns.length; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create a CellStyle for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy hh:mm"));

        // Create a CellStyle for formatting double
        CellStyle doubleCellStyle = workbook.createCellStyle();
        doubleCellStyle.setDataFormat(
                workbook.getCreationHelper().createDataFormat().getFormat("#.##"));

        // Create a DecimalFormat to rappresent a maximum number of decimal digits
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        // Fill the table
        int rowNum = 1;
        if(!prescriptionMedicines.isEmpty()) {
            // Not empty

            // Create other rows and cells with prescriptions data
            for (PrescriptionMedicine prescriptionMedicine : prescriptionMedicines) {
                Row row = sheet.createRow(rowNum++);
                int col = 0;

                // ID
                row.createCell(col++).setCellValue(prescriptionMedicine.getMedicine().getId());

                // Format the date for the user output
                String formattedDate;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
                formattedDate = prescriptionMedicine.getDateTime().format(formatter);

                // Prescription date
                Cell date = row.createCell(col++);
                date.setCellValue(formattedDate);
                date.setCellStyle(dateCellStyle);

                // Medicine name
                row.createCell(col++).setCellValue(prescriptionMedicine.getMedicine().getName());

                // Doctor ID
                row.createCell(col++).setCellValue(prescriptionMedicine.getDoctorId());

                // Patient ID
                row.createCell(col++).setCellValue(prescriptionMedicine.getPersonId());

                // Price per pcs
                Cell pricePcs = row.createCell(col++);
                pricePcs.setCellValue((double)prescriptionMedicine.getMedicine().getPrice()/100);

                // Quantity
                row.createCell(col++).setCellValue(prescriptionMedicine.getQuantity());

                // Total price
                Cell total = row.createCell(col);
                total.setCellValue((double)prescriptionMedicine.getTotalToPay()/100);
            }
        }
        else{
            // ERROR
            Row row = sheet.createRow(1);

            row.createCell(1).setCellValue("Nessuna ricetta presente");
        }
        /*
        if(!prescriptionMedicines.isEmpty()){
            // Build the Total table. It contains the sum of all quantities and costs
            // NB: It works with maximum range of the basic alphabet columns
            int i = 0;
            int spacing = 1;

            // Headers
            Cell cell = headerRow.createCell(columns.length + spacing + i++);
            cell.setCellValue(totTable[i]);
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(columns.length + spacing + i);
            cell.setCellValue(totTable[i]);
            cell.setCellStyle(headerCellStyle);

            // Formula of SUM
            i = 0;
            // Tot. Quantity
            cell = sheet.getRow(1).getCell(columns.length + spacing + i++);
            cell.setCellValue("=SOMMA(2" + alphabet[quantityCol] + ":" + rowNum + alphabet[quantityCol] + ")");

            // Tot Costs
            cell = sheet.getRow(1).getCell(columns.length + spacing + i);
            cell.setCellValue("=SOMMA(2" + alphabet[costCol] + ":" + rowNum + alphabet[costCol] + ")");
        }
        */

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

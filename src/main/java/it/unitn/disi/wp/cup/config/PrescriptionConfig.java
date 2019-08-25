package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prescription Configuration
 *
 * @author Carlo Corradini
 */
public final class PrescriptionConfig extends Config {

    private static final String FILE_NAME = "prescription.properties";
    private static final String CATEGORY = "prescription";
    private static final Logger LOGGER = Logger.getLogger(PrescriptionConfig.class.getName());
    private static PrescriptionConfig instance;

    private PrescriptionConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the Prescription Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new PrescriptionConfig();
        } else throw new ConfigException("PrescriptionConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("PrescriptionConfig has not been initialized");
    }

    /**
     * Return the Prescription Medicine Min Quantity
     *
     * @return Medicine Min Quantity
     */
    public static short getMedicineMinQuantity() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Medicine Min Quantity", ex);
        }

        return instance.getShort("medicine.minQuantity");
    }

    /**
     * Return the Prescription Medicine Max Quantity
     *
     * @return Medicine Max Quantity
     */
    public static short getMedicineMaxQuantity() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Medicine Max Quantity", ex);
        }

        return instance.getShort("medicine.maxQuantity");
    }

    /**
     * Return the Prescription Exam Report Content min Length
     *
     * @return Exam Report Content min Length
     */
    public static short getExamReportContentMinLength() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Exam Report Content min Length", ex);
        }

        return instance.getShort("exam.report.content.minLength");
    }

    /**
     * Return the Prescription Exam Report Content max Length
     *
     * @return Exam Report Content max Length
     */
    public static short getExamReportContentMaxLength() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Exam Report Content max Length", ex);
        }

        return instance.getShort("exam.report.content.maxLength");
    }
}

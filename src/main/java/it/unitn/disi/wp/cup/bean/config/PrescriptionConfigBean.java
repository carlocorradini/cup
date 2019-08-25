package it.unitn.disi.wp.cup.bean.config;

import it.unitn.disi.wp.cup.config.PrescriptionConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Permits the access to web pages of the Prescription Configuration
 *
 * @author Carlo Corradini
 * @see PrescriptionConfig
 */
@Named("prescription")
@ApplicationScoped
public final class PrescriptionConfigBean implements Serializable {
    private static final long serialVersionUID = 7687302701812972514L;

    /**
     * @see PrescriptionConfig#getMedicineMinQuantity()
     */
    public short getMedicineMinQuantity() {
        return PrescriptionConfig.getMedicineMinQuantity();
    }

    /**
     * @see PrescriptionConfig#getMedicineMaxQuantity()
     */
    public short getMedicineMaxQuantity() {
        return PrescriptionConfig.getMedicineMaxQuantity();
    }

    /**
     * @see PrescriptionConfig#getExamReportContentMinLength()
     */
    public short getExamReportMinLength() {
        return PrescriptionConfig.getExamReportContentMinLength();
    }

    /**
     * @see PrescriptionConfig#getExamReportContentMaxLength()
     */
    public short getExamReportMaxLength() {
        return PrescriptionConfig.getExamReportContentMaxLength();
    }
}

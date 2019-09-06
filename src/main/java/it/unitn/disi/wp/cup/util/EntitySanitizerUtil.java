package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;

import java.util.logging.Logger;

/**
 * Sanitize utility class for removing sensible information about an Entity
 *
 * @author Carlo Corradini
 */
public final class EntitySanitizerUtil {

    private static final Logger LOGGER = Logger.getLogger(EntitySanitizerUtil.class.getName());

    /**
     * Sanitize a {@link Person person} in hard mode.
     * The passed {@code person} as parameter will change
     *
     * @param person The {@link Person Person} to sanitize
     * @return The sanitized {@link Person Person}
     */
    public static Person sanitizePerson(Person person) {
        return sanitizePerson(person, true);
    }

    /**
     * Sanitize a {@link Person person}.
     * The passed {@code person} as parameter will be modified
     *
     * @param person The {@link Person Person} to sanitize
     * @param hard   If set to true the sanitization will remove more sensible information
     * @return The sanitized {@link Person Person}
     */
    public static Person sanitizePerson(Person person, boolean hard) {
        person.setPassword(null);
        person.setEmail(null);

        if (hard) {
            person.setAvatarHistory(null);
            person.setExams(null);
            person.setMedicines(null);
            person.setFiscalCode(null);
        }

        return person;
    }

    /**
     * Sanitize a {@link HealthService Health Service}.
     * The passed {@code healthService} as parameter will be modified
     *
     * @param healthService The {@link HealthService Health Service} to sanitize
     * @return The sanitized {@link HealthService Health Service}
     */
    public static HealthService sanitizeHealthService(HealthService healthService) {
        healthService.setPassword(null);
        healthService.setEmail(null);

        return healthService;
    }

    /**
     * Sanitize a {@link PrescriptionExam Prescription Exam}.
     * The passed {@code prescriptionExam} as parameter will be modified
     *
     * @param prescriptionExam The {@link PrescriptionExam Prescription Exam} to sanitize
     * @return The sanitized {@link PrescriptionExam Prescription Exam}
     */
    public static PrescriptionExam sanitizePrescriptionExam(PrescriptionExam prescriptionExam) {
        prescriptionExam.setReport(null);

        return prescriptionExam;
    }
}

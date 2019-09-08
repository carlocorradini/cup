"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const patients = {
        $exam: $("#patients-prescribe-exam"),
        $medicine: $("#patients-prescribe-medicine")
    };

    // Set automatic Patient Link for Prescription
    window.visit_creator.v.patient.$button.click(function () {
        const $button = $(this);
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(patientId)) {
            patients.$exam.attr("href", URI(patients.$exam.attr("href")).query({patientId: patientId}));
            patients.$medicine.attr("href", URI(patients.$medicine.attr("href")).query({patientId: patientId}));
        }
    });
});
"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const prescribe = {
        $exam: $("#patients-prescribe-exam"),
        $medicine: $("#patients-prescribe-medicine")
    };

    // INIT
    window.visit_creator.init();

    // Set automatic Patient Link for Prescription
    window.visit_creator.v.patient.$button.click(function () {
        const $button = $(this);
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(patientId)) {
            prescribe.$exam.attr("href", URI(prescribe.$exam.attr("href")).query({patientId: patientId}));
            prescribe.$medicine.attr("href", URI(prescribe.$medicine.attr("href")).query({patientId: patientId}));
        }
    });
});
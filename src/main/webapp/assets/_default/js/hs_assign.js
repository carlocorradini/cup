"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Assign Functionality
    window.visit_creator.init();
    assignPrescriptionExamConfig();
});

function assignPrescriptionExamConfig() {
    const assign = {
        $modal: $("#hs-assign-exam-modal"),
        $button: $("#hs-assign-exam-modal-button")
    };

    // Modal
    assign.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Trigger Button
    assign.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId) && window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");

            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(window.visit_creator.v.service.patient, patientId),
                success: function (data) {
                },
                error: function () {
                    console.error("Unable to get Patient");
                }
            });
        }
    });
}

function populateAssignPrescriptionExam() {
    
}
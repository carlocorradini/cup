"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable prescribed Exam Functionality
    prescribedExamConfig();
});

function prescribedExamConfig() {
    const pExam = {
        urlPattern: window.CONTEXT_PATH + "/service/restricted/person/prescription_exam/{1}",
        $modalButtons: $("button.prescribed-exam-view-report-modal-button"),
    };

    // INIT
    window.visit_creator.init();

    // Customize
    window.visit_creator.v.report.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Button Event trigger
    pExam.$modalButtons.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(pExam.urlPattern, prescriptionId),
                success: function (data) {
                    window.visit_creator.populate.report(data);
                    $button.removeClass("loading");
                    window.visit_creator.v.report.$modal.modal("show");
                },
                error: function () {
                    console.error("Unable to get Prescription Exam");
                }
            });
        }
    });
}
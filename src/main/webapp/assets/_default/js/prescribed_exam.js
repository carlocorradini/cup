"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable prescribed Exam Functionality
    prescribedExamConfig();
});

function prescribedExamConfig() {
    const pExam = {
        $table: $("#prescribed-exam-table"),
        report: {
            $modalContainer: $("#prescribed-exam-report-modal-container"),
            buttonIdPattern: "#prescribed-exam-report-modal-button-{1}",
            modalClass: ".report-modal"
        }
    };

    pExam.$table.tablesort();

    // Enable Report Modal
    pExam.report.$modalContainer.find(pExam.report.modalClass).each(function (index, element) {
        $(element).modal({
            allowMultiple: false,
            closable: true,
            inverted: true
        }).modal("attach events", window.UTIL.STRING.format(pExam.report.buttonIdPattern, $(element).data("report-id")), "show");
    });
}
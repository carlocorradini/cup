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
            modalClass: ".report-modal",
            $modalButton: $("button.report-modal-button")
        }
    };

    // Enable Datatable
    pExam.$table.DataTable();

    // Triggers
    pExam.report.$modalButton.click(function () {
        const $button = $(this);
        const reportId = $button.data("report-id");

        if (window.UTIL.NUMBER.isNumber(reportId)) {
            $button.addClass("loading");
            $(`${pExam.report.modalClass}[data-report-id="${reportId}"`).modal({
                allowMultiple: false,
                closable: true,
                inverted: true,
                onShow: function () {
                    $button.removeClass("loading");
                }
            }).modal("show");
        }
    });
}
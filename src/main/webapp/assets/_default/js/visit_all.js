"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const viewReport = {
        urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/prescription_exam/{1}",
    };

    // INIT
    window.visit_creator.init();

    $("button.view-report-modal-button").click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(viewReport.urlPattern, prescriptionId),
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
});
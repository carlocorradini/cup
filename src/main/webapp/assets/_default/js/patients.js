"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Personal Card Functionality
    personalCardConfig();
});

function personalCardConfig() {
    const personalCard = {
        button: $("#doctor-patients .personal-card-modal-button"),
        modalIdPattern: "#personal-card-modal-{1}",
        report: {
            modalClass: ".report-modal",
            buttonIdPattern: "#report-modal-button-{1}"
        }
    };

    personalCard.button.click(function () {
        const patientId = $(this).data("patient-id");
        const $modalPersonalCard = $(window.UTIL.STRING.format(personalCard.modalIdPattern, patientId));

        if ($modalPersonalCard.length) {
            // --- Valid patient Id & Modal
            // Enable Accordion
            $modalPersonalCard.find(".ui.accordion").accordion({
                animateChildren: false
            });
            // Enable Datatable
            $modalPersonalCard.find(".ui.table.sortable").DataTable();
            // Enable Report Modal on Top of Personal Card Modal
            $(personalCard.report.modalClass).each(function (index, element) {
                $(element).modal({
                    allowMultiple: true,
                    closable: false,
                    inverted: true,
                    onShow: function () {
                        $modalPersonalCard.addClass("disabled");
                    },
                    onHide: function () {
                        $modalPersonalCard.removeClass("disabled");
                    }
                }).modal("attach events", window.UTIL.STRING.format(personalCard.report.buttonIdPattern, $(element).data("report-id")), "show");
            });
            // Open Personal Card Modal
            $modalPersonalCard.modal({
                allowMultiple: true,
                closable: false,
                inverted: false
            }).modal("show");
        }
    });
}
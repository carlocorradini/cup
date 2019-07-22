"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Personal Card Functionality
    personalCardConfig();
});

function personalCardConfig() {
    const personalCard = {
        button: $("#doctor-patients .personal-card-button"),
        modal_id_pattern: "#personal-card-modal-{1}"
    };

    personalCard.button.click(function () {
        const patientId = $(this).data("patient-id");
        const $modal = $(window.UTIL.STRING.format(personalCard.modal_id_pattern, patientId));

        if ($modal.length) {
            // Valid patient Id & Modal
            $modal.find(".ui.accordion").accordion();
            $modal.find(".ui.table.sortable").tablesort();
            $modal.modal({
                inverted: true
            }).modal("show");
        }
    });
}
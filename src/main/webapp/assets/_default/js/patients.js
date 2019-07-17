"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Personal Card Functionality
    personalCardConfig();
});

function personalCardConfig() {
    const personalCard = {
        button: $("#doctor-patients .personal-card-button"),
        modal: $("#personal-card-modal")
    };

    personalCard.button.click(function () {
        const patientId = $(this).data("patient-id");

        if (patientId !== null && patientId !== undefined) {
            // Data Attribute is valid
            personalCard.modal.modal("show");
        }
    });
}
"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const patients = {
        $patients: $(".patients-container"),
        filter: {
            $dropdown: $(".patients-filter-dropdown"),
            $input: $(".patients-filter-input"),
            $notFound: $(".patients-empty-message")
        }
    };

    // INIT
    window.visit_creator.init();

    // Filter
    patients.filter.$dropdown
        .dropdown()
        .dropdown("set selected", "name");

    // Filtering
    const mixer = mixitup(patients.$patients.get(0), {
        selectors: {
            target: ".ui.card"
        },
        animation: {
            enable: false
        }
    });

    // Read Input Filter
    patients.filter.$input.keyup(function () {
        patients.filter.$notFound.addClass("hidden");
        if (this.value - length === 0) {
            // Empty -> Show All
            mixer.show();
        } else {
            // NOT Empty -> Filter
            mixer.filter(`[data-${patients.filter.$dropdown.dropdown("get value")}^="${this.value.toLowerCase()}"]`)
                .then(function (state) {
                    if (state.totalShow === 0) {
                        patients.filter.$notFound.removeClass("hidden");
                    }
                });
        }
    });
});
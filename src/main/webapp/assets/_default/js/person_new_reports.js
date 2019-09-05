"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable reports Functionality
    reportsConfig();
});

function reportsConfig() {
    const reports = {
        urlPattern: window.CONTEXT_PATH + "/service/restricted/person/prescription_exam/{1}",
        $modalButtons: $("button.report-view-report-modal-button"),
        $readCheckboxes: $(".reports-read-checkbox"),
        snackbar: {
            $snackbar: $("#reports-snackbar"),
            $button: $("#reports-snackbar-button"),
            $counter: $("#reports-snackbar-button").find("span.label")
        }
    };

    const examsRead = [];

    // INIT
    window.visit_creator.init();

    // Customize
    window.visit_creator.v.report.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Button Event trigger
    reports.$modalButtons.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(reports.urlPattern, prescriptionId),
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

    // Enable checkbox read
    reports.$readCheckboxes.checkbox({
        onChecked: function () {
            const id = $(this).data("exam-id");
            if (window.UTIL.NUMBER.isNumber(id) && !examsRead.includes(id)) {
                examsRead.push(id);
            }
            reports.snackbar.$counter.html(examsRead.length);
            if (examsRead.length === 1) {
                // New
                reports.snackbar.$snackbar.addClass("active");
            }
        },
        onUnchecked: function () {
            const id = $(this).data("exam-id");
            if (window.UTIL.NUMBER.isNumber(id) && examsRead.includes(id)) {
                window.UTIL.ARRAY.remove(examsRead, id);
            }
            reports.snackbar.$counter.html(examsRead.length);
            if (examsRead.length === 0) {
                // Empty
                reports.snackbar.$snackbar.removeClass("active");
            }
        }
    });

    // Send as Read on Button Click
    reports.snackbar.$button.click(function () {
        if (examsRead.length > 0) {
            reports.snackbar.$button.find(".ui.button").addClass("loading");
            $.ajax({
                type: "POST",
                url: window.CONTEXT_PATH + "/service/restricted/person/readExam",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(examsRead),
                success: function (data) {
                    reports.snackbar.$button.find(".ui.button").removeClass("loading");
                    // Add disabled class to table rows with corresponding id
                    for (let i = 0; i < data.ids.length; ++i) {
                        const id = data.ids[i];
                        if (window.UTIL.NUMBER.isNumber(id)) {
                            const row = window.visit_creator.v.$table.find(`tbody tr[data-exam-id="${id}"]`);
                            window.UTIL.ARRAY.remove(examsRead, id);
                            window.visit_creator.v.$table
                                .row(row)
                                .remove()
                                .draw();
                        }
                    }

                    // Update snackbar count
                    reports.snackbar.$counter.html(examsRead.length);
                    // Hide snackbar if count is 0
                    if (examsRead.length === 0) {
                        reports.snackbar.$snackbar.removeClass("active");
                    }
                },
                error: function () {
                    console.error("Unable to read Exam");
                }
            });
        }
    });
}
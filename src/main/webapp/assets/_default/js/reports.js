"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable reports Functionality
    reportsConfig();
});

function reportsConfig() {
    const reports = {
        $table: $("#reports-table"),
        report: {
            $modalContainer: $("#reports-report-modal-container"),
            buttonIdPattern: "#reports-report-modal-button-{1}",
            modalClass: ".report-modal",

        },
        $readCheckboxes: $(".reports-read-checkbox"),
        snackbar: {
            $snackbar: $("#reports-snackbar"),
            $button: $("#reports-snackbar-button"),
            $counter: $("#reports-snackbar-button").find("span.label")
        }
    };

    const examsRead = [];

    // Enable Sorting
    reports.$table.tablesort();

    // Enable Report Modal
    reports.report.$modalContainer.find(reports.report.modalClass).each(function (index, element) {
        $(element).modal({
            allowMultiple: false,
            closable: true,
            inverted: true
        }).modal("attach events", window.UTIL.STRING.format(reports.report.buttonIdPattern, $(element).data("report-id")), "show");
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
                            window.UTIL.ARRAY.remove(examsRead, id);
                            reports.$table.find('tbody tr[data-exam-id="' + id + '"]').addClass("disabled positive");
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
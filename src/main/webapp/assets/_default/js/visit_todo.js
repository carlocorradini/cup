"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const writeReport = {
        $modal: $(".visit-todo-write-report-modal"),
        $button: $("button.visit-todo-write-report-modal-button"),
        $form: $("form.visit-todo-write-report-form"),
        patient: {
            urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
            $fullName: $(".visit-todo-write-report-patient-full-name"),
            $fiscalCode: $(".visit-todo-write-report-patient-fiscal-code"),
            $avatar: $(".visit-todo-write-report-patient-avatar")
        },
        data: {
            $content: $("textarea.visit-todo-write-report-data-content"),
            $contentLength: $("span.visit-todo-write-report-data-content-length"),
            $paid: $(".visit-todo-write-report-data-paid"),
            $examsDropdown: $(".visit-todo-write-report-data-exams"),
            $medicinesDropdown: $(".visit-todo-write-report-data-medicines")
        },
        populate: {
            report: function (patient) {
                if (patient !== null && patient !== undefined) {
                    writeReport.patient.$fullName.html(patient.fullNameCapitalized);
                    writeReport.patient.$fiscalCode.html(patient.fiscalCode);
                    writeReport.patient.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));
                }
            }
        }
    };

    // The Object that represent a new Report
    const newReport = {
        prescriptionId: undefined,
        content: undefined,
        paid: false,
        exams: [],
        medicines: []
    };

    // INIT
    window.visit_creator.init();
    // Modal
    writeReport.$modal.modal({
        inverted: true
    });
    // Count chars
    writeReport.data.$content.on("keyup", function () {
        writeReport.data.$contentLength.html(writeReport.data.$content.attr("maxLength") - this.value.length);
    });
    // Checkbox
    writeReport.data.$paid.checkbox({
        onChecked: function () {
            const label = writeReport.data.$paid.find("label");
            label.html(label.data("paid"));
        },
        onUnchecked: function () {
            const label = writeReport.data.$paid.find("label");
            label.html(label.data("paid-not"));
        }
    });
    // Dropdown
    writeReport.data.$examsDropdown.dropdown({
        clearable: true,
        allowAdditions: false
    });
    writeReport.data.$medicinesDropdown.dropdown({
        clearable: true,
        allowAdditions: false
    });
    // Triggers Modal
    writeReport.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId) && window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");

            if (prescriptionId !== newReport.prescriptionId) {
                // DO ALL only if the last Prescription Id is different
                // Save prescriptionId into new Report obj
                newReport.prescriptionId = prescriptionId;
                // Reset the Modal
                writeReport.$form.removeClass("disabled success warning");
                writeReport.data.$content.val("");
                writeReport.data.$paid.checkbox("enable");
                writeReport.data.$paid.checkbox("uncheck");
                writeReport.data.$contentLength.html(writeReport.data.$content.attr("maxLength"));
                writeReport.data.$examsDropdown.dropdown("refresh");
                writeReport.data.$examsDropdown.dropdown("clear");
                writeReport.data.$medicinesDropdown.dropdown("refresh");
                writeReport.data.$medicinesDropdown.dropdown("clear");

                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(writeReport.patient.urlPattern, patientId),
                    success: function (data) {
                        writeReport.populate.report(data);
                        // Disable Paid checkbox if it's already paid
                        $.each(data.exams, function (index, element) {
                            if (element.id === prescriptionId) {
                                if (element.paid) {
                                    writeReport.data.$paid.checkbox("check");
                                    writeReport.data.$paid.checkbox("disable");
                                }
                                return false;
                            }
                        });
                    },
                    error: function () {
                        console.error("Unable to get Patient");
                    }
                });
            }

            writeReport.$modal.modal("show");
            $button.removeClass("loading");
        } else {
            console.error("Prescription Id or Patient Id is not a Number");
        }
    });

    // Form
    writeReport.$form.form({
        fields: {
            content: {
                identifier: "content",
                rules: [
                    {
                        type: "empty",
                        prompt: window.visit_creator.v.i18n.newReport.empty
                    },
                    {
                        type: `minLength[${writeReport.data.$content.attr("minlength")}]`,
                        prompt: window.visit_creator.v.i18n.newReport.minLength
                    },
                    {
                        type: `maxLength[${writeReport.data.$content.attr("maxlength")}]`,
                        prompt: window.visit_creator.v.i18n.newReport.maxLength
                    }
                ]
            },
            paid: {
                identifier: "paid",
                rules: [
                    {
                        type: "checked",
                        prompt: window.visit_creator.v.i18n.newReport.unpaid
                    }
                ]
            }
        },
        onSuccess: function () {
            // Show Loading
            writeReport.$form.removeClass("warning success").addClass("loading");

            // Reset
            newReport.exams = [];
            newReport.medicines = [];
            // Dropdown Values
            const examsArray = writeReport.data.$examsDropdown.dropdown("get value").split(",");
            const medicinesArray = writeReport.data.$medicinesDropdown.dropdown("get value").split(",");

            // Set newReport obj template
            if (examsArray.length !== 0 && examsArray[0] !== "") {
                newReport.exams = examsArray;
            }
            if (medicinesArray.length !== 0 && medicinesArray[0] !== "") {
                newReport.medicines = medicinesArray;
            }
            // Others
            newReport.content = writeReport.data.$content.val();
            newReport.paid = writeReport.data.$paid.checkbox("is checked");

            // Send newReport
            $.ajax({
                type: "POST",
                url: window.CONTEXT_PATH + "/service/restricted/specialist/report",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(newReport),
                success: function (data) {
                    writeReport.$form.removeClass("loading");
                    if (data.error === 0) {
                        // Added successfully
                        writeReport.$form.addClass("disabled success");
                        // Unused row
                        window.visit_creator.v.$table
                            .DataTable()
                            .row(window.visit_creator.v.$table.find(`tbody tr[data-prescription-id="${newReport.prescriptionId}"]`))
                            .remove()
                            .draw();
                    } else {
                        writeReport.$form.addClass("warning");
                    }
                },
                error: function () {
                    console.error("Unable to write a Report");
                }
            });

            return false;
        }
    });
});
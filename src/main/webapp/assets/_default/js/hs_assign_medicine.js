"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const assign = {
        service: {
            assign: window.CONTEXT_PATH + "/service/restricted/health_service/assignMedicine"
        },
        $modal: $("#hs-assign-medicine-modal"),
        $button: $(".hs-assign-medicine-modal-button"),
        $form: $("#hs-assign-medicine-form"),
        patient: {
            $fullName: $("span#hs-assign-medicine-patient-full-name"),
            $fiscalCode: $("span#hs-assign-medicine-patient-fiscal-code"),
            $avatar: $("img#hs-assign-medicine-patient-avatar")
        },
        $paid: $("#hs-assign-medicine-checkbox-paid"),
        populate: {
            patient: function (patient) {
                if (patient !== null && patient !== undefined) {
                    assign.patient.$fullName.html(patient.fullNameCapitalized);
                    assign.patient.$fiscalCode.html(patient.fiscalCode);
                    assign.patient.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));
                }
            }
        },
        do: {
            entry: function (patientId, doAfter) {
                assign.do.patient(patientId, function () {
                    doAfter();
                });
            },
            patient: function (patientId, doAfter) {
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(window.visit_creator.v.service.patient, patientId),
                    success: function (data) {
                        assign.populate.patient(data);
                        doAfter();
                    },
                    error: function () {
                        console.error("Unable to get Patient");
                    }
                });
            }
        }
    };

    // INIT
    window.visit_creator.init();

    // i18n
    const i18n = window.visit_creator.v.i18n;

    // The Object that represent a new Medicine Assign
    const newAssign = {
        prescriptionId: undefined,
        paid: false
    };

    // Modal
    assign.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Checkbox Paid
    assign.$paid.checkbox({
        onChecked: function () {
            const label = assign.$paid.find("label");
            label.html(label.data("paid"));
        },
        onUnchecked: function () {
            const label = assign.$paid.find("label");
            label.html(label.data("paid-not"));
        }
    });

    // Trigger Button
    assign.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const patientId = $button.data("patient-id");
        // Finished all settings and loading
        const doAfter = function () {
            assign.$modal.modal("show");
            $button.removeClass("loading");
        };

        if (window.UTIL.NUMBER.isNumber(prescriptionId) && window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");

            if (newAssign.prescriptionId !== prescriptionId) {
                // DO ALL only if the last Prescription Id is different
                // RESET
                assign.$form.removeClass("disabled success warning error");
                assign.$form.find(".field.error").removeClass("error");
                assign.$paid.checkbox("enable");
                assign.$paid.checkbox("uncheck");

                // Save prescriptionId into new Assign obj
                newAssign.prescriptionId = prescriptionId;

                // Populate
                assign.do.entry(patientId, doAfter);
            } else doAfter();
        }
    });

    assign.$form.form({
        fields: {
            paid: {
                identifier: "paid",
                rules: [
                    {
                        type: "checked",
                        prompt: i18n.unpaid
                    }
                ]
            }
        },
        onSuccess: function () {
            // Show Loading
            assign.$form.removeClass("warning success").addClass("loading");

            // Set
            newAssign.paid = assign.$paid.checkbox("is checked");

            // Send newAssign
            $.ajax({
                type: "POST",
                url: assign.service.assign,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(newAssign),
                success: function (data) {
                    assign.$form.removeClass("loading");

                    if (data.error === 0) {
                        // Added successfully
                        assign.$form.addClass("disabled success");
                        // Remove table row
                        window.visit_creator.v.$table
                            .DataTable()
                            .row(window.visit_creator.v.$table.find(`tbody tr[data-prescription-id="${newAssign.prescriptionId}"]`))
                            .remove()
                            .draw();
                    } else {
                        assign.$form.addClass("warning");
                    }
                },
                error: function () {
                    console.error("Unable to assign a Prescription Exam");
                }
            });

            return false;
        }
    });
});
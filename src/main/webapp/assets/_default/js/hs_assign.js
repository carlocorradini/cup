"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Assign Functionality
    window.visit_creator.init();
    assignPrescriptionExamConfig();
});

function assignPrescriptionExamConfig() {
    const assign = {
        $modal: $(".hs-assign-exam-modal"),
        $button: $(".hs-assign-exam-modal-button"),
        $form: $(".hs-assign-exam-form"),
        patient: {
            $fullName: $("span.hs-assign-exam-patient-full-name"),
            $fiscalCode: $("span.hs-assign-exam-patient-fiscal-code"),
            $avatar: $("img.hs-assign-exam-patient-avatar")
        },
        executor: {},
        service: {
            qualified: window.CONTEXT_PATH + "/service/restricted/medical/qualified/{1}/{2}"
        },
        populate: {
            assign: function (patient) {
                if (patient !== null && patient !== undefined) {
                    assign.patient.$fullName.html(patient.fullNameCapitalized);
                    assign.patient.$fiscalCode.html(patient.fiscalCode);
                    assign.patient.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));
                }
            }
        },
        do: {
            entry: function (patientId, provinceId, examId, doAfter) {
                assign.do.patient(patientId, function () {
                    assign.do.executor(provinceId, examId, function () {
                        doAfter();
                    });
                });
            },
            patient: function (patientId, doAfter) {
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(window.visit_creator.v.service.patient, patientId),
                    success: function (data) {
                        assign.populate.assign(data);
                        doAfter();
                    },
                    error: function () {
                        console.error("Unable to get Patient");
                    }
                });
            },
            executor: function (provinceId, examId, doAfter) {
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(assign.service.qualified, provinceId, examId),
                    success: function (data) {
                        console.log(data);
                        doAfter();
                    },
                    error: function () {
                        console.error("Unable to get qualified Executor");
                    }
                });
            }
        }
    };

    const i18n = window.visit_creator.v.i18n;

    // The Object that represent a new Exam Assign
    const newAssign = {
        prescriptionId: undefined,
        executorId: undefined,
    };

    // Modal
    assign.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Trigger Button
    assign.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const examId = $button.data("exam-id");
        const patientId = $button.data("patient-id");
        const provinceId = $button.data("province-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId) && window.UTIL.NUMBER.isNumber(examId)
            && window.UTIL.NUMBER.isNumber(patientId) && window.UTIL.NUMBER.isNumber(provinceId)) {
            $button.addClass("loading");
            assign.do.entry(patientId, provinceId, examId, function () {
                // Finished all settings and loading
                assign.$modal.modal("show");
                $button.removeClass("loading");
            });
        }
    });
}
"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable prescription Exam Functionality
    prescriptionExamConfig();
});

function prescriptionExamConfig() {
    const pExam = {
        $form: $("#prescription-exam-form"),
        $dropdownPatient: $("#prescription-exam-patient-dropdown"),
        $dropdownExam: $("#prescription-exam-exam-dropdown"),
        $checkboxPaid: $("#prescription-exam-paid-checkbox"),
        $button: $("#prescription-exam-button")
    };
    const prescription = {
        patientId: null,
        examId: null,
        paid: false
    };
    const patientId = window.UTIL.URL.getParams().patientId;

    pExam.$form.submit(function () {
        return false;
    });

    pExam.$dropdownPatient.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            prescription.patientId = value;
        }
    });
    // Set the Default Patient if present
    if (window.UTIL.NUMBER.isNumber(patientId)) {
        pExam.$dropdownPatient.dropdown("set selected", patientId);
        prescription.patientId = patientId;
    }
    // Exams dropdown
    pExam.$dropdownExam.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            prescription.examId = value;
        }
    });
    // Paid checkbox
    pExam.$checkboxPaid.checkbox({
        onChecked: function () {
            const label = pExam.$checkboxPaid.find("label");
            label.html(label.data("paid"));
            prescription.paid = true;
        },
        onUnchecked: function () {
            const label = pExam.$checkboxPaid.find("label");
            label.html(label.data("paid-not"));
            prescription.paid = false;
        }
    });

    pExam.$button.click(function () {
        if (UTIL.NUMBER.isNumber(prescription.patientId) && UTIL.NUMBER.isNumber(prescription.examId)) {
            pExam.$form.removeClass("warning success").addClass("loading");

            $.ajax({
                type: "POST",
                url: window.CONTEXT_PATH + "/service/restricted/doctor/prescription_exam",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(prescription),
                success: function (data) {
                    pExam.$form.removeClass("loading");
                    if (data.error === 0)
                        pExam.$form.addClass("success");
                    else
                        pExam.$form.addClass("warning");
                },
                error: function () {
                    console.error("Unable to Prescribe an Exam");
                }
            });
        } else {
            pExam.$form.removeClass("success").addClass("warning");
        }
    });
}
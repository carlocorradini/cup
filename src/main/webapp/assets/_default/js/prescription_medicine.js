"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable prescription Medicine Functionality
    prescriptionMedicineConfig();
});

function prescriptionMedicineConfig() {
    const pMedicine = {
        $form: $("#prescription-medicine-form"),
        $dropdownPatient: $("#prescription-medicine-patient-dropdown"),
        $dropdownMedicine: $("#prescription-medicine-medicine-dropdown"),
        $inputQuantity: $("#prescription-medicine-quantity-input"),
        $button: $("#prescription-medicine-button")
    };
    const prescription = {
        patientId: null,
        medicineId: null,
        quantity: null
    };
    const patientId = window.UTIL.URL.getParams().patientId;

    pMedicine.$form.submit(function () {
        return false;
    });

    pMedicine.$dropdownPatient.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            prescription.patientId = value;
        }
    });
    // Set the Default Patient if present
    if (window.UTIL.NUMBER.isNumber(patientId)) {
        pMedicine.$dropdownPatient.dropdown("set selected", patientId);
    }

    pMedicine.$dropdownMedicine.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            prescription.medicineId = value;
        }
    });

    pMedicine.$inputQuantity.on("change paste keyup", function () {
        const quantity = $(this).val();
        if (UTIL.NUMBER.isNumber(quantity)) {
            prescription.quantity = quantity;
        }
    });

    pMedicine.$button.click(function () {
        if (UTIL.NUMBER.isNumber(prescription.patientId) && UTIL.NUMBER.isNumber(prescription.medicineId) && UTIL.NUMBER.isNumber(prescription.quantity)) {
            pMedicine.$form.removeClass("warning success").addClass("loading");

            $.ajax({
                type: "POST",
                url: window.CONTEXT_PATH + "/service/restricted/doctor/prescription_medicine",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(prescription),
                success: function (data) {
                    pMedicine.$form.removeClass("loading");
                    if (data.error === 0)
                        pMedicine.$form.addClass("success");
                    else
                        pMedicine.$form.addClass("warning");
                },
                error: function () {
                    console.error("Unable to Prescribe a Medicine");
                }
            });
        } else {
            pMedicine.$form.removeClass("success").addClass("warning");
        }
    });
}
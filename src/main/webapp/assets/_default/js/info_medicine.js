"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const info = {
        service: {
            medicine: window.CONTEXT_PATH + "/service/open/medicine/get/{1}"
        },
        $dropdown: $("#info-medicine-dropdown"),
        $segment: $("#info-medicine-segment"),
        $message: $("#info-medicine-message"),
        table: {
            id: $("#info-medicine-td-id"),
            name: $("#info-medicine-td-name"),
            price: $("#info-medicine-td-price"),
            description: $("#info-medicine-td-description")
        },
        populate: function (medicine) {
            info.table.id.html(medicine.id);
            info.table.name.html(medicine.name);
            info.table.price.html(medicine.price);
            info.table.description.html(medicine.description);
        }
    };

    info.$dropdown.dropdown({
        clearable: true,
        onChange: function (medicineId, text, $item) {
            if (window.UTIL.NUMBER.isNumber(medicineId)) {
                // Hide message
                info.$message.addClass("hidden");
                info.$segment.removeClass("hidden");
                // Loading
                info.$segment.addClass("loading");
                // Retrieve information
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(info.service.medicine, medicineId),
                    success: function (data) {
                        info.populate(data);
                        info.$segment.removeClass("loading");
                    },
                    error: function () {
                        console.error("Unable to get Medicine information");
                    }
                });
            }
        }
    });
});
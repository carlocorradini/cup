"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const info = {
        service: {
            exam: window.CONTEXT_PATH + "/service/open/exam/get/{1}"
        },
        $dropdown: $("#info-exam-dropdown"),
        $segment: $("#info-exam-segment"),
        $message: $("#info-exam-message"),
        table: {
            id: $("#info-exam-td-id"),
            name: $("#info-exam-td-name"),
            price: $("#info-exam-td-price"),
            description: $("#info-exam-td-description"),
            supported: $("#info-exam-td-supported")
        },
        populate: function (exam) {
            const icon = "<i class=\"{1} {2} icon\"/>";

            info.table.id.html(exam.id);
            info.table.name.html(exam.name);
            info.table.price.html(exam.price);
            info.table.description.html(exam.description);
            if (exam.supported) {
                info.table.supported.html(window.UTIL.STRING.format(icon, "checkmark", "green"));
            } else {
                info.table.supported.html(window.UTIL.STRING.format(icon, "close", "red"));
            }
        }
    };

    info.$dropdown.dropdown({
        clearable: true,
        onChange: function (examId, text, $item) {
            if (window.UTIL.NUMBER.isNumber(examId)) {
                // Hide message
                info.$message.addClass("hidden");
                info.$segment.removeClass("hidden");
                // Loading
                info.$segment.addClass("loading");
                // Retrieve information
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(info.service.exam, examId),
                    success: function (data) {
                        info.populate(data);
                        info.$segment.removeClass("loading");
                    },
                    error: function () {
                        console.error("Unable to get Exam information");
                    }
                });
            }
        }
    });
});
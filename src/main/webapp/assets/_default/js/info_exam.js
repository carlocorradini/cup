"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const info = {
        service: {
            exam: window.CONTEXT_PATH + "/service/open/exam/get/{1}"
        },
        $dropdown: $("#info-exam-dropdown"),
        $segment: $("#info-exam-segment"),
        populate: function (examId) {
            if (window.UTIL.NUMBER.isNumber(examId)) {
                info.$segment.addClass("loading");
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(info.service.exam, examId),
                    success: function (data) {
                    },
                    error: function () {
                        console.error("Unable to get Exam information");
                    }
                });
            }
        }
    };

    info.$dropdown.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            info.populate(value);
        }
    });
});
"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const report = {
        service: {
            exam: window.CONTEXT_PATH + "/service/restricted/health_service/downloadReportExam/{1}/{2}/{3}",
            medicine: window.CONTEXT_PATH + "/service/restricted/health_service/downloadReportMedicine/{1}/{2}/{3}"
        },
        $inputDate: $("#hs-report-date"),
        $form: $("#hs-report-form")
    };

    // Option, default is for Exam
    let option = "exam";

    // Checkbox
    report.$form.find(".checkbox").checkbox({
        onChange: function () {
            option = $(this).data("option");
        }
    });

    // Date Picker
    report.$inputDate.calendar({
        type: "date",
        maxDate: new Date(),
        firstDayOfWeek: 1,
        inline: true,
        today: true,
        closable: false,
        onChange: function (date, text, mode) {
            let service = undefined;
            
            switch (option) {
                case "exam": {
                    service = report.service.exam;
                    break;
                }
                case "medicine": {
                    service = report.service.medicine;
                    break;
                }
                default: {
                    console.error("Invalid Option");
                    break;
                }
            }

            if (service !== null && service !== undefined) {
                window.open(window.UTIL.STRING.format(service, date.getFullYear(), date.getMonth() + 1, date.getDate()), "_blank");
            }
        }
    });

});
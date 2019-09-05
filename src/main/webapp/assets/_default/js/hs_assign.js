"use strict";

// === PAGE READY ===
$(document).ready(() => {
    const assign = {
        service: {
            qualified: window.CONTEXT_PATH + "/service/restricted/medical/qualified/{1}",
            assign: window.CONTEXT_PATH + "/service/restricted/health_service/assign"
        },
        $modal: $("#hs-assign-exam-modal"),
        $button: $(".hs-assign-exam-modal-button"),
        $form: $("#hs-assign-exam-form"),
        patient: {
            $fullName: $("span#hs-assign-exam-patient-full-name"),
            $fiscalCode: $("span#hs-assign-exam-patient-fiscal-code"),
            $avatar: $("img#hs-assign-exam-patient-avatar")
        },
        executor: {
            $message: $("#hs-assign-exam-executor-message"),
            $field: $("#hs-assign-exam-executor-field"),
            $dropdown: $("#hs-assign-exam-executor-dropdown"),
            $dropdownItems: $("#hs-assign-exam-executor-dropdown-items"),
            $inputHidden: $("#hs-assign-exam-executor-input-hidden")
        },
        $inputDate: $("#hs-assign-exam-date"),
        $inputTime: $("#hs-assign-exam-time"),
        populate: {
            patient: function (patient) {
                if (patient !== null && patient !== undefined) {
                    assign.patient.$fullName.html(patient.fullNameCapitalized);
                    assign.patient.$fiscalCode.html(patient.fiscalCode);
                    assign.patient.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));
                }
            },
            executor: function (executors) {
                if (executors !== null && executors !== undefined) {
                    $.each(executors, function (index, element) {
                            assign.executor.$dropdownItems.append(
                                `<div class="item" data-value="${element.id}">` +
                                `<img src="${window.UTIL.JSF.toResourceURL("_default", element.avatar.nameAsResource)}"` +
                                ` alt="Avatar" style="width: 2em; height: 2em;"` +
                                ` class="ui image bordered rounded person-avatar middle aligned"/>` +
                                `${element.fullNameCapitalized}` +
                                `<span class="text-light text-right">${element.id}</span>` +
                                `</div>`
                            );
                        }
                    );
                }
            }
        },
        do: {
            entry: function (patientId, examId, examIsSupported, doAfter) {
                assign.do.patient(patientId, function () {
                    if (!examIsSupported) {
                        assign.executor.$message.addClass("hidden");
                        assign.executor.$field.removeClass("hidden");
                        assign.do.executor(examId, function () {
                            doAfter();
                        });
                    } else {
                        assign.executor.$message.removeClass("hidden");
                        assign.executor.$field.addClass("hidden");
                        doAfter();
                    }
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
            },
            executor: function (examId, doAfter) {
                $.ajax({
                    type: "GET",
                    url: window.UTIL.STRING.format(assign.service.qualified, examId),
                    success: function (data) {
                        assign.populate.executor(data);
                        doAfter();
                    },
                    error: function () {
                        console.error("Unable to get qualified Executor");
                    }
                });
            }
        }
    };

    // INIT
    window.visit_creator.init();

    // i18n
    const i18n = window.visit_creator.v.i18n;

    // The Object that represent a new Exam Assign
    const newAssign = {
        prescriptionId: undefined,
        executorId: undefined,
        dateTime: {
            date: {
                day: undefined,
                month: undefined,
                year: undefined
            },
            time: {
                hour: undefined,
                minute: undefined
            }
        }
    };

    // Modal
    assign.$modal.modal({
        allowMultiple: false,
        closable: true,
        inverted: true
    });

    // Executor Dropdown
    assign.executor.$dropdown.dropdown({
        clearable: false,
        onChange: function (value, text, $item) {
            assign.executor.$inputHidden.val(value);
        }
    });

    // Date Picker
    const today = new Date();
    assign.$inputDate.calendar({
        type: "date",
        minDate: new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1),
        firstDayOfWeek: 1
    });

    // Time Picker
    assign.$inputTime.calendar({
        type: "time",
        ampm: false,
        minDate: new Date(today.getFullYear(), today.getMonth(), today.getDate(), assign.$inputTime.data("min")),
        maxDate: new Date(today.getFullYear(), today.getMonth(), today.getDate(), assign.$inputTime.data("max"))
    });

    // Trigger Button
    assign.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const examId = $button.data("exam-id");
        const examIsSupported = Boolean($button.data("exam-supported"));
        const patientId = $button.data("patient-id");
        // Finished all settings and loading
        const doAfter = function () {
            assign.$modal.modal("show");
            $button.removeClass("loading");
        };

        if (window.UTIL.NUMBER.isNumber(prescriptionId) && window.UTIL.NUMBER.isNumber(examId)
            && window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");

            if (newAssign.prescriptionId !== prescriptionId) {
                // DO ALL only if the last Prescription Id is different
                // RESET
                assign.$form.removeClass("disabled success warning error");
                assign.$form.find(".field.error").removeClass("error");
                assign.$inputDate.calendar("refresh");
                assign.$inputDate.calendar("clear");
                assign.$inputTime.calendar("refresh");
                assign.$inputTime.calendar("clear");
                assign.executor.$inputHidden.val("");
                assign.executor.$dropdownItems.empty();
                assign.executor.$dropdown.dropdown("restore defaults");

                // Save prescriptionId into new Assign obj
                newAssign.prescriptionId = prescriptionId;
                // Save Executor as Health Service
                if (examIsSupported) {
                    assign.executor.$inputHidden.val(window.visit_creator.v.$table.data("health-service-id"));
                }

                // Populate
                assign.do.entry(patientId, examId, examIsSupported, doAfter);
            } else doAfter();
        }
    });

    assign.$form.form({
        fields: {
            executor: {
                identifier: "executor",
                rules: [
                    {
                        type: "empty",
                        prompt: i18n.executor.empty
                    }
                ]
            },
            date: {
                identifier: "date",
                rules: [
                    {
                        type: "empty",
                        prompt: i18n.executor.emptyDate
                    }
                ]
            },
            time: {
                identifier: "time",
                rules: [
                    {
                        type: "empty",
                        prompt: i18n.executor.emptyTime
                    }
                ]
            }
        },
        onSuccess: function () {
            // Show Loading
            assign.$form.removeClass("warning success").addClass("loading");

            // Get Dates
            const visitDate = assign.$inputDate.calendar("get date");
            const visitTime = assign.$inputTime.calendar("get date");

            // Set
            newAssign.executorId = parseInt(assign.executor.$inputHidden.val(), 10);
            newAssign.dateTime.date.day = visitDate.getDate();
            newAssign.dateTime.date.month = visitDate.getMonth() + 1;
            newAssign.dateTime.date.year = visitDate.getFullYear();
            newAssign.dateTime.time.hour = visitTime.getHours();
            newAssign.dateTime.time.minute = visitTime.getMinutes();

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
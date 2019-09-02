"use strict";

/**
 * Visit Creator
 * @type {{init: Window.visit_creator.init, populate: {doctor: Window.visit_creator.populate.doctor, patient: Window.visit_creator.populate.patient, report: Window.visit_creator.populate.report}, v: *, isDefined: (function(): boolean), check: Window.visit_creator.check}}
 * @author Carlo Corradini
 */
window.visit_creator = {
    /**
     * v object for ui
     */
    v: undefined,
    /**
     * Check if {@code v} is defined
     * @returns {boolean} Return true if {@code v} is defined, false otherwise
     */
    isDefined: function () {
        return window.visit_creator.v !== null && window.visit_creator.v !== undefined;
    },
    /**
     * Check with error throwing if {@code v} is not defined
     */
    check: function () {
        if (!window.visit_creator.isDefined()) {
            throw "Visit Creator is not initialized, call init()";
        }
    },
    /**
     * Initialize the Visit Creator functionality
     */
    init: function () {
        if (window.visit_creator.isDefined()) {
            console.warn("Visit Creator is already initialized");
        } else {
            if (!window.jQuery) {
                throw "jQuery is not defined";
            }
            if (!window.moment) {
                throw "Moment is not defined";
            }
            if (!window.UTIL) {
                throw "UTIL is not defined";
            }

            const v = window.visit_creator.v = {
                $table: $(".visit-creator-table"),
                i18n: $(".visit-creator-i18n").data("i18n"),
                service: {
                    patient: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
                    doctor: window.CONTEXT_PATH + "/service/open/doctor/get/{1}",
                    specialist: window.CONTEXT_PATH + "/service/open/specialist/get/{1}",
                    healthService: window.CONTEXT_PATH + "/service/open/health_service/get/{1}",
                    prescriptionExam: window.CONTEXT_PATH + "/service/restricted/medical/prescription_exam/{1}"
                },
                patient: {
                    $modal: $(".visit-creator-patient-modal"),
                    $button: $("button.visit-creator-patient-modal-button"),
                    $accordion: $(".visit-creator-patient-accordion"),
                    personalInfomation: {
                        $id: $("span.visit-creator-patient-id"),
                        $name: $("span.visit-creator-patient-name"),
                        $surname: $("span.visit-creator-patient-surname"),
                        $fullName: $("span.visit-creator-patient-full-name"),
                        $sex: $("span.visit-creator-patient-sex"),
                        $fiscalCode: $("span.visit-creator-patient-fiscal-code"),
                        $birthDate: $("span.visit-creator-patient-birth-date"),
                        $birthCity: $("span.visit-creator-patient-birth-city"),
                        $domicile: $("span.visit-creator-patient-domicile"),
                        $avatar: $("img.visit-creator-patient-avatar")
                    },
                    exams: {
                        $emptyMessage: $(".visit-creator-patient-exams-empty"),
                        $table: $("table.visit-creator-patient-exams-table")
                    },
                    medicines: {
                        $emptyMessage: $(".visit-creator-patient-medicines-empty"),
                        $table: $("table.visit-creator-patient-medicines-table")
                    },
                    timeline: {
                        $emptyMessage: $(".visit-creator-patient-timeline-empty"),
                        $table: $("table.visit-creator-patient-timeline-table")
                    }
                },
                doctor: {
                    $modal: $(".visit-creator-doctor-modal"),
                    $button: $(".visit-creator-doctor-modal-button"),
                    $id: $(".visit-creator-doctor-id"),
                    $fullName: $(".visit-creator-doctor-full-name"),
                    $province: $(".visit-creator-doctor-province"),
                    $avatar: $(".visit-creator-doctor-avatar")
                },
                executor: {
                    $modal: $(".visit-creator-executor-modal"),
                    $button: $(".visit-creator-executor-modal-button"),
                    $message: $(".visit-creator-executor-message"),
                    $id: $(".visit-creator-executor-id"),
                    $fullName: $(".visit-creator-executor-full-name"),
                    $territory: $(".visit-creator-executor-territory"),
                    $avatar: $(".visit-creator-executor-avatar")
                },
                report: {
                    $modal: $(".visit-creator-patient-report-modal"),
                    buttonClass: "visit-creator-patient-modal-report-button",
                    $id: $("span.visit-creator-patient-report-id"),
                    $date: $("span.visit-creator-patient-report-date"),
                    $time: $("span.visit-creator-patient-report-time"),
                    $content: $("textarea.visit-creator-patient-report-content"),
                    executor: {
                        $message: $(".visit-creator-patient-report-executor-message"),
                        $accordion: $(".visit-creator-patient-report-executor-accordion"),
                        $id: $(".visit-creator-patient-report-executor-id"),
                        $fullName: $(".visit-creator-patient-report-executor-full-name"),
                        $territory: $(".visit-creator-patient-report-executor-territory"),
                        $avatar: $(".visit-creator-patient-report-executor-avatar")
                    },
                    exam: {
                        $emptyMessage: $(".visit-creator-patient-report-exam-empty"),
                        $container: $(".visit-creator-patient-report-exam-container"),
                        $list: $(".visit-creator-patient-report-exam-list")
                    },
                    medicine: {
                        $emptyMessage: $(".visit-creator-patient-report-medicine-empty"),
                        $container: $(".visit-creator-patient-report-medicine-container"),
                        $list: $(".visit-creator-patient-report-medicine-list")
                    }
                }
            };

            // Set moment correct locale
            window.moment.locale(window.navigator.userLanguage || window.navigator.language);

            // Enable Datatable
            v.$table.DataTable();

            // === DOCTOR ===
            // Modal
            v.doctor.$modal.modal({
                inverted: true
            });
            // Populate
            v.doctor.$button.click(function () {
                const $button = $(this);
                const doctorId = $button.data("doctor-id");

                if (window.UTIL.NUMBER.isNumber(doctorId)) {
                    $button.addClass("loading");
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(v.service.doctor, doctorId),
                        success: function (data) {
                            window.visit_creator.populate.doctor(data);
                            $button.removeClass("loading");
                            v.doctor.$modal.modal("show");
                        },
                        error: function () {
                            console.error("Unable to get Doctor");
                        }
                    });
                }
            });

            // === EXECUTOR ===
            // Modal
            v.executor.$modal.modal({
                inverted: true
            });
            // Populate
            v.doctor.$button.click(function () {
                const $button = $(this);
                const executorId = $button.data("executor-id");
                const isSupported = Boolean($button.data("is-supported"));
                const executor = {
                    message: undefined,
                    id: undefined,
                    fullName: undefined,
                    territory: undefined,
                    avatar: undefined
                };

                if (window.UTIL.NUMBER.isNumber(executorId)) {
                    const url = (isSupported) ? v.service.healthService : v.service.specialist;

                    $button.addClass("loading");
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(url, executorId),
                        success: function (data) {
                            if (isSupported) {
                                // Health Service
                                executor.message = v.i18n.executorIsHealthService;
                                executor.id = data.id;
                                executor.fullName = data.province.nameLongCapitalized;
                                executor.territory = data.province.region.nameCapitalized;
                                executor.avatar = window.UTIL.JSF.toResourceURL("_default", data.crestAsResource);
                            } else {
                                // Specialist
                                executor.message = v.i18n.executorIsSpecialist;
                                executor.id = data.id;
                                executor.fullName = data.fullNameCapitalized;
                                executor.territory = data.city.nameCapitalized;
                                executor.avatar = window.UTIL.JSF.toResourceURL("_default", data.avatar.nameAsResource);
                            }
                            window.visit_creator.populate.executor(executor);
                            $button.removeClass("loading");
                            v.executor.$modal.modal("show");
                        },
                        error: function () {
                            console.error("Unable to get Executor");
                        }
                    });
                }
            });

            // === PATIENT ===
            // Modal
            v.patient.$modal.modal({
                allowMultiple: true,
                closable: false,
                inverted: false
            });
            // Accordion
            v.patient.$accordion.accordion({
                animateChildren: false
            });
            // Populate
            v.patient.$button.click(function () {
                const $button = $(this);
                const patientId = $button.data("patient-id");

                if (window.UTIL.NUMBER.isNumber(patientId)) {
                    $button.addClass("loading");
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(v.service.patient, patientId),
                        success: function (data) {
                            window.visit_creator.populate.patient(data);
                            // Enable Datatable
                            v.patient.exams.$table.DataTable();
                            v.patient.medicines.$table.DataTable();
                            v.patient.timeline.$table.DataTable();
                            $button.removeClass("loading");
                            v.patient.$modal.modal("show");
                        },
                        error: function () {
                            console.error("Unable to get Patient");
                        }
                    });
                }
            });

            // === REPORT ===
            // Accordion
            v.report.executor.$accordion.accordion({
                animateChildren: false
            });
            // Modal
            v.report.$modal.modal({
                allowMultiple: true,
                closable: false,
                inverted: true,
                onShow: function () {
                    v.patient.$modal.addClass("disabled");
                },
                onHide: function () {
                    v.patient.$modal.removeClass("disabled");
                }
            });
        }
    },
    initToDo: function (writeService) {
        window.visit_creator.check();
        if (window.visit_creator.v.writeReport) {
            console.warn("Visit Creator To Do is already initialized");
        } else {
            if (!writeService) throw "Write Service URL is not defined";

            const v = window.visit_creator.v.writeReport = {
                $modal: $(".visit-creator-todo-write-report-modal"),
                $button: $("button.visit-creator-todo-write-report-modal-button"),
                $form: $("form.visit-creator-todo-write-report-form"),
                patient: {
                    $fullName: $("span.visit-creator-todo-write-report-patient-full-name"),
                    $fiscalCode: $("span.visit-creator-todo-write-report-patient-fiscal-code"),
                    $avatar: $(".visit-creator-todo-write-report-patient-avatar")
                },
                data: {
                    $content: $("textarea.visit-creator-todo-write-report-data-content"),
                    $contentLength: $("span.visit-creator-todo-write-report-data-content-length"),
                    $paid: $(".visit-creator-todo-write-report-data-paid"),
                    $examsDropdown: $(".visit-creator-todo-write-report-data-exams"),
                    $medicinesDropdown: $(".visit-creator-todo-write-report-data-medicines")
                },
                populate: {
                    report: function (patient) {
                        if (patient !== null && patient !== undefined) {
                            v.patient.$fullName.html(patient.fullNameCapitalized);
                            v.patient.$fiscalCode.html(patient.fiscalCode);
                            v.patient.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));
                        }
                    }
                }
            };

            const i18n = visit_creator.v.i18n;

            // The Object that represent a new Report
            const newReport = {
                prescriptionId: undefined,
                content: undefined,
                paid: false,
                exams: [],
                medicines: []
            };

            // Modal
            v.$modal.modal({
                allowMultiple: false,
                closable: true,
                inverted: true
            });
            // Count chars
            v.data.$content.on("keyup", function () {
                v.data.$contentLength.html(v.data.$content.attr("maxLength") - this.value.length);
            });
            // Checkbox
            v.data.$paid.checkbox({
                onChecked: function () {
                    const label = v.data.$paid.find("label");
                    label.html(label.data("paid"));
                },
                onUnchecked: function () {
                    const label = v.data.$paid.find("label");
                    label.html(label.data("paid-not"));
                }
            });
            // Dropdown
            v.data.$examsDropdown.dropdown({
                clearable: true,
                allowAdditions: false
            });
            v.data.$medicinesDropdown.dropdown({
                clearable: true,
                allowAdditions: false
            });
            // Triggers Modal
            v.$button.click(function () {
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
                        v.$form.removeClass("disabled success warning");
                        v.data.$content.val("");
                        v.data.$paid.checkbox("enable");
                        v.data.$paid.checkbox("uncheck");
                        v.data.$contentLength.html(v.data.$content.attr("maxLength"));
                        v.data.$examsDropdown.dropdown("refresh");
                        v.data.$examsDropdown.dropdown("clear");
                        v.data.$medicinesDropdown.dropdown("refresh");
                        v.data.$medicinesDropdown.dropdown("clear");

                        $.ajax({
                            type: "GET",
                            url: window.UTIL.STRING.format(window.visit_creator.v.service.patient, patientId),
                            success: function (data) {
                                v.populate.report(data);
                                // Disable Paid checkbox if it's already paid
                                $.each(data.exams, function (index, element) {
                                    if (element.id === prescriptionId) {
                                        if (element.paid) {
                                            v.data.$paid.checkbox("check");
                                            v.data.$paid.checkbox("disable");
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

                    v.$modal.modal("show");
                    $button.removeClass("loading");
                } else {
                    console.error("Prescription Id or Patient Id is not a Number");
                }
            });

            // Form
            v.$form.form({
                fields: {
                    content: {
                        identifier: "content",
                        rules: [
                            {
                                type: "empty",
                                prompt: i18n.newReport.empty
                            },
                            {
                                type: `minLength[${v.data.$content.attr("minlength")}]`,
                                prompt: i18n.newReport.minLength
                            },
                            {
                                type: `maxLength[${v.data.$content.attr("maxlength")}]`,
                                prompt: i18n.newReport.maxLength
                            }
                        ]
                    },
                    paid: {
                        identifier: "paid",
                        rules: [
                            {
                                type: "checked",
                                prompt: i18n.newReport.unpaid
                            }
                        ]
                    }
                },
                onSuccess: function () {
                    // Show Loading
                    v.$form.removeClass("warning success").addClass("loading");

                    // Reset
                    newReport.exams = [];
                    newReport.medicines = [];
                    // Dropdown Values
                    const examsArray = v.data.$examsDropdown.dropdown("get value").split(",");
                    const medicinesArray = v.data.$medicinesDropdown.dropdown("get value").split(",");

                    // Set newReport obj template
                    if (examsArray.length !== 0 && examsArray[0] !== "") {
                        newReport.exams = examsArray;
                    }
                    if (medicinesArray.length !== 0 && medicinesArray[0] !== "") {
                        newReport.medicines = medicinesArray;
                    }
                    // Others
                    newReport.content = v.data.$content.val();
                    newReport.paid = v.data.$paid.checkbox("is checked");

                    // Send newReport
                    $.ajax({
                        type: "POST",
                        url: writeService,
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(newReport),
                        success: function (data) {
                            v.$form.removeClass("loading");
                            if (data.error === 0) {
                                // Added successfully
                                v.$form.addClass("disabled success");
                                // Unused row
                                window.visit_creator.v.$table
                                    .DataTable()
                                    .row(window.visit_creator.v.$table.find(`tbody tr[data-prescription-id="${newReport.prescriptionId}"]`))
                                    .remove()
                                    .draw();
                            } else {
                                v.$form.addClass("warning");
                            }
                        },
                        error: function () {
                            console.error("Unable to write a Report");
                        }
                    });

                    return false;
                }
            });
        }
    },
    initDone: function () {
        window.visit_creator.check();
        if (window.visit_creator.v.done) {
            console.warn("Visit Creator Done is already initialized");
        } else {
            const v = window.visit_creator.v.done = {
                $button: $("button.visit-creator-done-view-report-modal-button")
            };

            // Button Event trigger
            v.$button.click(function () {
                const $button = $(this);
                const prescriptionId = $button.data("prescription-id");

                if (window.UTIL.NUMBER.isNumber(prescriptionId)) {
                    $button.addClass("loading");
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(window.visit_creator.v.service.prescriptionExam, prescriptionId),
                        success: function (data) {
                            window.visit_creator.populate.report(data);
                            $button.removeClass("loading");
                            window.visit_creator.v.report.$modal.modal("show");
                        },
                        error: function () {
                            console.error("Unable to get Prescription Exam");
                        }
                    });
                }
            });
        }
    },
    populate: {
        /**
         * Populate the Doctor
         * @param doctor The Doctor obj
         */
        doctor: function (doctor) {
            window.visit_creator.check();
            const template = window.visit_creator.v.doctor;

            if (doctor !== null && doctor !== undefined) {
                template.$id.html(doctor.id);
                template.$fullName.html(doctor.fullNameCapitalized);
                template.$province.html(doctor.city.province.nameLongCapitalized);
                template.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", doctor.avatar.nameAsResource));
            }
        },
        /**
         * Populate the Executor
         * @param executor The Executor data
         */
        executor: function (executor) {
            window.visit_creator.check();
            const template = window.visit_creator.v.executor;

            if (executor !== null && executor !== undefined) {
                template.$message.html(executor.message);
                template.$id.html(executor.id);
                template.$fullName.html(executor.fullName);
                template.$territory.html(executor.territory);
                template.$avatar.attr("src", executor.avatar);
            }
        },
        /**
         * Populate the Patient
         * @param patient The Patient obj
         */
        patient: function (patient) {
            window.visit_creator.check();
            const template = window.visit_creator.v.patient;
            const reportTemplate = window.visit_creator.v.report;
            const i18n = window.visit_creator.v.i18n;

            if (patient !== null && patient !== undefined) {
                // Caching the Prescription Exams
                const pExamCache = {};

                // === Personal Information ===
                template.personalInfomation.$id.html(patient.id);
                template.personalInfomation.$name.html(patient.name);
                template.personalInfomation.$surname.html(patient.surname);
                template.personalInfomation.$fullName.html(patient.fullNameCapitalized);
                template.personalInfomation.$sex.html(patient.sex.sex);
                template.personalInfomation.$fiscalCode.html(patient.fiscalCode);
                template.personalInfomation.$birthDate.html(moment(patient.birthDate).format("ll"));
                template.personalInfomation.$birthCity.html(patient.birthCity.name);
                template.personalInfomation.$domicile.html(patient.city.name);
                template.personalInfomation.$avatar.attr("src", window.UTIL.JSF.toResourceURL("_default", patient.avatar.nameAsResource));

                // === EXAMS ===
                if (!patient.exams || patient.exams.length === 0) {
                    // Empty Exams
                    template.exams.$emptyMessage.removeClass("hidden");
                    template.exams.$table.parents("div.dataTables_wrapper").first().hide();
                } else {
                    template.exams.$emptyMessage.addClass("hidden");
                    template.exams.$table.parents("div.dataTables_wrapper").first().show();
                    // Clean Table
                    template.exams.$table.find("tbody").empty();
                    // Populate table
                    $.each(patient.exams, function (index, element) {
                        // Cache exams if Report is present
                        if (element.report) {
                            pExamCache[element.id] = element;
                        }

                        let tr = `<tr>` +
                            `<td>${element.id}</td>` +
                            `<td>${element.exam.name}</td>` +
                            `<td>${element.dateTime ? moment(element.dateTime).format("ll") : "-"}</td>` +
                            `<td>${element.dateTime ? moment(element.dateTime).format("LTS") : "-"}</td>` +
                            `<td class="${element.report ? 'positive' : 'disabled'}">`;

                        if (!element.report) {
                            tr += `<i class="icon close"/>` +
                                `${i18n.noReport}`;
                        } else {
                            tr += `<button class="ui fluid button ${reportTemplate.buttonClass}" data-prescription-id="${element.id}">` +
                                `<i class="shuffle icon"/>` +
                                `${i18n.view}` +
                                `</button>`;
                        }
                        tr += `</td></tr>`;
                        template.exams.$table.find("tbody").append(tr);
                    });
                    // Attach Events on Report Button
                    $(`button.${reportTemplate.buttonClass}`).click(function () {
                        const $button = $(this);
                        const prescription = pExamCache[$button.data("prescription-id")];

                        if (prescription !== null && prescription !== undefined) {
                            $button.addClass("loading");
                            // Report exists
                            window.visit_creator.populate.report(prescription);
                            $button.removeClass("loading");
                            // Show Modal
                            reportTemplate.$modal.modal("show");
                        }
                    });
                }

                // === MEDICINES ===
                if (!patient.medicines || patient.medicines.length === 0) {
                    // Empty Medicines
                    template.medicines.$emptyMessage.removeClass("hidden");
                    template.medicines.$table.parents("div.dataTables_wrapper").first().hide();
                } else {
                    template.medicines.$emptyMessage.addClass("hidden");
                    template.medicines.$table.parents("div.dataTables_wrapper").first().show();
                    // Clean Table
                    template.medicines.$table.find("tbody").empty();
                    // Populate Table
                    $.each(patient.medicines, function (index, element) {
                        const tr = `<tr>` +
                            `<td>${element.id}</td>` +
                            `<td>${element.medicine.name}</td>` +
                            `<td>${moment(element.dateTime).format("ll")}</td>` +
                            `<td>${moment(element.dateTime).format("LTS")}</td>` +
                            `<td>${element.quantity}</td>` +
                            `</tr>`;
                        template.medicines.$table.find("tbody").append(tr);
                    });
                }

                // === TIMELINE ===
                if (!patient.avatarHistory || patient.avatarHistory.length === 0) {
                    // Empty Timeline
                    template.timeline.$emptyMessage.removeClass("hidden");
                    template.timeline.$table.parents("div.dataTables_wrapper").first().hide();
                } else {
                    template.timeline.$emptyMessage.addClass("hidden");
                    template.timeline.$table.parents("div.dataTables_wrapper").first().show();
                    // Clean Table
                    template.timeline.$table.find("tbody").empty();
                    // Populate Table
                    $.each(patient.avatarHistory, function (index, element) {
                        const tr = `<tr class="center aligned ${patient.avatar.id === element.id ? 'text-strong' : 'disabled'}">` +
                            `<td>` +
                            `<img src="${window.UTIL.JSF.toResourceURL('_default', element.nameAsResource)}" alt="Avatar" class="ui image centered bordered rounded person-avatar tiny middle aligned"/>` +
                            `&#160;&#160;` +
                            `<i class="${patient.avatar.id === element.id ? 'checkmark green' : ''} icon"/>` +
                            `</td>` +
                            `<td>${moment(element.upload).format("ll")}</td>` +
                            `<td>${moment(element.upload).format("LTS")}</td>` +
                            `</tr>`;
                        template.timeline.$table.find("tbody").append(tr);
                    });
                }
            }
        },
        /**
         * Populate the Report
         * @param prescription The Prescription Exam obj
         */
        report: function (prescription) {
            window.visit_creator.check();
            const template = window.visit_creator.v.report;

            if (prescription !== null && prescription !== undefined && prescription.report) {
                const report = prescription.report;

                template.$id.html(report.id);
                template.$date.html(moment(report.dateTime).format("ll"));
                template.$time.html(moment(report.dateTime).format("LTS"));
                template.$content.val(report.content);
                visit_creator.populate.reportExecutor(prescription);

                if (!report.exams || report.exams.length === 0) {
                    // Empty Exams
                    template.exam.$emptyMessage.removeClass("hidden");
                    template.exam.$container.addClass("hidden");
                } else {
                    template.exam.$emptyMessage.addClass("hidden");
                    template.exam.$container.removeClass("hidden");
                    // Clean List
                    template.exam.$list.empty();
                    // Populate List
                    $.each(report.exams, function (index, element) {
                        const item = `<div class="item">${element.name}</div>`;
                        template.exam.$list.append(item);
                    });
                }

                if (!report.medicines || report.medicines.length === 0) {
                    // Empty Medicines
                    template.medicine.$emptyMessage.removeClass("hidden");
                    template.medicine.$container.addClass("hidden");
                } else {
                    template.medicine.$emptyMessage.addClass("hidden");
                    template.medicine.$container.removeClass("hidden");
                    // Clean List
                    template.medicine.$list.empty();
                    // Populate List
                    $.each(report.medicines, function (index, element) {
                        const item = `<div class="item">${element.name}</div>`;
                        template.medicine.$list.append(item);
                    });
                }
            }
        },
        /**
         * Populate the Report Executor
         * @param prescription The Prescription Exam obj
         */
        reportExecutor: function (prescription) {
            window.visit_creator.check();
            const template = window.visit_creator.v.report.executor;
            const i18n = window.visit_creator.v.i18n;
            const executor = {
                message: undefined,
                id: undefined,
                fullName: undefined,
                territory: undefined,
                avatar: undefined,
                populate: function () {
                    template.$message.html(executor.message);
                    template.$id.html(executor.id);
                    template.$fullName.html(executor.fullName);
                    template.$territory.html(executor.territory);
                    template.$avatar.attr("src", executor.avatar);
                }
            };

            if (prescription !== null && prescription !== undefined) {
                // Executor
                if (!prescription.exam.supported && prescription.specialistId) {
                    // Executor is a Specialist
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(visit_creator.v.service.specialist, prescription.specialistId),
                        success: function (data) {
                            executor.message = i18n.executorIsSpecialist;
                            executor.id = data.id;
                            executor.fullName = data.fullNameCapitalized;
                            executor.territory = data.city.nameCapitalized;
                            executor.avatar = window.UTIL.JSF.toResourceURL("_default", data.avatar.nameAsResource);
                            executor.populate();
                        },
                        error: function () {
                            console.error("Unable to get Specialist");
                        }
                    });
                } else if (prescription.exam.supported && prescription.healthServiceId) {
                    // Executor is the Health Service
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(visit_creator.v.service.healthService, prescription.healthServiceId),
                        success: function (data) {
                            executor.message = i18n.executorIsHealthService;
                            executor.id = data.id;
                            executor.fullName = data.province.nameLongCapitalized;
                            executor.territory = data.province.region.nameCapitalized;
                            executor.avatar = window.UTIL.JSF.toResourceURL("_default", data.crestAsResource);
                            executor.populate();
                        },
                        error: function () {
                            console.error("Unable to get Health Service");
                        }
                    });
                } else {
                    // Error
                    console.error("Found Report without any executor");
                }
            }
        }
    }
};
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
                $table: $("#visit-assigned-table"),
                i18n: $("#visit-assigned-i18n").data("i18n"),
                patient: {
                    urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
                    $modal: $("#visit-assigned-patient-modal"),
                    $button: $("#visit-assigned-table button.patient-modal-button"),
                    $accordion: $("#patient-accordion"),
                    personalInfomation: {
                        $id: $("span#patient-id"),
                        $name: $("span#patient-name"),
                        $surname: $("span#patient-surname"),
                        $fullName: $("span#patient-full-name"),
                        $sex: $("span#patient-sex"),
                        $fiscalCode: $("span#patient-fiscal-code"),
                        $birthDate: $("span#patient-birth-date"),
                        $birthCity: $("span#patient-birth-city"),
                        $domicile: $("span#patient-domicile"),
                        $avatar: $("img#patient-avatar")
                    },
                    exams: {
                        $table: $("table#patient-exams-table"),
                        buttonClass: "button.patient-modal-report-button"
                    },
                    medicines: {
                        $emptyMessage: $("#patient-medicines-empty"),
                        $table: $("table#patient-medicines-table")
                    },
                    timeline: {
                        $emptyMessage: $("#patient-timeline-empty"),
                        $table: $("table#patient-timeline-table")
                    }
                },
                doctor: {
                    urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
                    $modal: $("#visit-assigned-doctor-modal"),
                    $button: $("#visit-assigned-table button.doctor-modal-button"),
                    $id: $("#doctor-id"),
                    $fullName: $("#doctor-full-name"),
                    $fullNameHeader: $("#doctor-full-name-header"),
                    $province: $("#doctor-province"),
                    $avatar: $("#doctor-avatar")
                },
                report: {
                    $modal: $("#visit-assigned-patient-report-modal"),
                    buttonClass: "button.patient-modal-report-button",
                    $idHeader: $("span#patient-report-id-header"),
                    $id: $("span#patient-report-id"),
                    $idSpecialist: $("span#patient-report-specialist-id"),
                    $date: $("span#patient-report-date"),
                    $time: $("span#patient-report-time"),
                    $content: $("textarea#patient-report-content"),
                    exam: {
                        $emptyMessage: $("#patient-report-exam-empty"),
                        $container: $("#patient-report-exam-container"),
                        $list: $("#patient-report-exam-list")
                    },
                    medicine: {
                        $emptyMessage: $("#patient-report-medicine-empty"),
                        $container: $("#patient-report-medicine-container"),
                        $list: $("#patient-report-medicine-list")
                    }
                }
            };


            // Set moment correct locale
            window.moment.locale(window.navigator.userLanguage || window.navigator.language);

            // Enable table sorting
            v.$table.tablesort();

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
                        url: window.UTIL.STRING.format(v.doctor.urlPattern, doctorId),
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
            // Table Sorting
            v.patient.exams.$table.tablesort();
            v.patient.medicines.$table.tablesort();
            v.patient.timeline.$table.tablesort();
            // Populate
            v.patient.$button.click(function () {
                const $button = $(this);
                const patientId = $button.data("patient-id");

                if (window.UTIL.NUMBER.isNumber(patientId)) {
                    $button.addClass("loading");
                    $.ajax({
                        type: "GET",
                        url: window.UTIL.STRING.format(v.patient.urlPattern, patientId),
                        success: function (data) {
                            window.visit_creator.populate.patient(data);
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
                template.$fullNameHeader.html(doctor.fullNameCapitalized);
                template.$province.html(doctor.city.province.nameLongCapitalized);
                template.$avatar.attr("src", window.UTIL.JSF.resourceURL("_default", doctor.avatar.nameAsResource));
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
                template.personalInfomation.$avatar.attr("src", window.UTIL.JSF.resourceURL("_default", patient.avatar.nameAsResource));

                // === EXAMS ===
                // Clean Table
                template.exams.$table.find("tbody").empty();
                // Populate table
                $.each(patient.exams, function (index, element) {
                    // Cache exams if Report is present
                    if (element.report) {
                        pExamCache[element.id] = element;
                    }

                    let tr = `<tr>` +
                        `<td>${element.exam.name}</td>` +
                        `<td>${element.dateTime ? moment(element.dateTime).format("ll") : "-"}</td>` +
                        `<td>${element.dateTime ? moment(element.dateTime).format("LTS") : "-"}</td>` +
                        `<td class="${element.report ? 'positive' : 'disabled'}">`;

                    if (!element.report) {
                        tr += `<i class="icon close"/>` +
                            `${i18n.noReport}`;
                    } else {
                        tr += `<button class="ui fluid button patient-modal-report-button" data-prescription-id="${element.id}">` +
                            `<i class="shuffle icon"/>` +
                            `${i18n.view}` +
                            `</button>`;
                    }
                    tr += `</td></tr>`;
                    template.exams.$table.find("tbody").append(tr);
                });
                // Attach Events on Report Button
                $(template.exams.buttonClass).click(function () {
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

                // === MEDICINES ===
                if (patient.medicines && patient.medicines.length === 0) {
                    // Empty Medicines
                    template.medicines.$emptyMessage.removeClass("hidden");
                    template.medicines.$table.addClass("hidden");
                } else {
                    template.medicines.$emptyMessage.addClass("hidden");
                    template.medicines.$table.removeClass("hidden");
                    // Clean Table
                    template.medicines.$table.find("tbody").empty();
                    // Populate Table
                    $.each(patient.medicines, function (index, element) {
                        const tr = `<tr>` +
                            `<td>${element.medicine.name}</td>` +
                            `<td>${moment(element.dateTime).format("ll")}</td>` +
                            `<td>${moment(element.dateTime).format("LTS")}</td>` +
                            `<td>${element.quantity}</td>` +
                            `</tr>`;
                        template.medicines.$table.find("tbody").append(tr);
                    });
                }

                // === TIMELINE ===
                if (patient.avatarHistory && patient.avatarHistory.length === 0) {
                    // Empty Timeline
                    template.timeline.$emptyMessage.removeClass("hidden");
                    template.timeline.$table.addClass("hidden");
                } else {
                    template.timeline.$emptyMessage.addClass("hidden");
                    template.timeline.$table.removeClass("hidden");
                    // Clean Table
                    template.timeline.$table.find("tbody").empty();
                    // Populate Table
                    $.each(patient.avatarHistory, function (index, element) {
                        const tr = `<tr class="center aligned ${patient.avatar.id === element.id ? 'text-strong' : 'disabled'}">` +
                            `<td>` +
                            `<img src="${window.UTIL.JSF.resourceURL('_default', element.nameAsResource)}" alt="Avatar" class="ui image centered bordered rounded person-avatar tiny middle aligned"/>` +
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
         * @param prescription The Report obj
         */
        report: function (prescription) {
            window.visit_creator.check();
            const template = window.visit_creator.v.report;
            const i18n = window.visit_creator.v.i18n;

            if (prescription !== null && prescription !== undefined && prescription.report) {
                const report = prescription.report;

                template.$idHeader.html(report.id);
                template.$id.html(report.id);
                template.$idSpecialist.html(prescription.specialistId);
                template.$date.html(moment(report.dateTime).format("ll"));
                template.$time.html(moment(report.dateTime).format("LTS"));
                template.$content.val(report.content);

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
        }
    }
};
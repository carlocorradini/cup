"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Assigned Visit Functionality
    visitAssignedConfig();
});

function visitAssignedConfig() {
    const visit = {
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
                $table: $("table#patient-exams-table")
            },
            medicines: {
                $emptyMessage: $("#patient-medicines-empty"),
                $table: $("table#patient-medicines-table")
            },
            timeline: {
                $emptyMessage: $("#patient-timeline-empty"),
                $table: $("table#patient-timeline-table")
            },
            report: {
                $modal: $("#visit-assigned-patient-report-modal"),
                buttonClass: "button.patient-modal-report-button",
                $idHeader: $("span#patient-report-id-header"),
                $id: $("span#patient-report-id"),
                $idSpecialist: $("span#patient-report-specialist-id"),
                $date: $("span#patient-report-date"),
                $time: $("span#patient-report-time"),
                $content: $("span#patient-report-content"),
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
        },
        doctor: {
            urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
            $modal: $("#visit-assigned-doctor-modal"),
            $button: $("#visit-assigned-table button.doctor-modal-button"),
            $id: $("#doctor-id"),
            $fullName: $("#doctor-full-name"),
            $fullNameHeader: $("#doctor-full-name-header"),
            $avatar: $("#doctor-avatar")
        },
        writeReport: {
            $modal: $("#visit-assigned-write-report-modal"),
            $button: $("#visit-assigned-table button.write-report-modal-button"),
            patient: {
                urlPattern: window.CONTEXT_PATH + "/service/restricted/medical/patient/{1}",
                $fullName: $("#write-report-patient-full-name"),
                $fiscalCode: $("#write-report-patient-fiscal-code"),
                $avatar: $("#write-report-patient-avatar")
            },
            data: {
                $content: $("#write-report-data-content"),
                $examsDropdown: $("#write-report-data-exams"),
                $medicinesDropdown: $("#write-report-data-medicines")
            }
        }
    };

    // Set Moment correct Locale
    moment.locale(window.navigator.userLanguage || window.navigator.language);

    // Enable Table Sorting
    visit.$table.tablesort();

    // === WRITE REPORT ===
    // Modal
    visit.writeReport.$modal.modal({
        inverted: true
    });
    // Dropdown
    visit.writeReport.data.$examsDropdown.dropdown({
        clearable: true,
        allowAdditions: false
    });
    visit.writeReport.data.$medicinesDropdown.dropdown({
        clearable: true,
        allowAdditions: false
    });

    // Triggers Modal
    visit.writeReport.$button.click(function () {
        const $button = $(this);
        const prescriptionId = $button.data("prescription-id");
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(prescriptionId)
            && window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(visit.writeReport.patient.urlPattern, patientId),
                success: function (data) {
                    populateWriteReportModal(visit.writeReport, data);
                    visit.writeReport.$modal.modal("show");
                    $button.removeClass("loading");
                },
                error: function () {
                    console.error("Unable to get Patient");
                }
            });
        } else {
            throw "Prescription Id or Patient Id is not a Number";
        }
    });

    // === DOCTOR ===
    // Modal
    visit.doctor.$modal.modal({
        inverted: true
    });
    // Populate Modal with information retrieving
    visit.doctor.$button.click(function () {
        const $button = $(this);
        const doctorId = $button.data("doctor-id");

        if (window.UTIL.NUMBER.isNumber(doctorId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(visit.doctor.urlPattern, doctorId),
                success: function (data) {
                    populateDoctorModal(visit.doctor, data);
                    $button.removeClass("loading");
                    visit.doctor.$modal.modal("show");
                },
                error: function () {
                    console.error("Unable to get Doctor");
                }
            });
        }
    });

    // === PATIENT ===
    // Modal
    visit.patient.$modal.modal({
        allowMultiple: true,
        closable: false,
        inverted: false
    });
    // Report Modal
    visit.patient.report.$modal.modal({
        allowMultiple: true,
        closable: false,
        inverted: true,
        onShow: function () {
            visit.patient.$modal.addClass("disabled");
        },
        onHide: function () {
            visit.patient.$modal.removeClass("disabled");
        }
    });
    // Accordion
    visit.patient.$accordion.accordion({
        animateChildren: false
    });
    // Table Sorting
    visit.patient.exams.$table.tablesort();
    visit.patient.medicines.$table.tablesort();
    visit.patient.timeline.$table.tablesort();
    // Populate Modal with information retrieving
    visit.patient.$button.click(function () {
        const $button = $(this);
        const patientId = $button.data("patient-id");

        if (window.UTIL.NUMBER.isNumber(patientId)) {
            $button.addClass("loading");
            $.ajax({
                type: "GET",
                url: window.UTIL.STRING.format(visit.patient.urlPattern, patientId),
                success: function (data) {
                    populatePatientModal(visit.patient, data, visit.i18n);
                    $button.removeClass("loading");
                    visit.patient.$modal.modal("show");
                },
                error: function () {
                    console.error("Unable to get Patient");
                }
            });
        }
    });
}

// Populate the Modal Write Report given a Template and a Patient
function populateWriteReportModal(writeReportTemplate, patient) {
    if (writeReportTemplate !== null && writeReportTemplate !== undefined
        && patient !== null && patient !== undefined) {
        writeReportTemplate.patient.$fullName.html(patient.fullNameCapitalized);
        writeReportTemplate.patient.$fiscalCode.html(patient.fiscalCode);
        writeReportTemplate.patient.$avatar.attr("src", window.UTIL.JSF.resourceURL("_default", patient.avatar.nameAsResource));
    }
}

// Populate the Modal Doctor given a Template and a Doctor
function populateDoctorModal(doctorTemplate, doctor) {
    if (doctorTemplate !== null && doctorTemplate !== undefined
        && doctor !== null && doctor !== undefined) {
        doctorTemplate.$id.html(doctor.id);
        doctorTemplate.$fullName.html(doctor.fullNameCapitalized);
        doctorTemplate.$fullNameHeader.html(doctor.fullNameCapitalized);
        doctorTemplate.$avatar.attr("src", window.UTIL.JSF.resourceURL("_default", doctor.avatar.nameAsResource));
    }
}

// Populate the Modal Patient given a Template and a Patient
function populatePatientModal(patientTemplate, patient, i18n) {
    if (patientTemplate !== null && patientTemplate !== undefined
        && patient !== null && patient !== undefined) {
        // === Report Cache ===
        // Caching the Report in a Set, populate in Exams sections
        const reportCache = {};

        // === Personal Information ===
        patientTemplate.personalInfomation.$id.html(patient.id);
        patientTemplate.personalInfomation.$name.html(patient.name);
        patientTemplate.personalInfomation.$surname.html(patient.surname);
        patientTemplate.personalInfomation.$fullName.html(patient.fullNameCapitalized);
        patientTemplate.personalInfomation.$sex.html(patient.sex.sex);
        patientTemplate.personalInfomation.$fiscalCode.html(patient.fiscalCode);
        patientTemplate.personalInfomation.$birthDate.html(moment(patient.birthDate).format("ll"));
        patientTemplate.personalInfomation.$birthCity.html(patient.birthCity.name);
        patientTemplate.personalInfomation.$domicile.html(patient.city.name);
        patientTemplate.personalInfomation.$avatar.attr("src", window.UTIL.JSF.resourceURL("_default", patient.avatar.nameAsResource));

        // === Exams ===
        // Clean Table
        patientTemplate.exams.$table.find("tbody").empty();
        // Populate table
        $.each(patient.exams, function (index, element) {
            // Populate Report Cache if present
            if (element.report) {
                element.report.specialistId = element.specialistId;
                reportCache[element.report.id] = element.report;
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
                tr += `<button class="ui fluid button patient-modal-report-button" data-report-id="${element.report.id}">` +
                    `<i class="shuffle icon"/>` +
                    `${i18n.view}` +
                    `</button>`;
            }
            tr += `</td></tr>`;
            patientTemplate.exams.$table.find("tbody").append(tr);
        });
        // Attach Events on Report Button
        $(patientTemplate.report.buttonClass).click(function () {
            const $button = $(this);
            const report = reportCache[$button.data("report-id")];

            if (report !== null && report !== undefined) {
                $button.addClass("loading");
                // Report exists
                populatePatientReportModal(patientTemplate.report, report);
                $button.removeClass("loading");
                // Show Modal
                patientTemplate.report.$modal.modal("show");
            }
        });

        // === Medicines ===
        if (patient.medicines.length === 0) {
            // Empty Medicines
            patientTemplate.medicines.$emptyMessage.removeClass("hidden");
            patientTemplate.medicines.$table.addClass("hidden");
        } else {
            patientTemplate.medicines.$emptyMessage.addClass("hidden");
            patientTemplate.medicines.$table.removeClass("hidden");
            // Clean Table
            patientTemplate.medicines.$table.find("tbody").empty();
            // Populate Table
            $.each(patient.medicines, function (index, element) {
                const tr = `<tr>` +
                    `<td>${element.medicine.name}</td>` +
                    `<td>${moment(element.dateTime).format("ll")}</td>` +
                    `<td>${moment(element.dateTime).format("LTS")}</td>` +
                    `<td>${element.quantity}</td>` +
                    `</tr>`;
                patientTemplate.medicines.$table.find("tbody").append(tr);
            });
        }

        // === Timeline ===
        if (patient.avatarHistory.length === 0) {
            // Empty Timeline
            patientTemplate.timeline.$emptyMessage.removeClass("hidden");
            patientTemplate.timeline.$table.addClass("hidden");
        } else {
            patientTemplate.timeline.$emptyMessage.addClass("hidden");
            patientTemplate.timeline.$table.removeClass("hidden");
            // Clean Table
            patientTemplate.timeline.$table.find("tbody").empty();
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
                patientTemplate.timeline.$table.find("tbody").append(tr);
            });
        }
    }
}

// Populate the Modal Patient Report given a Template and a Report
function populatePatientReportModal(patientReportTemplate, report) {
    if (patientReportTemplate !== null && patientReportTemplate !== undefined
        && report !== null && report !== undefined) {
        patientReportTemplate.$idHeader.html(report.id);
        patientReportTemplate.$id.html(report.id);
        patientReportTemplate.$idSpecialist.html(report.specialistId);
        patientReportTemplate.$date.html(moment(report.dateTime).format("ll"));
        patientReportTemplate.$time.html(moment(report.dateTime).format("LTS"));
        patientReportTemplate.$content.html(report.content);

        if (report.exams.length === 0) {
            // Empty Exams
            patientReportTemplate.exam.$emptyMessage.removeClass("hidden");
            patientReportTemplate.exam.$container.addClass("hidden");
        } else {
            patientReportTemplate.exam.$emptyMessage.addClass("hidden");
            patientReportTemplate.exam.$container.removeClass("hidden");
            // Clean List
            patientReportTemplate.exam.$list.empty();
            // Populate List
            $.each(report.exams, function (index, element) {
                const item = `<div class="item">${element.name}</div>`;
                patientReportTemplate.exam.$list.append(item);
            });
        }

        if (report.medicines.length === 0) {
            // Empty Medicines
            patientReportTemplate.medicine.$emptyMessage.removeClass("hidden");
            patientReportTemplate.medicine.$container.addClass("hidden");
        } else {
            patientReportTemplate.medicine.$emptyMessage.addClass("hidden");
            patientReportTemplate.medicine.$container.removeClass("hidden");
            // Clean List
            patientReportTemplate.medicine.$list.empty();
            // Populate List
            $.each(report.medicines, function (index, element) {
                const item = `<div class="item">${element.name}</div>`;
                patientReportTemplate.medicine.$list.append(item);
            });
        }
    }
}
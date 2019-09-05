"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Doctor Specialist Dashboard Functionality
    dashboardDoctorSpecialistConfig();
});

function dashboardDoctorSpecialistConfig() {
    const dashboard = {
        $tab: $("#dashboard-doctor-specialist-tab").find(".item"),
        qualification: {
            $table: $("#dashboard-doctor-specialist-qualification-table")
        }
    };

    // ENABLE TAB
    dashboard.$tab.tab();

    // QUALIFICATION TAB
    dashboard.qualification.$table.DataTable({
        "aaSorting": []
    });
}
"use strict";

// === PAGE READY ===
$(document).ready(() => {
    // Enable Write Report Functionality
    window.visit_creator.init();
    window.visit_creator.initToDo(window.CONTEXT_PATH + "/service/restricted/health_service/report");
});
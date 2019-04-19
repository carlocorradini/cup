"use strict";

// === PAGE FULLY LOADED ===
function windowLoaded() {
    // Hide Preloader
    $(".preloader")
        .delay(350)
        .fadeOut("slow");
}
// === PAGE READY ===
function documentReady() {
    // Hide Noscript
    $("noscript").hide();
}

// === START ===
$(window).on("load", windowLoaded);
$(document).ready(documentReady);

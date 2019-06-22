"use strict";

const $window = $(window);
const $document = $(document);

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

    // Enable Dropdown
    $(".ui.dropdown").dropdown();

    // Enable Sidebar Functionality
    sidebarHandler();
}

// === START ===
$window.on("load", windowLoaded);
$document.ready(documentReady);

function sidebarHandler() {
    const sidebar = {
        sidebar: $("#sidebar-menu"),
        button: $("#sidebar-menu-button"),
        avatar: {
            input: $("#sidebar-avatar-input"),
            image: $("#sidebar-avatar-image"),
            save: $("#sidebar-avatar-save"),
            error: $("#sidebar-avatar-error")
        }
    };
    const maxFileSize = parseInt(sidebar.avatar.input.data("max-size"), 10);
    let newAvatar = null;

    // Enable Sidebar
    sidebar.button.click(function () {
        sidebar.sidebar
            .sidebar("setting", "transition", "overlay")
            .sidebar("toggle");
    });
    // Close on ESC key
    $document.keyup(function (e) {
        if (e.key === "Escape") sidebar.sidebar.sidebar("hide");
    });

    // Avatar Upload local
    sidebar.avatar.input.change(function () {
        if (this.files && this.files[0]) {
            if (this.files[0].size > maxFileSize) {
                sidebar.avatar.error.removeClass("hidden");
                sidebar.avatar.input.trigger("change");
            } else {
                const reader = new FileReader();
                newAvatar = this.files[0];

                sidebar.avatar.error.addClass("hidden");
                sidebar.avatar.save.find("i").addClass("hidden");
                sidebar.avatar.save.find(".save.icon").removeClass("hidden");

                reader.onload = function (e) {
                    sidebar.avatar.image.attr("src", e.target.result);
                    sidebar.avatar.save.removeClass("hidden");
                };
                reader.readAsDataURL(this.files[0]);
            }
        }
    });

    // Avatar Upload remote
    sidebar.avatar.save.click(function () {
        if (newAvatar !== null) {
            const formData = new FormData();
            formData.append("avatar", newAvatar);

            sidebar.avatar.save.find("i").addClass("hidden");
            sidebar.avatar.save.find(".circle.notched.icon").removeClass("hidden");

            $.ajax({
                url: window.CONTEXT_PATH + "/service/restricted/person/avatar",
                type: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    sidebar.avatar.save.find("i").addClass("hidden");
                    if (data.error !== 0) {
                        sidebar.avatar.save.find(".close.icon").removeClass("hidden");
                    } else {
                        sidebar.avatar.save.find(".check.icon").removeClass("hidden");
                        newAvatar = null;
                    }
                },
                error: function () {
                    sidebar.avatar.save.find("i").addClass("hidden");
                    sidebar.avatar.save.find(".close.icon").removeClass("hidden");
                }
            });
        }
    });
}

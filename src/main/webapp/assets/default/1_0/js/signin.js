"use strict";

function countDown(startTime, $target, link) {
    const newTime = startTime - 1;
    $target.html(startTime);

    if (newTime >= 0) {
        setTimeout(countDown, 1000, newTime, $target, link);
    } else {
        window.location.href = link;
    }
}

function signIn() {
    const $form = $("#sign-in-form");
    const $message = $("#sign-in-message");
    $form.form({
        fields: {
            username: {
                identifier: "username",
                rules: [
                    {
                        type: "empty",
                        prompt: "Username Obbligatorio"
                    },
                    {
                        type: "maxLength[50]",
                        prompt:
                            "Lunghezza massima username {ruleValue} caratteri"
                    }
                ]
            },
            password: {
                identifier: "password",
                rules: [
                    {
                        type: "empty",
                        prompt: "Password Obbligatoria"
                    },
                    {
                        type: "minLength[8]",
                        prompt:
                            "Lunghezza password minimo {ruleValue} caratteri"
                    },
                    {
                        type: "maxLength[50]",
                        prompt:
                            "Lunghezza massima password {ruleValue} caratteri"
                    }
                ]
            }
        },
        onSuccess() {
            $form.addClass("loading");
            $.ajax({
                type: "POST",
                url: "/servlet/auth",
                data: {
                    username: $form.find('input[name="username"]').val(),
                    password: $form.find('input[name="password"]').val()
                },
                cache: false,
                success(data) {
                    $form.removeClass("loading");

                    if (data.error !== null || data.error !== undefined) {
                        $message.removeClass("success").addClass("error");
                        $message
                            .find("i")
                            .removeClass()
                            .addClass("remove icon");
                        $message.find("span").html(data.message);
                    } else {
                        $form.find(".button.submit").addClass("disabled");
                        $message.removeClass("error").addClass("success");
                        $message
                            .find("i")
                            .removeClass()
                            .addClass("checkmark icon");
                        $message
                            .find("span")
                            .html(
                                `${data.message} - Redirect in <span></span>`
                            );
                        // Show Countdown and Redirect
                        countDown(
                            3,
                            $message.find("span span"),
                            window.location.href
                        );
                    }
                    console.log(`[AUTHENTICATION]: ${data.message}`);
                },
                error(jqXHR, status, error) {
                    $message.removeClass("success").addClass("error");
                    $message
                        .find("i")
                        .removeClass()
                        .addClass("exclamation triangle icon");
                    $message.find("span").html(`ERRORE: ${status} | ${error}`);
                    console.error(`[AUTHENTICATION]: "${status} ${error}`);
                }
            });
            return false;
        }
    });
}

$(document).ready(signIn());

"use strict";

$(document).ready(function () {
    const signIn = {
        i18n: $("#sign-in-i18n").data("i18n"),
        $form: $("#sign-in-form"),
        timeout: 1500,
        input: {
            $dropdownHS: $("#sign-in-dropdown"),
            $inputHiddenHS: $("#sign-in-hs-id"),
            $password: $("#sign-in-password"),
            $remember: $("#sign-in-remember")
        },
        error: {
            errorCode: $("#sign-in-error-code")
        }
    };
    const credentials = {
        id: undefined,
        password: undefined,
        remember: false
    };

    // Enable Dropdown
    signIn.input.$dropdownHS.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            signIn.input.$inputHiddenHS.val(value);
        }
    });

    //Enable Checkbox
    signIn.input.$remember.checkbox();

    // Form handler
    signIn.$form.form({
        fields: {
            health_service: {
                identifier: "health_service",
                rules: [
                    {
                        type: "empty",
                        prompt: signIn.i18n.hs.empty
                    }
                ]
            },
            password: {
                identifier: "password",
                rules: [
                    {
                        type: "empty",
                        prompt: signIn.i18n.password.empty
                    },
                    {
                        type: `minLength[${signIn.input.$password.attr("minlength")}]`,
                        prompt: signIn.i18n.password.minLength
                    },
                    {
                        type: `maxLength[${signIn.input.$password.attr("maxlength")}]`,
                        prompt: signIn.i18n.password.maxLength
                    }
                ]
            }
        },
        onSuccess: function () {
            signIn.$form.removeClass("success warning").addClass("loading");

            // Set Credentials
            credentials.id = parseInt(signIn.input.$dropdownHS.dropdown("get value"), 10);
            credentials.password = signIn.input.$password.val();
            credentials.remember = signIn.input.$remember.checkbox("is checked");

            // Send Credentials
            $.ajax({
                type: "POST",
                url: window.CONTEXT_PATH + "/service/open/health_service/signin",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(credentials),
                success: function (data) {
                    signIn.$form.removeClass("loading");
                    if (data.error !== 0) {
                        signIn.error.errorCode.html(data.error);
                        signIn.$form.addClass("warning");
                    } else {
                        signIn.$form.addClass("success");
                        // Redirect to Dashboard
                        setTimeout(function () {
                            window.location.href = window.UTIL.URL.getBaseUrl() + "dashboard/index.xhtml";
                        }, signIn.timeout);
                    }
                },
                error: function () {
                    console.error("Unable to Sign In");
                }
            });

            return false;
        }
    });
});
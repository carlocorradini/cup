"use strict";

$(document).ready(function () {
    const signIn = {
        i18n: $("#sign-in-i18n").data("i18n"),
        $form: $("#sign-in-form"),
        input: {
            $dropdownHS: $("#sign-in-dropdown"),
            $inputHiddenHS: $("#sign-in-hs-id"),
            $password: $("#sign-in-password")
        }
    };
    const credentials = {
        id: undefined,
        password: undefined
    };

    signIn.input.$dropdownHS.dropdown({
        clearable: true,
        onChange: function (value, text, $item) {
            signIn.input.$inputHiddenHS.val(value);
        }
    });

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


            return false;
        }
    });
});
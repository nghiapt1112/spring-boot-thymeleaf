$(document).ready(function () {
    var booleanEmailExisted;
    var emailExisted = $("#email").val();
    if ($("#error_exitsted").text().length === 0) {
        booleanEmailExisted = true;
    } else {
        booleanEmailExisted = false;
    }
    $("body").on("click", "#save", function (event) {
        var checkForm = true;
        var REGEX_EMAIL = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var STRING_EMPTY = " ";
        var email = $("#email").val();
        if (booleanEmailExisted) {
            if (emailExisted != email) {
                if (isEmpty(email) || isBlank(email)) {
                    $("#errorFormat").removeClass("error_show").addClass("error");
                    $("#error_exitsted").removeClass("error_show").addClass("error");
                    $("#errorEmail").removeClass("error").addClass("error_show");
                    checkForm = false;
                } else {
                    if (!REGEX_EMAIL.test(email)) {
                        $("#errorEmail").removeClass("error_show").addClass("error");
                        $("#error_exitsted").removeClass("error_show").addClass("error");
                        $("#errorFormat").removeClass("error").addClass("error_show");
                        checkForm = false;
                    } else {
                        $("#error_exitsted").removeClass("error_show").addClass("error");
                        $("#errorFormat").removeClass("error_show").addClass("error");
                        $("#errorEmail").removeClass("error_show").addClass("error");
                    }
                }
            }

        } else {
            if (emailExisted === email) {
                $("#error_exitsted").removeClass("error").addClass("error_show");
                $("#errorFormat").removeClass("error_show").addClass("error");
                $("#errorEmail").removeClass("error_show").addClass("error");
                checkForm = false;
            } else {
                if (isEmpty(email) || isBlank(email)) {
                    $("#errorFormat").removeClass("error_show").addClass("error");
                    $("#error_exitsted").removeClass("error_show").addClass("error");
                    $("#errorEmail").removeClass("error").addClass("error_show");
                    checkForm = false;
                } else {
                    if (!REGEX_EMAIL.test(email)) {
                        $("#errorEmail").removeClass("error_show").addClass("error");
                        $("#error_exitsted").removeClass("error_show").addClass("error");
                        $("#errorFormat").removeClass("error").addClass("error_show");
                        checkForm = false;
                    } else {
                        $("#error_exitsted").removeClass("error_show").addClass("error");
                        $("#errorFormat").removeClass("error_show").addClass("error");
                        $("#errorEmail").removeClass("error_show").addClass("error");
                    }
                }
            }
        }
        var userName = $("#userName").val();
        if (isEmpty(userName) || isBlank(userName)) {
            $("#errorUserName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorUserName").removeClass("error_show").addClass("error");
        }

        var password = $("#password").val();
        if (isEmpty(password)) {
            $("#errorPassword").removeClass("error").addClass("error_show");
            $("#errorPasswordSpaceWhite").removeClass("error_show").addClass("error");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else if (isBlank(password) || isExistedSpaceWhite(password)) {
            $("#errorPassword").removeClass("error_show").addClass("error");
            $("#errorPasswordSpaceWhite").removeClass("error").addClass("error_show");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        }
        else {
            $("#errorPasswordSpaceWhite").removeClass("error_show").addClass("error");
            $("#errorPassword").removeClass("error_show").addClass("error");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
        }

        var repPassword = $("#repPassword").val();
        if (isEmpty(repPassword)) {
            $("#errorRepPassword").removeClass("error").addClass("error_show");
            $("#errorRepPasswordSpaceWhite").removeClass("error_show").addClass("error");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else if (isBlank(repPassword) || isExistedSpaceWhite(repPassword)) {
            $("#errorRepPassword").removeClass("error_show").addClass("error");
            $("#errorRepPasswordSpaceWhite").removeClass("error").addClass("error_show");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        }
        else {
            $("#errorRepPasswordSpaceWhite").removeClass("error_show").addClass("error");
            $("#errorRepPassword").removeClass("error_show").addClass("error");
            // $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
        }

        if (isNotEmpty(repPassword) && isNotEmpty(password) && isNotBlank(repPassword) && isNotExistedSpaceWhite(repPassword) && isNotExistedSpaceWhite(password) && isNotBlank(password)) {
            // $("#errorRepPassword").removeClass("error_show").addClass("error");

            if (password != repPassword) {
                $("#errorPasswordNoRep").removeClass("error").addClass("error_show");
                checkForm = false;
            }
        } else {
            $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        }

        if (checkForm === false) {
            event.preventDefault();
        }

    })

});
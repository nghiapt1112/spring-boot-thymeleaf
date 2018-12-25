$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var checkForm = true;
        var REGEX_EMAIL = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

        var email = $("#email").val();
        if (isEmpty(email)) {
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
            }
        }

        var userName = $("#userName").val();
        if (isEmpty(userName)) {
            $("#errorUserName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorUserName").removeClass("error_show").addClass("error");
        }

        var password = $("#password").val();
        if (isEmpty(password)) {
            $("#errorPassword").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorPassword").removeClass("error_show").addClass("error");
        }

        var confirm_password = $("#confirm_password").val();
        if (isEmpty(confirm_password)) {
            $("#errorRepPassword").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorRepPassword").removeClass("error_show").addClass("error");
        }

        if (isNotEmpty(password) && isNotEmpty(confirm_password)) {
            $("#errorPassword").removeClass("error_show").addClass("error");
            $("#errorRepPassword").removeClass("error_show").addClass("error");
            if (password != confirm_password) {
                $("#errorPasswordNoRep").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            }
        }
        if (checkForm === false) {
            event.preventDefault();
        }
    })
});

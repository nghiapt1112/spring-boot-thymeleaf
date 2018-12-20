$(document).ready(function () {
    $("body").on("click", "#btnSubmit", function (event) {
        var checkForm = true;
        var REGEX_EMAIL = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

        var email = $("#email").val();
        if (isEmpty(email)) {
            $("#errorEmail").removeClass("error").addClass("error_show");
            $("#errorFormat").removeClass("error_show").addClass("error");
            checkForm = false;
        } else {
            $("#errorEmail").removeClass("error_show").addClass("error");
            if (!REGEX_EMAIL.test(email)) {
                $("#errorFormat").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
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

        if (checkForm === false) {
            event.preventDefault();
        }

        var password = $("#password").val();
        if (isEmpty(password)) {
            $("#errorPassword").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorPassword").removeClass("error_show").addClass("error");
        }

        if (checkForm === false) {
            event.preventDefault();
        }

    })

});
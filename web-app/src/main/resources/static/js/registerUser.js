$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
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

        var password = $("#password").val();
        var repPassword = $("#repPassword").val();
        if (isEmpty(password)) {
            $("#errorPassword").removeClass("error").addClass("error_show");
            $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else {
            $("#errorPassword").removeClass("error_show").addClass("error");
        }

        if (isEmpty(repPassword)) {
            $("#errorRepPassword").removeClass("error").addClass("error_show");
            $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else {
            $("#errorRepPassword").removeClass("error_show").addClass("error");
        }


        if (isNotEmpty(repPassword) && isNotEmpty(password)) {
            $("#errorPassword").removeClass("error_show").addClass("error");
            $("#errorRepPassword").removeClass("error_show").addClass("error");
            if (password != repPassword) {
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
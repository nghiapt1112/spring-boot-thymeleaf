$(document).ready(function () {
    $("body").on("click", "#btnSubmit", function (event) {
        var REGEX_EMAIL = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var checkForm = true;
        var email = $("#email").val();
        if (email == null || email === "" || email.trim() === "") {
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
        if (userName == null || userName === "" || userName.trim() === "") {
            $("#errorUserName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorUserName").removeClass("error_show").addClass("error");
        }

        var password = $("#password").val();
        var repPassword = $("#repPassword").val();
        if (password == null || password === "" || password.trim() === "") {
            $("#errorPassword").removeClass("error").addClass("error_show");
            $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else {
            $("#errorPassword").removeClass("error_show").addClass("error");
        }


        if (repPassword == null || repPassword === "" || repPassword.trim() === "") {
            $("#errorRepPassword").removeClass("error").addClass("error_show");
            $("#errorPasswordNoRep").removeClass("error_show").addClass("error");
            checkForm = false;
        } else {
            $("#errorRepPassword").removeClass("error_show").addClass("error");
        }
        if (repPassword != null && repPassword != "" && repPassword.trim() != "" && password != null &&
            password != "" && password.trim() != "") {
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
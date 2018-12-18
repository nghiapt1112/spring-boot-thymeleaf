$(document).ready(function () {
    $("body").on("click", "#btnSubmit", function (event) {
        var checkForm = true;
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
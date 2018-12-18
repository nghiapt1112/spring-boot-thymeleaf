$(document).ready(function () {
    $("body").on("click", "#btnSubmit", function (event) {
        var checkForm = true;

        var password = $("#password").val();
        if (password == null || password === "" || password.trim() === "") {
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
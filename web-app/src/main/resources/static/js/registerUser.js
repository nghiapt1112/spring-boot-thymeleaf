$(document).ready(function () {


    $("body").on("click", "#save", function (event) {
        let checkForm = true;
        var email = $("#email").val();
        if (email === "" || email.trim() === "") {
            $("#errorEmail").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmail").removeClass("error_show").addClass("error");
        }

        var userName = $("#userName").val();
        if (userName === "" || userName.trim() === "") {
            $("#errorUserName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorUserName").removeClass("error_show").addClass("error");
        }

        var password = $("#password").val();
        if (password === "" || password.trim() === "") {
            $("#errorPassword").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorPassword").removeClass("error_show").addClass("error");
        }

        var repPassword = $("#repPassword").val();
        if (repPassword === "" || repPassword.trim() === "") {
            $("#errorRepPassword").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorRepPassword").removeClass("error_show").addClass("error");
        }
        var repPassword = $("#repPassword").val();


        if (checkForm === false) {
            event.preventDefault();
        }

    })

})
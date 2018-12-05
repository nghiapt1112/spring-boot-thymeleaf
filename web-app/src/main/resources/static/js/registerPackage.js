$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var checkForm = true;
        var name = $("#name").val();
        if (name = null || name === "" || name.trim() === "") {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");
        }
        if (checkForm === false) {
            event.preventDefault();
        }

    })

})
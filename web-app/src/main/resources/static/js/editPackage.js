$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var numberFormat = /^(\d{1,11})(?:\.\d\d?)?$/;
        var checkForm = true;
        var name = $("#name").val();
        if (name = null || name === "" || name.trim() === "") {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");
        }

        var emptyWeight = $("#emptyWeight").val();
        if (emptyWeight = null || emptyWeight === "" || emptyWeight.trim() === "" || !numberFormat.test(emptyWeight)) {
            $("#errorEmptyWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyWeight").removeClass("error_show").addClass("error");
        }

        var fullLoadWeight = $("#fullLoadWeight").val();
        if (fullLoadWeight = null || fullLoadWeight === "" || fullLoadWeight.trim() === "" || !numberFormat.test(fullLoadWeight)) {
            $("#errorFullLoadWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorFullLoadWeight").removeClass("error_show").addClass("error");
        }

        var emptyCapacity = $("#emptyCapacity").val();
        if (emptyCapacity = null || emptyCapacity === "" || emptyCapacity.trim() === "" || !numberFormat.test(emptyCapacity)) {
            $("#errorEmptyCapacity").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyCapacity").removeClass("error_show").addClass("error");
        }

        var fullLoadCapacity = $("#fullLoadCapacity").val();
        if (fullLoadCapacity = null || fullLoadCapacity === "" || fullLoadCapacity.trim() === "" || !numberFormat.test(fullLoadCapacity)) {
            $("#errorFullLoadCapacity").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorFullLoadCapacity").removeClass("error_show").addClass("error");
        }

        if (checkForm === false) {
            event.preventDefault();
        }

    })

})
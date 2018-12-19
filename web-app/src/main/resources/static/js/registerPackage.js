$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var numberFormat = /^(\d{1,11})(?:\.\d\d?)?$/;
        var checkForm = true;

        var name = $("#name").val();
        if (isEmpty(name)) {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");

        }
        var emptyWeight = $("#emptyWeight").val();
        if (isEmpty(emptyWeight) === "" || !numberFormat.test(emptyWeight)) {
            $("#errorEmptyWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyWeight").removeClass("error_show").addClass("error");
        }

        var fullLoadWeight = $("#fullLoadWeight").val();
        if (isEmpty(fullLoadWeight) === "" || !numberFormat.test(fullLoadWeight)) {
            $("#errorFullLoadWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorFullLoadWeight").removeClass("error_show").addClass("error");
        }

        var emptyCapacity = $("#emptyCapacity").val();
        if (isEmpty(emptyCapacity) || !numberFormat.test(emptyCapacity)) {
            $("#errorEmptyCapacity").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyCapacity").removeClass("error_show").addClass("error");
        }

        var fullLoadCapacity = $("#fullLoadCapacity").val();
        if (isEmpty(fullLoadCapacity) === "" || !numberFormat.test(fullLoadCapacity)) {
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
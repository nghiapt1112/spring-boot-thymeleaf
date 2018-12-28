$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var REGEX_NUMBER = /^(\d{1,})(?:\.\d{1,})?$/;
        var checkForm = true;

        var name = $("#name").val();
        if (isEmpty(name) || isBlank(name)) {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");

        }
        var emptyWeight = $("#emptyWeight").val();
        if (isEmpty(emptyWeight) || isBlank(emptyWeight)) {
            $("#errorFormatEmptyWeight").removeClass("error_show").addClass("error");
            $("#errorEmptyWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyWeight").removeClass("error_show").addClass("error");
            if (!REGEX_NUMBER.test(emptyWeight)) {
                $("#errorFormatEmptyWeight").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorFormatEmptyWeight").removeClass("error_show").addClass("error");
            }
        }

        var fullLoadWeight = $("#fullLoadWeight").val();
        if (isEmpty(fullLoadWeight) || isBlank(fullLoadWeight)) {
            $("#errorFormatFullLoadWeight").removeClass("error_show").addClass("error");
            $("#errorFullLoadWeight").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorFullLoadWeight").removeClass("error_show").addClass("error");
            if (!REGEX_NUMBER.test(fullLoadWeight)) {
                $("#errorFormatFullLoadWeight").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorFormatFullLoadWeight").removeClass("error_show").addClass("error");
            }
        }

        var emptyCapacity = $("#emptyCapacity").val();
        if (isEmpty(emptyCapacity) || isBlank(emptyCapacity)) {
            $("#errorFormatEmptyCapacity").removeClass("error_show").addClass("error");
            $("#errorEmptyCapacity").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorEmptyCapacity").removeClass("error_show").addClass("error");
            if (!REGEX_NUMBER.test(emptyCapacity)) {
                $("#errorFormatEmptyCapacity").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorFormatEmptyCapacity").removeClass("error_show").addClass("error");
            }
        }

        var fullLoadCapacity = $("#fullLoadCapacity").val();
        if (isEmpty(fullLoadCapacity) || isBlank(fullLoadCapacity)) {
            $("#errorFormatFullLoadCapacity").removeClass("error_show").addClass("error");
            $("#errorFullLoadCapacity").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorFullLoadCapacity").removeClass("error_show").addClass("error");
            if (!REGEX_NUMBER.test(fullLoadCapacity)) {
                $("#errorFormatFullLoadCapacity").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorFormatFullLoadCapacity").removeClass("error_show").addClass("error");
            }
        }
        checkForm = false;
        if (checkForm === false) {
            event.preventDefault();
        }

    })

});
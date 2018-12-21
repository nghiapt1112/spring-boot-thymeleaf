$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var REGEX_PRICE = /^(\d{1,})(?:\.\d{1,})?$/;
        var checkForm = true;
        var code = $("#code").val();
        if (isEmpty(code)) {
            $(this).closest("body").find(".button-submit").find("button").removeAttr("data-toggle").removeAttr("data-target");
            $("#errorCode").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorCode").removeClass("error_show").addClass("error");
        }

        var name = $("#name").val();
        if (isEmpty(name)) {
            $("#errorName").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorName").removeClass("error_show").addClass("error");
        }

        var price = $("#price").val();
        if (isEmpty(price)) {
            $("#errorFormatPrice").removeClass("error_show").addClass("error");
            $("#errorPrice").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorPrice").removeClass("error_show").addClass("error");
            if (!REGEX_PRICE.test(price)) {
                $("#errorFormatPrice").removeClass("error").addClass("error_show");
                checkForm = false;
            } else {
                $("#errorFormatPrice").removeClass("error_show").addClass("error");
            }
        }

        if (checkForm === false) {
            event.preventDefault();
        }

    })

});
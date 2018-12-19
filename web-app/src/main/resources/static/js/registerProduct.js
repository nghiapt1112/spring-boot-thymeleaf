$(document).ready(function () {
    $("body").on("click", "#save", function (event) {
        var numberFormat = /^(\d{1,10})(?:\.\d\d?)?$/;
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
        if (isEmpty(price) || !numberFormat.test(price)) {
            $("#errorPrice").removeClass("error").addClass("error_show");
            checkForm = false;
        } else {
            $("#errorPrice").removeClass("error_show").addClass("error");
        }

        if (checkForm === false) {
            event.preventDefault();
        }

    })

})
$(document).ready(function () {
    $("#min").datepicker({
        format: 'yyyy/mm/dd'
    });
    $("#max").datepicker({
        format: 'yyyy/mm/dd'
    });

    $("#orderButtonClick").click(function () {
       var _this = $(this);
        setTimeout(function () {
            _this.closest("body").find("#logisticsButtonClickTranfer").trigger("click");
        },170)
    });
    $("#logisticsButtonClickTranfer").click(function () {
        $("#setWidthHederForOrder").click();
    })
    onLogistics();
    onOrderDetails();
});
$(function () {
    $('button[type=submit]').click(function (e) {
        if (document.getElementById("files").files.length == 0) {
            e.preventDefault();
            $("#alertMsg").text("ファイルを選択してください。")
        } else {
            var form = document.forms[0];
            var formData = new FormData(form);
            var optionType = $("#quizID").val();
            var url = "/upload/file/delivery";
            if (optionType == 2) {
                //Disable submit button
                $(this).prop('disabled', true);
                // Ajax call for file uploaling
                var ajaxReq = $.ajax({
                    url: url,
                    type: 'POST',
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    xhr: function () {
                        //Get XmlHttpRequest object
                        var xhr = $.ajaxSettings.xhr();

                        //Set onprogress event handler
                        xhr.upload.onprogress = function (event) {
                            var perc = Math.round((event.loaded / event.total) * 100);
                            $('#progressBar').text(perc + '%');
                            $('#progressBar').css('width', perc + '%');
                        };
                        return xhr;
                    },
                    beforeSend: function (xhr) {
                        //Reset alert message and progress bar
                        $('#alertMsg').text('');
                        $('#progressBar').text('');
                        $('#progressBar').css('width', '0%');
                    }
                });

                // Called on success of file upload
                ajaxReq.done(function (msg) {
                    var logisticAPI = '/mainScreen';
                    $('input[type=file]').val('');
                    $('button[type=submit]').prop('disabled', false);
                    modal.style.display = "none";
                    window.location.replace(logisticAPI)
                });

                // Called on failure of file upload
                ajaxReq.fail(function (jqXHR) {
                    $('#alertMsg').text(jqXHR.responseText.replace
                    ('[', '').replace(']', '').replace('"', '').replace('"', ""));
                    $('button[type=submit]').prop('disabled', false);
                });
            }

        }
    });
});

var modal = document.getElementById('myModal');
var btnOrder = document.getElementById("myBtn");
var btnDelivery = document.getElementById("deliveryBtn");
var els = document.getElementsByClassName("close");
btnOrder.onclick = function () {
    $('#progressBar').text('');
    $('#progressBar').css('width', '0%');
    modal.style.display = "block";
    document.getElementById("quizID").value = "1";
    $('#alertMsg').text('');
    $('input[type=file]').val('');
};

for (var i = 0; i < els.length; i++) {
    els[i].onclick = function () {
        modal.style.display = "none";
    }
}

btnDelivery.onclick = function () {
    $('#progressBar').text('');
    $('#progressBar').css('width', '0%');
    modal.style.display = "block";
    document.getElementById("quizID").value = "2";
    $('#alertMsg').text('');
    $('input[type=file]').val('');
};

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};

// call search API
function searchMainMenu() {
    var logisticAPI = '/mainScreen';
    var startDate = document.getElementById("min").value;
    var endDate = document.getElementById("max").value;
    var postName = document.getElementById("post").value;

    var currentDate = new Date().setHours(0, 0, 0, 0);
    if (startDate) {
        logisticAPI += '?start=' + new Date(startDate).getTime()
    } else {
        logisticAPI += '?start=' + currentDate
    }
    if (endDate) {
        logisticAPI += '&end=' + new Date(endDate).getTime()
    } else {
        logisticAPI += '&end=' + currentDate
    }
    if (postName) {
        logisticAPI += '&postName=' + postName
    }

    window.location.replace(logisticAPI)
}


function onLogistics() {
    exportDataMainmenu('#table-logistics', 0, '物量', true, true);
}

function onOrderDetails() {
    exportDataMainmenu('#table-order', 0, '発注データ明細', true, true);
}




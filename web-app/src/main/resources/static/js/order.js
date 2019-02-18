$(function () {
    $('.js-example-basic-single').select2();
    var table = $("#t_order").DataTable({
        'paging': true,
        'scrollX': false,
        'scrollCollapse': false,
        'lengthChange': false,
        'searching': false,
        'ordering': false,
        'info': false,
        'autoWidth': true,
        'order': [2, 'asc'],
        "columnDefs": [{'orderable': false, 'targets': [0]}],
        "language": {
            "lengthMenu": "  _MENU_ 件を表示",
            "zeroRecords": "該当データが存在しません。",
            "info": "_TOTAL_ 件中 _START_ ~ _END_  件を表示",
            "infoEmpty": "該当データが存在しません。",
            "infoFiltered": "(件を表示 _MAX_ total records)",
            "search": "検索:",
            "paginate": {
                "previous": " 前へ ",
                "next": "  次へ "
            }
        }

    });

    $('#submitFile').click(function (e) {
        var form = document.forms[0];
        var formData = new FormData(form);
        var url = "/upload/file/order";
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
            $('input[type=file]').val('');
            $(this).prop('disabled', true);
            var logisticAPI = '/mainScreen';
            window.location.replace(logisticAPI)
        });

        // Called on failure of file upload


        ajaxReq.fail(function (jqXHR) {
            var messageError = jqXHR.responseText;
            $("#messagePopup").trigger("click");
            $("#messageErrors").empty();
            messageError = messageError.substring(1, messageError.length - 1);
            if (messageError.includes(",")) {
                var messageErrorArray = messageError.split(",");
                for (var i = 0; i < messageErrorArray.length; i++) {
                    var indexCodeBegin = messageErrorArray[i].indexOf('"');
                    var indexCodeEnd = messageErrorArray[i].indexOf('"', indexCodeBegin + 1);
                    var errorCode = messageErrorArray[i].substring(indexCodeBegin + 1, indexCodeEnd);
                    var indexContentBegin = messageErrorArray[i].indexOf('"', indexCodeEnd + 1);
                    var indexContentEnd = messageErrorArray[i].indexOf('"', indexContentBegin + 2);
                    var errorContent = messageErrorArray[i].substring(indexContentBegin + 1, indexContentEnd);
                    $("#messageErrors").append('<tr><td>' + errorCode + '</td><td>' + errorContent + '</td></tr>')
                }
            }
        });
    });

    $('.select2').on('click', function () {
        $(this).closest("body").find(".select2-results").find("ul").children().eq(0).css({
            "padding-bottom": "21%"
        });
    });
});
$(function () {
    $('.js-example-basic-single').select2();
    var table = $("#t_order").DataTable({
        'paging': true,
        'scrollX': false,
        'scrollCollapse': false,
        'fixedColumns': {
            'leftColumns': 0
        },
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

    $('button[type=submit]').click(function (e) {
        var form = document.forms[0];
        var formData = new FormData(form);
        var url = "/upload/file/order";
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
            $('input[type=file]').val('');
            $('button[type=submit]').prop('disabled', false);
            var logisticAPI = '/mainScreen';
            window.location.replace(logisticAPI)
        });

        // Called on failure of file upload
        ajaxReq.fail(function (jqXHR) {
            $('#alertMsg').text(jqXHR.responseText.replace
            ('[', '').replace(']', '').replace('"', '').replace('"', ""));
            $('button[type=submit]').prop('disabled', false);
        });

    });
});
$(document).ready(function () {
    $("#min").datepicker({
        format: 'yyyy/mm/dd'
    });
    $("#max").datepicker({
        format: 'yyyy/mm/dd'
    });

});

$(function () {
    $(document).ready(function () {
        var table = $('#table-order').DataTable();
        $('#search').click(function () {
            table.draw();
        });
        var table = $('#table-logicstic').DataTable();
        $('#search').click(function () {
            table.draw();
        });
        $("#min").datepicker({
            format: 'yyyy/mm/dd'
        });
        $("#max").datepicker({
            format: 'yyyy/mm/dd'
        });
    });
});

var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
btn.onclick = function () {
    modal.style.display = "block";
}
span.onclick = function () {
    modal.style.display = "none";
}
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
$(function () {
    $('button[type=submit]').click(function (e) {
        e.preventDefault();
        //Disable submit button
        $(this).prop('disabled', true);

        var form = document.forms[0];
        var formData = new FormData(form);

        // Ajax call for file uploaling
        var ajaxReq = $.ajax({
            url: "/upload/file",
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
            $('#alertMsg').text(msg);
            $('input[type=file]').val('');
            $('button[type=submit]').prop('disabled', false);
        });

        // Called on failure of file upload
        ajaxReq.fail(function (jqXHR) {
            $('#alertMsg').text(jqXHR.responseText + '(' + jqXHR.status +
                ' - ' + jqXHR.statusText + ')');
            $('button[type=submit]').prop('disabled', false);
        });
    });
});

var modal = document.getElementById('myModal');
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
btn.onclick = function () {
    modal.style.display = "block";
}
span.onclick = function () {
    modal.style.display = "none";
}
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

$(function () {
    $('#table-order').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': true,
        'order': [],
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
    })
    $('#table-logicstic').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,

        'autoWidth': true,
        'order': [],
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
    })
})



 


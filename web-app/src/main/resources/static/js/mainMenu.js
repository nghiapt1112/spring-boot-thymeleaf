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

        value = $("#quizID").val();
        var url = "/upload/file";
        if (value == 2)
            url = "/upload/fileDelivery";

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
var btndelivery = document.getElementById("deliveryBtn");
var span = document.getElementsByClassName("close")[0];
btn.onclick = function () {
    modal.style.display = "block";
    document.getElementById("quizID").value = "1";
}
btndelivery.onclick = function () {
    modal.style.display = "block";
    document.getElementById("quizID").value = "2";
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
    })
    $('#table-logicstic').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': true
    })
})

 


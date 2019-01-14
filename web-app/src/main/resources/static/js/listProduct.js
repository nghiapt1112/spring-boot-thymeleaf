$(document).ready(function () {
    deleteTable("product","#table-product");
});

$(function () {
    $('button[type=submit]').click(function (e) {
        if(document.getElementById("files").files.length == 0 ){
            e.preventDefault();
            $("#alertMsg").text("ファイルを選択してください。")
        }else {
            e.preventDefault();
            //Disable submit button
            $(this).prop('disabled', true);

            var form = document.forms[0];
            var formData = new FormData(form);

            value = $("#quizID").val();
            var url = "/upload/file/product";

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
                //$('#alertMsg').text(msg);
                $('input[type=file]').val('');
                $('button[type=submit]').prop('disabled', false);
                window.location.href = "/product/list";
            });

            // Called on failure of file upload
            ajaxReq.fail(function (jqXHR) {
                $('#alertMsg').text(jqXHR.responseText.replace
                ('[', '').replace(']', '').replace('"', '').replace('"', ""));
                $('button[type=submit]').prop('disabled', false);
            });
        }

    });
});

var modal = document.getElementById('myModalProduct');
var btnProduct = document.getElementById("productBtn");
var span = document.getElementsByClassName("close")[0];

btnProduct.onclick = function () {
    modal.style.display = "block";
    document.getElementById("quizID").value = "1";
    $('#alertMsg').text('');
};

span.onclick = function () {
    modal.style.display = "none";
};
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};

exportDataToExcel('#table-product',2,'商品一覧',1,2,3,4,5,6,7,'');






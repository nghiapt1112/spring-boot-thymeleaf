$(document).ready(function () {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });

    $("#inputSearch").on('change keydown paste input', function () {
        updateLink();
    });
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
            var url = "/upload/file/store";

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
                window.location.href = "/store/list";
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

var modal = document.getElementById('myModalStore');
var btnStore = document.getElementById("storeBtn");
var span = document.getElementsByClassName("close")[0];

btnStore.onclick = function () {
    $('#progressBar').text('');
    $('#progressBar').css('width', '0%');
    modal.style.display = "block";
    $('#alertMsg').text('');
    $('input[type=file]').val('');
};

span.onclick = function () {
    modal.style.display = "none";
};
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};

function updateLink() {
    textInput = document.getElementById("inputSearch").value.replace(/\s+/g, '').toLowerCase();
    var links = document.getElementsByTagName('a');
    for (var i = 0; i < links.length; i++) {
        var thisLink = links[i];
        var source = thisLink.getAttribute('href');
        if (source != null && source.includes("searchText") !== -1) {
            var indexSearch = source.indexOf("searchText");
            var size = document.getElementById("listStore_length").value;
            var response = source.substring(0, indexSearch) + "searchText=" + textInput + " &size=" + size;
            thisLink.setAttribute('href', response);
        }
    }
}

function deleteStore() {
    var pickedOne = false;
    var inputs = document.getElementsByClassName('chkCheckBoxId');
    for (var i = 0, l = inputs.length; i < l; ++i) {
        if (inputs[i].checked) {
            pickedOne = true;
            break;
        }
    }

    if (!pickedOne) {
        alert('少なくともいずれか一つを選らんでください。');
        return false;
    } else if (confirm('削除してもよろしいですか？')) {
        deleteViaAjax();
    }
}


function deleteViaAjax() {
    var storeIds = [];
    var checkboxes = $('input[name="storeid"]');
    checkboxes.filter(":checked").map(function () {
        storeIds.push(this.value);
    }).get()

    $.ajax({
        type: "GET",
        contentType: 'application/json; charset=utf-8',
        url: "/store/delete",
        data: {
            storeIds: storeIds
        },
        dataType: 'json',
        timeout: 100000,
        done: function (data) {
        },
        statusCode: function () {
        },
        success: function (data) {
            window.location.href = "/store/list";
        },
        error: function (e) {
            alert("削除しました。");
        }
    });
};

function changeSizeStore() {
    updateLink();
}

exportDataToExcel('#table-store',1, '店舗一覧',1,2,3,4,5,6,7, '');





$(document).ready(function () {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });
});

function deleteUser() {
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
        addViaAjax();
    }
}

//
function addViaAjax() {
    var userIds = [];
    var checkboxes = $('input[name="userid"]');
    checkboxes.filter(":checked").map(function () {
        userIds.push(this.value);
    }).get()

    $.ajax({
        type: "GET",
        contentType: 'application/json; charset=utf-8',
        url: "/user/delete",
        data: {
            userIds: userIds.toString()
        },
        dataType: 'json',
        timeout: 100000,
        done: function (data) {
        },
        statusCode: function () {
        },
        success: function (data) {
            console.log("SUCCESS: ", data);
            var result = "<h3> You delete user:  </h3>"
                + "<strong>Name:</strong> " + data.name;
            $("#ajax-response").html(result);
            //alert("削除しました。");
            window.location.href = "/user/list";
        },
        error: function (e) {
            alert("削除しました。");
            console.log("ERROR: ", e);
        }
    });
};

$(function () {
    $('#listUser').DataTable({
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': false,
        'autoWidth': false,
        'order': [],
        "columnDefs": [{'orderable': false, 'targets': [0]}],

        "language": {
            "lengthMenu": "  _MENU_ 件を表示",
            "zeroRecords": "không tìm thấy found - sorry",
            "info": "_TOTAL_ 件中 _START_ ~ _END_  件を表示",
            "infoEmpty": "Không có records available",
            "infoFiltered": "(Tìm Thấy from _MAX_ total records)",
            "search": "検索:",
            "paginate": {
                "previous": " 前へ ",
                "next": "  次へ "
            }
        }
    });
});

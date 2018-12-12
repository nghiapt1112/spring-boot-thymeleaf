$(document).ready(function () {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });
});

function deleteProduct() {
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
    var productIds = [];
    $("#table-product > tbody input:checked").each(function () {
        productIds.push($(this).val());
    })

    $.ajax({
        type: "GET",
        contentType: 'application/json; charset=utf-8',
        url: "/product/delete",
        data: {
            productIds: productIds
        },
        dataType: 'json',
        timeout: 100000,
        done: function (data) {
        },
        statusCode: function () {
        },
        success: function (data) {
            if (data == true) {
                window.location.href = "/product/list";
            } else {
                alert("削除しました。");
            }
        },
        error: function (e) {
            alert("削除しました。");
        }
    });
};

$('#table-product').DataTable({
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
});





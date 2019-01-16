function deleteTable(apiName, tableId) {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });
    $("body").on("click", "#delete", function () {
        var pickedOne = false;
        var objectIds = [];
        var inputs = document.getElementsByClassName('chkCheckBoxId');
        for (var i = 0, l = inputs.length; i < l; ++i) {
            if (inputs[i].checked) {
                pickedOne = true;
                objectIds.push(inputs[i].value);
                break;
            }
        }
        if (!pickedOne) {
            alert('少なくともいずれか一つを選らんでください。');
            return false;
        } else if (confirm('削除してもよろしいですか？')) {
            $.ajax({
                type: "GET",
                contentType: 'application/json; charset=utf-8',
                url: "/" + apiName + "/delete",
                data: {
                    ojectIds: objectIds
                },
                dataType: 'json',
                timeout: 100000,
                done: function (data) {
                },
                statusCode: function () {
                },
                success: function (data) {
                    if (data == true) {
                        window.location.href = "/" + apiName + "/list";
                    } else {
                        alert("削除しました。");
                    }
                },
                error: function (e) {
                    alert("削除しました。");
                }
            });
        }
    })
}

function dataTable(tableId, sortDefaultColumn) {
    $(tableId).DataTable({
        'dom': 'lBfrtip',
        'buttons': [
            'excel'
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': true,
        'order': [sortDefaultColumn, 'asc'],
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
    $("body").find(".buttons-excel").find('span').text('優れる');
}
$(document).ready(function () {

    var arrayStoreNumber = [1,2];
    $("body").find(".storeModel").each(function (item) {
        arrayStoreNumber.push(item + 3);
    })
    $("#table-user").DataTable({
        'scrollX':        true,
        'scrollCollapse': true,
        'fixedColumns':   {
            'leftColumns': 3,
        },
        'dom': 'lBfrtip',
        'buttons': [
            {
                extend : 'excel',
                filename: 'ユーザー一覧',
                title : null,
                exportOptions: {
                    columns: arrayStoreNumber
                }
            }
        ],
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': true,
        'order': [1, 'asc'],
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
    $("body").find(".buttons-excel").find("span").text("EXCEL出力");

    $(".checkBoxAll:eq(2)").click(function () {
        if ($(this).is(":checked")){
            $('body').find('tbody:eq(1)').find(".chkCheckBoxId").prop('checked', true);
        } else{
            $('body').find('tbody:eq(1)').find(".chkCheckBoxId").prop('checked', false);
        }
    })
    $("body").on("click", "#delete", function () {
        var pickedOne = false;
        var objectIds = [];
        var inputs = $('body').find('tbody:eq(1)').find(".chkCheckBoxId");
        console.log(inputs.length);
        for (var i = 0, l = inputs.length; i < l; i++) {
            if (inputs[i].checked) {
                pickedOne = true;
                objectIds.push(inputs[i].value);

            }
        }

        if (!pickedOne) {
            alert('少なくともいずれか一つを選らんでください。');
            return false;
        } else if (confirm('削除してもよろしいですか？')) {
            $.ajax({
                type: "GET",
                contentType: 'application/json; charset=utf-8',
                url: "/user/delete",
                data: {
                    objectIds: objectIds
                },
                dataType: 'json',
                timeout: 100000,
                done: function (data) {
                },
                statusCode: function () {
                },
                success: function (data) {
                    if (data == true) {
                        window.location.href = "/user/list";
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
});
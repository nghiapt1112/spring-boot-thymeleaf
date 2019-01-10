$(document).ready(function () {
    deleteTable("user","#table-user");
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
                filename: 'ユーザー一覧'
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
});
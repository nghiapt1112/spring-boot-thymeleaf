function exportDataToExcel(tableId, sortDefaultColumn , fileName, item1 ,item2 , item3, item4, item5, item6, item7 , item8) {
    $(tableId).DataTable({
        destroy: true,
        'dom': 'lBfrtip',
        'buttons': [
            {
                extend: 'excel',
                filename : fileName,
                title : null,
                exportOptions: {
                    columns: [ item1, item2, item3, item4, item5, item6, item7, item8 ]
                }
            }
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
    $("body").find(".buttons-excel").find("span").text("EXCEL出力");

}
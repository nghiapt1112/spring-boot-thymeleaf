function exportDataToExcel(t1 , t2) {
    var table = $(t1).DataTable({
        destroy: true,
        'scrollX':        false,
        'scrollCollapse': false,
        'fixedColumns':   {
            'leftColumns': 3,
        },
        'paging': true,
        'lengthChange': true,
        'searching': true,
        'ordering': true,
        'info': true,
        'autoWidth': true,
        'order': [2, 'asc'],
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
    var buttons = new $.fn.dataTable.Buttons(table, {
        buttons: [
            'excelHtml5'
        ]
    }).container().appendTo($(t2));
    $("body").find(".buttons-excel").find("span").text("優れる");

}
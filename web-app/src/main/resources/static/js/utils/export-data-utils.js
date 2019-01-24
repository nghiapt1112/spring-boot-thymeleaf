function exportDataToExcel(tableId, sortDefaultColumn, fileName, item1, item2, item3, item4, item5, item6, item7, item8) {
    $(tableId).DataTable({
        destroy: true,
        'dom': 'lBfrtip',
        'buttons': [
            {
                extend: 'excel',
                filename: fileName,
                title: null,
                exportOptions: {
                    columns: [item1, item2, item3, item4, item5, item6, item7, item8]
                }
            }
        ],
        "footerCallback": function (row, data, start, end, display) {
            if (tableId === "#table-logicstic") {
                var api = this.api();
                var columns = document.querySelectorAll(".columnSum");
                for (i = 0; i < columns.length; i++) {
                    var colNo = i + 3;
                    var pageTotal = api
                        .column(colNo, {page: 'current'})
                        .data()
                        .reduce(function (a, b) {
                            return Number(a) + Number(b);
                        }, 0).toFixed(2);
                    var index = pageTotal.indexOf(".");

                    if ('0' != (pageTotal.substring(index + 2))) {
                        $(api.column(colNo).footer()).html(
                            pageTotal
                        );
                    } else {
                        var resultNumber;
                        if ('0' != (pageTotal.substring(index + 1, index + 2))) {
                            resultNumber = pageTotal.substring(0, pageTotal.length - 1);
                            $(api.column(colNo).footer()).html(
                                resultNumber
                            );
                        } else {
                            resultNumber = pageTotal.substring(0, pageTotal.length - 3);
                            $(api.column(colNo).footer()).html(
                                resultNumber
                            );
                        }
                    }
                }
            }
        },
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
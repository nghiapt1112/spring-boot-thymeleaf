var item = [0, 1, 2, 3, 4, 5, 6, 7];
$('.columnSum').each(function (index) {
    item.push(index + 8);
});

function exportDataMainmenu(tableId, sortDefaultColumn, fileName, item) {
    $(tableId).DataTable({
        destroy: true,
        'dom': 'lBfrtip',
        'buttons': [
            {
                extend: 'excel',
                filename: fileName,
                title: null,
                exportOptions: {
                    columns: item
                }
            }
        ],
        "footerCallback": function (row, data, start, end, display) {
            if (tableId === "#table-logistics") {
                var arrayDisplay = [];
                for(i = 0; i < display.length; i++){
                    if(end <= i){
                        break;
                    }
                    for(j = 0; j < data.length; j++){
                        if(display[i] === j){
                            arrayDisplay.push(data[j]);
                        }
                    }
                }
                var api = this.api();
                var columns = document.querySelectorAll(".columnSum");
                for (i = 0; i < columns.length; i++) {
                    var sumColumn = 0;
                    var colNo = i + 3;
                    for(j = 0; j < arrayDisplay.length; j++){
                        sumColumn = parseFloat(sumColumn) + parseFloat((arrayDisplay[j])[colNo]);
                    }
                    sumColumn = sumColumn.toFixed(2);
                    var index = sumColumn.toString().indexOf(".");
                    if ('0' != (sumColumn.toString().substring(index + 2))) {
                        $(api.column(colNo).footer()).html(
                            sumColumn
                        );
                    } else {
                        var resultNumber;
                        if ('0' != (sumColumn.toString().substring(index + 1, index + 2))) {
                            resultNumber = sumColumn.toString().substring(0, sumColumn.length - 1);
                            $(api.column(colNo).footer()).html(
                                resultNumber
                            );
                        } else {
                            resultNumber = sumColumn.toString().substring(0, sumColumn.length - 3);
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
        'order': [sortDefaultColumn, 'desc'],
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
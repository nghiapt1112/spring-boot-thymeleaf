var item = [0, 1, 2, 3, 4, 5, 6, 7];
$('.columnSum').each(function (index) {
    item.push(index + 8);
});

function exportDataMainmenu(tableId, sortDefaultColumn, fileName, boOleansSrollX, booleansCrollCollapse, item) {
    $(tableId).DataTable({
        destroy: true,
        'scrollX': boOleansSrollX,
        'scrollCollapse': booleansCrollCollapse,
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
                if(display.length > 0){
                    $("#columnSumFooterDisplay").show();
                    var arrayDisplay = [];
                    for (i = 0; i < display.length; i++) {
                        for (j = 0; j < data.length; j++) {
                            if (display[i] === j) {
                                arrayDisplay.push(data[j]);
                            }
                        }
                    }
                    var columns = document.querySelectorAll(".columnSum");
                    for (i = 0; i < columns.length; i++) {
                        var total = 0;
                        var colNo = i + 3;
                        for (j = 0; j < arrayDisplay.length; j++) {
                            if(isNotEmpty((arrayDisplay[j])[colNo])){
                                total = total + parseFloat((arrayDisplay[j])[colNo]);
                            }
                        }
                        if(total.toString().includes(".")){
                            var index = total.toString().indexOf(".");
                            if(total.toString().substring(index + 3).length > 0){
                                total = total.toFixed(2);
                                if ('0' != (total.toString().substring(index + 2))) {
                                    $(this).closest("body").find(".columnSumFooter:eq(" + i + ")").text(total);
                                } else {
                                    if ('0' != (total.toString().substring(index + 1, index + 2))) {
                                        total = total.toString().substring(0, total.length - 1);
                                        $(this).closest("body").find(".columnSumFooter:eq(" + i + ")").text(total);
                                    } else {
                                        total = total.toString().substring(0, total.length - 3);
                                        $(this).closest("body").find(".columnSumFooter:eq(" + i + ")").text(total);
                                    }
                                }

                            }
                        }
                        $(this).closest("body").find(".columnSumFooter:eq(" + i + ")").text(total);
                    }
                }else{
                    $("#columnSumFooterDisplay").hide();
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
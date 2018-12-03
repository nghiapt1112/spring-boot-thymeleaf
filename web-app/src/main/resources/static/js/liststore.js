$(document).ready(function () {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });

    $("#inputSearch").on('change keydown paste input', function () {
        updateLink();
    });

});

function updateLink() {
    textInput = document.getElementById("inputSearch").value.replace(/\s+/g, '').toLowerCase();
    var links = document.getElementsByTagName('a');
    for (var i = 0; i < links.length; i++) {
        var thisLink = links[i];
        var source = thisLink.getAttribute('href');
        if (source != null && source.includes("searchText") !== -1) {
            var indexSearch = source.indexOf("searchText");
            var size = document.getElementById("listStore_length").value;
            var response = source.substring(0, indexSearch) + "searchText=" + textInput + " &size=" + size;
            thisLink.setAttribute('href', response);
        }
    }
}

function deleteStore() {
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


function addViaAjax() {
    var storeIds = [];
    var checkboxes = $('input[name="storeid"]');
    checkboxes.filter(":checked").map(function () {
        storeIds.push(this.value);
    }).get()

    $.ajax({
        type: "GET",
        contentType: 'application/json; charset=utf-8',
        url: "/store/delete",
        data: {
            storeId: storeIds.toString()
        },
        dataType: 'json',
        timeout: 100000,
        done: function (data) {
        },
        statusCode: function () {
        },
        success: function (data) {
            window.location.href = "/store/list";
        },
        error: function (e) {
            alert("削除しました。");
            console.log("ERROR: ", e);
        }
    });
};

function changeSizeStore() {
    updateLink();
}

$('#listStore').DataTable({
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





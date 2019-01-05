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
        deleteViaAjax();
    }
}


function deleteViaAjax() {
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
            storeIds: storeIds
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
        }
    });
};

function changeSizeStore() {
    updateLink();
}

exportDataToExcel('#table-store',1, 店舗一覧);





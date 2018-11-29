$(document).ready(function () {
    $('#checkBoxAll').click(function () {
        if ($(this).is(":checked"))
            $('.chkCheckBoxId').prop('checked', true);
        else
            $('.chkCheckBoxId').prop('checked', false);
    });
});

function deleteUser() {
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
    var userIds = [];
    var checkboxes = $('input[name="userid"]');
    checkboxes.filter(":checked").map(function () {
        userIds.push(this.value);
    }).get()

    $.ajax({
        type: "GET",
        contentType: 'application/json; charset=utf-8',
        url: "/user/delete",
        data: {
            userIds: userIds.toString()
        },
        dataType: 'json',
        timeout: 100000,
        done: function (data) {
        },
        statusCode: function () {
        },
        success: function (data) {
            console.log("SUCCESS: ", data);
            var result = "<h3> You delete user:  </h3>"
                + "<strong>Name:</strong> " + data.name;
            $("#ajax-response").html(result);
            //alert("削除しました。");
            window.location.href = "/user/list";
        },
        error: function (e) {
            alert("削除しました。");
            console.log("ERROR: ", e);
        }
    });
};

function search() {
    var query = '/user/list?cp=1';
    var limitItems = document.getElementById('pageSizeSelect').value;
    if (!limitItems) {
        limitItems = 5;
    }
    query += '&limit=' + limitItems;

    var searchValue = document.getElementById('search-box').value;
    if (searchValue) {
        query += '&search=' + searchValue;
    }
    window.location.replace(query);
}
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
        addViaAjax();
    }
}

//
function addViaAjax() {
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

//
// function reloadPage() {
//
// };

//
// $(function () {
//     $.fn.dataTable.ext.search.push(
//         function (settings, data, dataIndex) {
//             var d_min = new Date($('#min').val());
//             var m_min = d_min.getMonth() + 1;
//             var month_min = (m_min < 10 ? '0' : '') + m_min;
//             var day_min = (d_min.getDate() < 10 ? '0' : '') + d_min.getDate();
//             var year_min = d_min.getFullYear();
//             var date1_min = year_min + month_min + day_min;
//
//             var d_max = new Date($('#max').val());
//             var m_max = d_max.getMonth() + 1;
//             var month_max = (m_max < 10 ? '0' : '') + m_max;
//             var day_max = (d_max.getDate() < 10 ? '0' : '') + d_max.getDate();
//             var year_max = d_max.getFullYear();
//             var date2_max = year_max + month_max + day_max;
//
//             var d_value = new Date(data[0]);
//             var m_value = d_value.getMonth() + 1;
//             var month_value = (m_value < 10 ? '0' : '') + m_value;
//             var day_value = (d_value.getDate() < 10 ? '0' : '') + d_value.getDate();
//             var year_value = d_value.getFullYear();
//             var date_value = year_value + month_value + day_value;
//
//             var min = parseInt(date1_min, 10);
//             var max = parseInt(date2_max, 10);
//             var date = parseInt(date_value) || 0;
//             var post = parseInt($("#post").val(), 10);
//             var post_value = parseInt(data[2] || 0);
//
//             if (
//                 (isNaN(post) && isNaN(min) && isNaN(max)) ||
//                 (isNaN(post) && isNaN(min) && date <= max) ||
//                 (isNaN(post) && min <= date && isNaN(max)) ||
//                 (isNaN(post) && min <= date && date <= max) ||
//                 (post === post_value && isNaN(min) && isNaN(max)) ||
//                 (post === post_value && min <= date && isNaN(max)) ||
//                 (post === post_value && isNaN(min) && date <= max) ||
//                 (min <= date && date <= max && post === post_value)
//
//
//             ) {
//                 return true;
//             }
//             return false;
//
//
//         });
//
//     $(document).ready(function () {
//         var table = $('#listUser').DataTable();
//         $('#search').click(function () {
//             table.draw();
//         });
//
//     });
//
//
// });
//
// $(function () {
//     $('#listUser').DataTable({
//         'paging': true,
//         'lengthChange': true,
//         'searching': true,
//         'ordering': true,
//         'info': true,
//         'autoWidth': true,
//         'order': [],
//         "columnDefs": [{'orderable': false, 'targets': [0, 3]}]
//     })
// })

function search(){
    var query = '/user/list?cp=1';
    var limitItems = document.getElementById('pageSizeSelect').value;
    if (!limitItems) {
        limitItems = 5;
    }
    query += '&limit=' + limitItems;

    var searchValue = document.getElementById('search-box').value ;
    if (searchValue) {
        query += '&search=' + searchValue;
    }


    console.log('query: ', query);
    window.location.replace(query);
}
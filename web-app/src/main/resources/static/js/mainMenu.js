$(function() {
    $.fn.dataTable.ext.search.push(
        function( settings, data, dataIndex ) {
            var d_min = new Date($('#min').val());
            var m_min = d_min.getMonth() + 1;
            var month_min = (m_min < 10 ? '0' : '') + m_min;
            var day_min = (d_min.getDate() < 10 ? '0' : '') + d_min.getDate();
            var year_min = d_min.getFullYear();
            var date1_min = year_min + month_min + day_min ;

            var d_max = new Date($('#max').val());
            var m_max = d_max.getMonth() + 1;
            var month_max = (m_max < 10 ? '0' : '') + m_max;
            var day_max = (d_max.getDate() < 10 ? '0' : '') + d_max.getDate();
            var year_max = d_max.getFullYear();
            var date2_max = year_max + month_max + day_max ;

            var d_value = new Date(data[0]);
            var m_value = d_value.getMonth() + 1;
            var month_value = (m_value < 10 ? '0' : '') + m_value;
            var day_value = (d_value.getDate() < 10 ? '0' : '') + d_value.getDate();
            var year_value = d_value.getFullYear();
            var date_value = year_value + month_value + day_value;

            var min = parseInt( date1_min, 10 );
            var max = parseInt( date2_max, 10 );
            var date = parseInt(date_value) || 0;
            var post = parseInt($("#post").val(),10);
            var post_value = parseInt(data[2] || 0);

            if (
                ( isNaN( post ) && isNaN( min ) && isNaN(max)) ||
                ( isNaN( post ) && isNaN( min ) && date <= max) ||
                ( isNaN(post) && min <= date && isNaN( max )  ) ||
                ( isNaN(post) && min <= date && date <= max ) ||
                ( post === post_value && isNaN(min) && isNaN(max) ) ||
                ( post === post_value && min <= date && isNaN(max) ) ||
                ( post === post_value && isNaN(min) && date <= max) ||
                ( min <= date   && date <= max  && post === post_value)


            )
            {
                return true;
            }
            return false;


        });

    $(document).ready(function() {
        var table = $('#table-order').DataTable();
        $('#search').click( function() {
            table.draw();
        } );
        var table = $('#table-logicstic').DataTable();
        $('#search').click( function() {
            table.draw();
        } );
        $("#min").datepicker({
            format: 'yyyy/mm/dd'
        });
        $("#max").datepicker({
            format: 'yyyy/mm/dd'
        });
    } );


});
$(function () {
    $('#table-order').DataTable({
        'paging'      : true,
        'lengthChange': true,
        'searching'   : true,
        'ordering'    : true,
        'info'        : true,
    })
    $('#table-logicstic').DataTable({
        'paging'      : true,
        'lengthChange': true,
        'searching'   : true,
        'ordering'    : true,
        'info'        : true,
        'autoWidth'   : true
    })
})

 


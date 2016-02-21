/**
 * Created by Firstvan on 2016. 02. 20..
 */

$(document).ready(loadTable());

function loadTable() {
    $.get("/getAdminOpens", function(data){
        $("#open_order_content").empty().append(data);
    });
}

/**
 * Created by Firstvan on 2016. 02. 16..
 */
function deleteItem(itemno){
    $.get("/deleteOrder?id="+itemno, function(data){
        location.reload();
    });
    $("#dialog_"+itemno).dialog("close");
}

function closeDialog(itemno){
    $("#dialog_"+itemno).dialog("close");
}

function show_delete_dialog(itemno){
    $("#dialog_"+itemno).dialog({modal: true});
}

jQuery(document).ready(function($) {
    $("#opened_orders_table tr").each(function(i, e){
        $(e).children('td:not(:last)').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});

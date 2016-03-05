/**
 * Created by Firstvan on 2016. 02. 20..
 */
jQuery(document).ready(function($) {
    $("#opened_orders_table").find("tr").each(function(i, e){
        $(e).children('td:not(:nth-last-child(-n+2))').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});

function deleteItem(itemno){
    $.get("/deleteOrder?id="+itemno, function(data){
        loadTable();
    });
    $("#dialog_"+itemno).dialog("close");
}

function closeDialog(itemno){
    $("#dialog_"+itemno).dialog("close");
}

function show_delete_dialog(itemno){
    $("#dialog_"+itemno).dialog({modal: true});
}

function closeOrderDialog(itemno){
    $.get("/closeOrder?id="+itemno, function(data){
        if(data === '0')
        {
            loadTable();
        }
    });
    $("#close_dialog_"+itemno).dialog("close");
}

function closeDialogClose(itemno){
    $("#close_dialog_"+itemno).dialog("close");
}

function show_close_dialog(itemno){
    $("#close_dialog_"+itemno).dialog({modal: true}).prev(".ui-dialog-titlebar").css("background","limegreen");
}


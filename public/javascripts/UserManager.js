/**
 * Created by Firstvan on 2016. 02. 23..
 */

jQuery(document).ready(function($) {
    $("#users-table").find("tr").each(function(i, e){
        $(e).children('td:not(:last)').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});

function removeCustomer(id){
    $("#dialog_"+id).dialog({modal: true})
}

function deleteItem(id) {
    $("#dialog_"+id).dialog("close");
    $.get("/removeUser?id="+id, function(data){
        window.location.replace("/users");
    });
}

function closeDialog(itemno){
    $("#dialog_"+itemno).dialog("close");
}

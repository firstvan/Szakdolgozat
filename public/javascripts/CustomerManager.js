/**
 * Created by Firstvan on 2016. 02. 23..
 */
jQuery(document).ready(function($) {
    $("#customers-table").find("tr").each(function(i, e){
        $(e).children('td:not(:nth-last-child(-n+2))').click(function()
        {
            window.document.location = $(this).closest("tr").data("href");
        });
    });
});

$(document).ready(function(){
  index();
});

function index() {
    $.get("/customerTable", function(data){
        $("#customer_table").empty().append(data);
    });
}

function searchUser() {
    console.log($("#search_text").val());
    $.cookie("search_name", $("#search_text").val());
    index();
}

function paging(page) {
    $.cookie("actual_page", page);
    index();
}

function removeCustomer(id){
    $("#dialog_"+id).dialog({modal: true})
}

function deleteItem(id) {
    $("#dialog_"+id).dialog("close");
    $.get("/removeCustomer?id="+id, function(data){
        index();
    });
}

function closeDialog(itemno){
    $("#dialog_"+itemno).dialog("close");
}
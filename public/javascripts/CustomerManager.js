/**
 * Created by Firstvan on 2016. 02. 23..
 */

$(document).ready(function(){
  index();
  requestIndex();
  setInterval(function(){ requestIndex(); }, 60000);
});

function index() {
    $.get("/customerTable", function(data){
        $("#customer_table").empty().append(data);
    });
}

function requestIndex(){
    $.get("/requestCustomer", function(data){
        if(data) {
            $("#customer_request_container").css("display", "block");
            $("#customer_requestTable").empty().append(data);
        }
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
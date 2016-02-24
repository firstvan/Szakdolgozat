$(document).ready(function(){
    if (jQuery.cookie('list_size')) {
        $("#dropdown_label").text($.cookie("list_size"));
    }
});


var index_page = function(orderd, closed) {
    console.log(closed);
    $.get("/paginationAdmin?orderd=" + orderd + "&admin=true" + "&closed=" + closed, function(data){
        $("#product_page").empty().append(data);
    });
};

function paging(page, closed) {
    $.cookie("actual_page", page);
    index_page(true, closed);
}

function search(closed){
    $.cookie("search_item_name", $("#search_text").val());
    $.cookie("actual_page", 1);
    index_page(true, closed);
}

function setListSize(size, closed) {
    $.cookie("actual_page", 1);
    $.cookie("list_size", size);
    $("#dropdown_label").text(size);
    index_page(true, closed);
}

function show_confirm(id) {
    $("#dialog_"+id).dialog({modal: true}).prev(".ui-dialog-titlebar").css("background","limegreen");
}

function closeDialog(id) {
    $("#dialog_"+id).dialog("close");
}

function getItem(id, num, closed) {
    $.get("/updateCloseProd?id="+id+"&db="+num, function(data){
        closeDialog(id);
        if(data === '-1'){
            alert("hiba");
        }
        else {
            index_page(true, closed);
        }
    });
}

function getItemModal(id, ord) {
    var db = $("#darab"+id).val();
    var stock = parseInt($("#stock_"+id).text());
    $.get("/updateCloseProd?id="+id+"&db="+db, function(data){
        var nums = data.split(" ");
        var first = parseInt(nums[0]);
        var sec = parseInt(nums[1]);
        var newStock = 0;

        if(sec < 0)
            newStock = stock - sec;
        else
            newStock = stock - first;


        if(first === ord) {
                $("#deli_"+id).empty().append("<span class='glyphicon glyphicon-ok' style='color: limegreen;'></span>");
                $("#stock_"+id).empty().append(newStock);
                $("#stockM_"+id).empty().append(newStock);
            } else {
                $("#deli_"+id).empty().append(first + " db");
                $("#stock_"+id).empty().append(newStock);
                $("#stockM_"+id).empty().append(newStock);
            }
    });
    $("#modal_"+id).modal("hide");
}
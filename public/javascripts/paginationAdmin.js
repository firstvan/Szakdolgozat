$(document).ready(function(){
    if (jQuery.cookie('list_size')) {
        $("#dropdown_label").text($.cookie("list_size"));
    }
});


var index_page = function(orderd) {
    $.get("/paginationAdmin?orderd=" + orderd, function(data){
        $("#product_page").empty().append(data);
    });
};

function paging(page) {
    $.cookie("actual_page", page);
    index_page(true);
}

function search(){
    $.cookie("search_item_name", $("#search_text").val());
    $.cookie("actual_page", 1);
    index_page(true);
}

function setListSize(size) {
    $.cookie("actual_page", 1);
    $.cookie("list_size", size);
    $("#dropdown_label").text(size);
    index_page(true);
}

function closeProduct(id) {

}

function show_confirm(id) {
    $("#dialog_"+id).dialog({modal: true}).prev(".ui-dialog-titlebar").css("background","limegreen");
}

function closeDialog(id) {
    $("#dialog_"+id).dialog("close");
}

function getItem(id, num) {
    $.get("/updateCloseProd?id="+id+"&db="+num, function(data){
        closeDialog(id);
        if(data === '0'){
            index_page(true);
        }
        else {
            alert("hiba");
        }
    });
}

function getItemModal(id, ord) {
    var db = $("#darab"+id).val();
    $.get("/updateCloseProd?id="+id+"&db="+db, function(data){
        if(data === '0'){
            if(ord.toString() === db) {
                $("#deli_"+id).empty().append("<span class='glyphicon glyphicon-ok' style='color: limegreen;'></span>");
            } else {
                $("#deli_"+id).empty().append(db+ " db");
            }
        }
        else {
            alert("hiba");
        }
    });
    $("#modal_"+id).modal("hide");
}
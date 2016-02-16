$(document).ready(function(){
    if (jQuery.cookie('list_size')) {
        $("#dropdown_label").text($.cookie("list_size"));
    }
    $("#onlyOrderd").change(function(){
        if($(this).is(":checked")){
            $.cookie("actual_page", 1);
            $.cookie("search_item_name", "");
            index_page(true);
        }
        else{
            $.cookie("actual_page", 1);
            index_page(false);
        }
    });
});

var index_page = function(orderd) {
    $.get("/pagination?orderd=" + orderd, function(data){
        $("#product_page").empty().append(data);
    });
};

function paging(page) {
    $.cookie("actual_page", page);
    var ord = $("#onlyOrderd");
    if(ord.is(":checked")){
        index_page(true);
    } else {
        index_page(false);
    }
}

function search(){
    $.cookie("search_item_name", $("#search_text").val());
    $.cookie("actual_page", 1);
    var ord = $("#onlyOrderd");
    if(ord.is(":checked")){
        index_page(true);
    } else {
        index_page(false);
    }
}

function deleteItem(itemno){
    if($("#p_size").text() === "1"){
        var page = parseInt($.cookie("actual_page"));
        if(page > 1){
            $.cookie("actual_page", page - 1);
        }
    }
    $.get("/deleteItem?prodNo="+itemno, function(data){
        if(data == "0")
            if($("#onlyOrderd").is(":checked"))
                index_page(true);
            else
                index_page(false);
    });
    $("#dialog_"+itemno).dialog("close");
}

function closeDialog(itemno){
    $("#dialog_"+itemno).dialog("close");
}

function show_delete_dialog(itemno){
    $("#dialog_"+itemno).dialog({
        modal: true,
        buttons: [
            {
                class: "leftbutton",
                text: "Törlés",
                click: function() {
                    deleteItem(itemno);
                }
            },
            {
            text: "Mégse",
            click: function() {
                $( this ).dialog( "close" );
            }
        }]
    });
}

function setListSize(size) {
    $.cookie("actual_page", 1);
    $.cookie("list_size", size);
    $("#dropdown_label").text(size);
    if($("#onlyOrderd").is(":checked"))
        index_page(true);
    else
        index_page(false);
}

$("#search_text").keyup(function(event){
    if(event.keyCode == 13){
        search();
    }
});


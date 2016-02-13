$(document).ready(function(){
    $("#onlyOrderd").change(function(){
        if($(this).is(":checked")){
            index_page(true);
        }
        else{
            index_page(false);
        }
    });
});

var index_page = function(orderd) {
    $.get("/pagination?orderd=" + orderd, function(data){
        $("#product_page").empty();
        $("#product_page").append(data);
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

$("#search_text").keyup(function(event){
    if(event.keyCode == 13){
        search();
    }
});


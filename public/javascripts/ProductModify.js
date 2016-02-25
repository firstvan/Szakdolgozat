$(document).ready(function(){
    $.cookie("search_item_name", "");
    $.get("/pagingModify", function(data){
       $("#product_page").empty().append(data);
    });
});

function modifyProduct(id) {
    window.location.replace("/modifyProductForm?id="+id);
}

var index_page = function() {
    $.get("/pagingModify", function(data){
        $("#product_page").empty().append(data);
    });
};

function paging(page) {
    $.cookie("actual_page", page);
    index_page();
}

function search(){
    $.cookie("search_item_name", $("#search_text").val());
    $.cookie("actual_page", 1);
    index_page();
}


function setListSize(size) {
    $.cookie("actual_page", 1);
    $.cookie("list_size", size);
    index_page();
}

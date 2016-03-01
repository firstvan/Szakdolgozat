/**
 * Created by Firstvan on 2016. 02. 13..
 */
var name = "";
var code = "";
var user = 0;
function clearCookies(){
    $.cookie("actual_page", "1");
    $.cookie("search_item_name", "");
}
jQuery(document).ready(function($) {
    setInterval(nameChange, 1000);
    setInterval(codeChange, 1000);
    refreshClick();
});

function refreshClick(){
    $("#customertable tbody tr").each(function(i, e){
        $(e).children().click(function()
        {
            var user1 = $(this).closest("tr").children().first().html();
            if(user1 == user){
                console.log("juhu")
                $(this).closest("tr").css("background", "#FFFFFF");
                user = 0;
            } else {
                if(user != 0) {
                    $("#"+user).css("background", "#FFFFFF")
                }
                $(this).closest("tr").css("background", "#3071A9");
                user = user1;
            }
        });
    });
}

function nameChange(){
    if(name != $("#name").val()) {
        if(code != ""){
            code = "";
            $("#code").val("");
        }
        name = $("#name").val();
        $.get("/getCustomersByName?name=" + name, function (data) {
            $("#bodyoftable").empty().append(data);
            refreshClick();
        });
    }
}

function codeChange(){

    if(code != $("#code").val()) {
        if(name != ""){
            name = "";
            $("#name").val("");
        }
        code = $("#code").val();
        $.get("/getCustomersByCode?code=" + code, function (data) {
            $("#bodyoftable").empty().append(data);
            refreshClick();
        });
    }
}

function addOrder(){
    if(user == 0) {
        $("#errordiv").css("display", "block");
    } else {
        $.ajax({
            type: "POST",
            url: "/addorder",
            data: {code: user, date: $("#shippingDate").val()},
            success: successfnc,
            dataType: "text"
        });
    }
}

function successfnc(data){
    window.location.replace(data);
}
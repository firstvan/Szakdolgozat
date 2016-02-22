/**
 * Created by Firstvan on 2016. 02. 20..
 */
var availableDates = [];
var availableDates2 = [];

function available(date) {
    dmy = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
    if ($.inArray(dmy, availableDates) != -1) {
        return [true, "","Available"];
    } else {
        return [false,"","unAvailable"];
    }
}
function available2(date) {
    dmy = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
    if ($.inArray(dmy, availableDates2) != -1) {
        return [true, "","Available"];
    } else {
        return [false,"","unAvailable"];
    }
}

$(document).ready(function(){
    var date = $("#date_of_take").val();
    var dateParts = date.split(".");
    var date1 = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
    if(dateParts[1].charAt(0) === '0'){
        dateParts[1] = dateParts[1].replace(0, "");
    }

    availableDates.push(dateParts[2]+"-"+ dateParts[1]+"-"+dateParts[0]);
    $( "#date_of_take" ).datepicker({
        beforeShowDay: available, dateFormat: 'yy.mm.dd' });

    date = $("#delivery_date").val();
    dateParts = date.split(".");
    date1 = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
    if(dateParts[1].charAt(0) === '0'){
        dateParts[1] = dateParts[1].replace(0, "");
    }
    availableDates2.push(dateParts[2]+"-"+ dateParts[1]+"-"+dateParts[0]);
    $( "#delivery_date" ).datepicker({
        beforeShowDay: available2,
        dateFormat: 'yy.mm.dd', minDate: 0 });
});

jQuery(document).ready(function($) {
    $("#base_inform_header").click(function(){
        if($("#base_inform_content").is(":hidden")){
            $("#orderd_products_content").hide();
            $("#base_inform_content").show();
            refresh();
        } else {
            $("#base_inform_content").hide();
        }
    });

    $("#orderd_products_header").click(function(){
        if($("#orderd_products_content").is(":hidden")){
            $("#base_inform_content").hide();
            $("#orderd_products_content").show();
        } else {
            $("#orderd_products_content").hide();
        }
    });

    $("#onlyOrderd").prop('checked', true);

});

function refresh(){
    $.get("/getDTotal", function (data){
        $("#d_total").text(data + " ft");
    });
}
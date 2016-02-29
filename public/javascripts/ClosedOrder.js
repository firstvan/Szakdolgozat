/**
 * Created by Firstvan on 2016. 02. 24..
 */
$(document).ready(function () {


    var date = new Date();

   $("#date_of_start").datepicker({
      dateFormat: "yy.mm.dd"
   });

   $("#date_of_end").datepicker({
      dateFormat: "yy.mm.dd"
   });

    var start_date = new Date();
    start_date.setDate(date.getDate() - 10);

    var start_string = start_date.getFullYear() + ".";
    if(start_date.getMonth() + 1 < 10){
        start_string = start_string + "0";
    }

    start_string = start_string + (start_date.getMonth()+1) + ".";

    if(start_date.getDate() < 10){
        start_string = start_string + "0";
    }

    start_string = start_string + start_date.getDate();

    var start = $.cookie("start_date");
    if(!start) {
        $("#date_of_start").val(start_string);
        $.cookie("start_date", start_string.replace(/\./g, "-"));
    } else {
        $("#date_of_start").val(start.replace(/\-/g, "."));
    }

    var end_string = date.getFullYear() + ".";
    if(date.getMonth() + 1 < 10){
        end_string = end_string + "0";
    }

    end_string = end_string + (date.getMonth()+1) + ".";

    if(date.getDate() < 10){
        end_string = end_string + "0";
    }

    end_string = end_string + date.getDate();

    var end = $.cookie("end_date");
    if(!end) {
        $("#date_of_end").val(end_string);
        $.cookie("end_date", end_string.replace(/\./g, "-"));
    } else {
        $("#date_of_end").val(end.replace(/\-/g, "."));
    }

    $.get("/closedOrdersDate", function(data){
        $("#closed_table").empty().append(data);
    });
});

function refreshTableByDate(){
    $.cookie("start_date", $("#date_of_start").val().replace(/\./g, "-"));
    $.cookie("end_date", $("#date_of_end").val().replace(/\./g, "-"));
    $.get("/closedOrdersDate", function(data){
        $("#closed_table").empty().append(data);
    });
}

function searchOrder(){
    $.cookie("search_name", $("#search_text").val());
    $.get("/closedOrdersDate?start="+$("#date_of_start").val().replace(/\./g, "-")+"&end="+$("#date_of_end").val().replace(/\./g, "-"), function(data){
        $("#closed_table").empty().append(data);
    });
}

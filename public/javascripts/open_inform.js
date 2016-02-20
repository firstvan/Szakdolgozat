var availableDates = [];

function available(date) {
    dmy = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
    if ($.inArray(dmy, availableDates) != -1) {
        return [true, "","Available"];
    } else {
        return [false,"","unAvailable"];
    }
}

$(document).ready(function(){
    var date = $("#date_of_take").val();
    dateParts = date.split(".");
    var date1 = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
    if(dateParts[1].charAt(0) === '0'){
        dateParts[1] = dateParts[1].replace(0, "");
    }

    availableDates.push(dateParts[2]+"-"+ dateParts[1]+"-"+dateParts[0]);
    $( "#date_of_take" ).datepicker({
        beforeShowDay: available, dateFormat: 'yy.mm.dd' });

    $( "#delivery_date" ).datepicker({
        onSelect: function(){
            $("#delevery_save").removeClass("btn-default").addClass("btn-primary");
            $("#delevery_save").removeAttr("disabled");
        },
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
    $.get("/gettotal", function (data){
        $("#total").text(data + " ft");
    });
}

function saveDate(){
    var time = $("#delivery_date").val().replace(/\./g, "-");
    $.get("/updatetime?time="+time, function (data) {
       if(data === "0"){
           $("#delevery_save").removeClass("btn-primary").addClass("btn-default");
           $("#delevery_save").prop("disabled", true);
       } else {
           alert("Mentés sikertelen");
       }
    });
}
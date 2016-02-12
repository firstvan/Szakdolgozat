function check(){
    var usr = document.getElementById("usrname");
    var pwd = document.getElementById("pwd");

    if(usr.value.length == 0) {
        alert("Nem adott meg felhasználónevet.");
        return false;
    }

    if(pwd.value.length == 0) {
        alert("Nem adott meg jelszót.");
        return false;
    }

    return true;
}

$(document).ready(function(){
  $( "#shippingDate" ).datepicker({ dateFormat: 'dd-mm-yy'}).datepicker("setDate", new Date());
});



function validate(){
  var val=$("#custName").val();
  var obj=$("#customers").find("option[value='"+val+"']");

  if(obj != null && obj.length>0)
       return true;  // allow form submission
      else{
        alert("Nincs ilyen vásárló");
        return false;
      }
}

jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.document.location = $(this).data("href");
    });

    $("#base_inform_header").click(function(){
        $("#base_inform_content").fadeToggle();
    });

    $("#orderd_products_header").click(function(){
        $("#orderd_products_content").fadeToggle();
    });
});

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
    $( "#date_of_take" ).datepicker({ beforeShowDay: available,  dateFormat: 'yy.mm.dd' });
});

$(document).ready(function(){
    $( "#delivery_date" ).datepicker({ dateFormat: 'yy.mm.dd', minDate: 0 });
});

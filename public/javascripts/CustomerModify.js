/**
 * Created by Firstvan on 2016. 02. 23..
 */

function save_cust(id) {
    if ($("#payform").val() == "") {
      $("#alert_dialog").css("display", "block");
      return
    }

    $.ajax({
        type: "POST",
        url: "/saveCustomer",
        data: {id: id, name: $("#cust_name").val(), addr: $("#addr").val(), payment: $("#payform").val()},
        success: successfnc,
        dataType: "text"
    });
}

function successfnc(data) {
   window.location.replace("/customers")
}
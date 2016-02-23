/**
 * Created by Firstvan on 2016. 02. 23..
 */

function save_user(id) {
    var usr = $("#usr_name").val();
    var fname = $("#fname").val();

    if ($("#pass").val() != "") {
        if ($("#pass").val() != $("#pass_re").val()){
            $("#alert_dialog_pass").css("display", "block");
            return
        }
    }

    $.ajax({
        type: "POST",
        url: "/saveUser",
        data: {id: id, username: usr, fullname: fname, pass: $("#pass").val()},
        success: successfnc,
        dataType: "text"
    });
}

function successfnc(data){
    if(data === "-1"){
        $("#alert_dialog").css("display", "block");
    } else {
        window.location.replace("/users");
    }
}
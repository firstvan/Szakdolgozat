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

    var t = $("#type").val();

    if(id == 0 && t === ""){
        $("#noType").css("display", "block");
        return 0
    }

    $.ajax({
        type: "POST",
        url: "/saveUser",
        data: {id: id, username: usr, fullname: fname, pass: $("#pass").val(), type: t},
        success: successfnc,
        dataType: "text"
    });
}

function successfnc(data){
    if(data === "-1"){
        $("#alert_dialog").css("display", "block");
    } else {
        if(data==="2")
            window.location.replace("/users");
        else
            window.location.replace("/main");
    }
}
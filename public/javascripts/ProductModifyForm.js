function save_product(id) {
    var prodnum = $("#prodnum");
    if(prodnum.val() == null || prodnum.val() == ""){
       $("#nameofinput").text(" Termékszám ");
       $("#alert_dialog").css("display", "flex");
       return;
    }

    var ean = $("#ean");
    if(ean.val() == null || ean.val() == ""){
        $("#nameofinput").text(" Vonalkód ");
        $("#alert_dialog").css("display", "flex");
        return;
    }

    var prod_name = $("#prod_name");
    if(prod_name.val() == null || prod_name.val() == ""){
        $("#nameofinput").text("Termék név");
        $("#alert_dialog").css("display", "flex");
        return;
    }

    var prod_price = $("#prod_price");
    if(prod_price.val() == null || prod_price.val() == ""){
        $("#nameofinput").text("Ár");
        $("#alert_dialog").css("display", "flex");
        return;
    }

    var prod_stock = $("#prod_stock");
    if(prod_stock.val() == null || prod_stock.val() == ""){
        $("#nameofinput").text("Készlet");
        $("#alert_dialog").css("display", "flex");
        return;
    }

        $.ajax({
            type: "POST",
            url: "/updateProduct",
            data: {id: id, prodnum: prodnum.val(), ean: ean.val(),name: prod_name.val(), price: prod_price.val(), stock: prod_stock.val()},
            success: successfnc,
            dataType: "text"
        });
}

function successfnc(data){
    if(data ==='0'){
        window.location.replace("/productModify");
    } else {
        alert("hey");
    }
}
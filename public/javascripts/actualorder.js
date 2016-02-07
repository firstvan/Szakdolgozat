function addorder(itemNo, price) {
  var piece = $("#darab"+ itemNo).val();

  $.get("/additem?itemNo=" + itemNo + "&piece=" + piece + "&price=" + price, function(data){
    if(piece == 0){
      $("#piece-"+itemNo).text("");
    } else {
      $("#piece-"+itemNo).text(piece);
    }
    $("#" + itemNo).modal('hide');
  });
}
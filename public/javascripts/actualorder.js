function addorder(itemNo, price) {
  var piece = $("#darab"+ itemNo).val();

  $.get("/additem?itemNo=" + itemNo + "&piece=" + piece + "&price=" + price, function(data){
    if(piece == 0){
      $("#piece-"+itemNo).text("");
      $("#container_" + itemNo).empty();
    } else {
      $("#piece-"+itemNo).text(piece);

      if($.trim($("#container_" + itemNo).html())=='') {
          var tag = "<a id='id_" + itemNo + "' onclick='show_delete_dialog(" + itemNo + ")'>";
          tag += "<span class='glyphicon glyphicon-remove' style='color: red;' ></span> Törlés";
          tag += "</a>";
          $("#container_" + itemNo).append(tag);
      }
    }
    $("#" + itemNo).modal('hide');
  });
}
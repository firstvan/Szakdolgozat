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




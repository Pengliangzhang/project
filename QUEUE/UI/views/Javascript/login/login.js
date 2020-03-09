$(document).ready(function(){
    console.log("Logging ")

    var xhr=new XMLHttpRequest();
    xhr.addEventListener("load", getUserInfoListener);
    xhr.open('GET', requestURL+'/userInfo')
    xhr.send();

    function getUserInfoListener() {
        var response = JSON.parse(this.responseText);
        if(response.code == 1){
            window.location.replace("/admin");
        }
    }
})

function checkExistUser(){
    //email = $('#individual-signip-email').val();
    $.ajax({
        url: requestURL + $('form').attr("action"),
        method:"POST",
        data:$('#login').serialize(),
        success: function(data) {
            
            if(data.code == 1){
                window.location.replace("/admin");
            }else {
                window.location.replace("/");
            }			
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}




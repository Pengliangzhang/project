$(document).ready(function(){
    // console.log("Logging ")
    
    

    
})

function getUserInfoListener() {
    var response = JSON.parse(this.responseText);
    console.log(response);
    // if(response.code == 1){
    //     window.location.replace("/admin");
    // }
}

function checkExistUser(){
    $.ajax({
        url: requestURL + '/login',
        method:"POST",
        data:$('#user_login_form').serialize(),
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success: function(data) {
            if(data.code == 1){
                window.location.replace("/admin");
            }
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}



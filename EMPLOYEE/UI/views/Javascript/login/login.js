$(document).ready(function(){
    console.log("Logging ")

    var xhr=new XMLHttpRequest();
    xhr.addEventListener("load", getUserInfoListener);
    xhr.open('GET', requestURL+'/userInfo')
    xhr.send();

    $("#submit_login_form").click(function (event) {
        event.preventDefault()

        var form = $("#login_form"); 
        console.log("Clicked login button !")
        $.ajax({
            url: requestURL+'/',
            method: "get",
            data: $("#login_form").serialize(),
            success: function (data) {
                console.log(data);
            }
         });
    })
    
})

function getUserInfoListener() {
    var response = JSON.parse(this.responseText);
    console.log(response.code);
}
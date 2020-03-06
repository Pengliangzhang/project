$(document).ready(function(){
    console.log("Logging ")

    var xhr=new XMLHttpRequest();
    xhr.addEventListener("load", getUserInfoListener);
    xhr.open('GET', requestURL+'/userInfo')
    xhr.send();

    $("#submit_login_form").click(function (event) {
        event.preventDefault()

        var form = $("#login_form"); 
        var xhr1=new XMLHttpRequest();
        xhr1.addEventListener("load", getUserInfoListener);
        xhr1.open('POST', requestURL+'/servertest')
        xhr1.send(form.serialize());

        // console.log("Clicked login button !")
        // $.ajax({
        //     url: requestURL+'/',
        //     method: "post",
        //     data: $("#login_form").serialize(),
        //     success: function (data) {
        //         console.log(data);
        //     }
        //  });
    })
    
})

function getUserInfoListener() {
    var response = JSON.parse(this.responseText);
    console.log(response);
}

function checkExistUser(){
    //email = $('#individual-signip-email').val();
    $.ajax({
        url: requestURL + $('form').attr("action"),
        async:true,
        cache: false,
        method:"POST",
        data:$('#login').serialize(),
        success: function(data) {
			console.log(data)
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}
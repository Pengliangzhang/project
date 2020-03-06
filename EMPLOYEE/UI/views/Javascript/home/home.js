function loadUSERS() {
    var xhr=new XMLHttpRequest();
    xhr.addEventListener("load", getUserInfoListener);
    xhr.open('GET', requestURL+'/userInfo')
    xhr.send();
}

function getUserInfoListener() {
    var response = JSON.parse(this.responseText);
    console.log(response.code);
}

$(document).ready(function(){
    console.log("Logging data ")
   
    loadUSERS();
    
})


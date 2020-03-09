$(document).ready(function(){
    $("#input_delete_fob").val("");

    $("#deactivate_fob_form").submit(function (e) {
        event.preventDefault();
        var form = $(this);
        $.ajax({
            url: requestURL+form.attr('action'),
            method: form.attr('method'),
            data: form.serialize(),
            success: function (data) {
                if(data.code == 1){
                    window.location.replace("/deactivatefob");
                    $("#input_delete_fob").val("");
                }else{
                    $("#deactivate_fob_feedback").attr("placeholder", data.message);
                    $("#input_delete_fob").val("");
                }           
            }
         });
    })
})
$(document).ready(function(){
     $("#load_ticket_button").click(function () {
        $("#load_tickets_container").show();
        $("#verify_ticket_container").hide();
        $.ajax({
            url: requestURL + '/queryalltickets',
            method: "get",
            success: function (data) {
                $( "#load_tickets_table" ).empty();
                data = data.message;
                for(var i=1;i<=data.length; i++){
                    if(data[i-1].valid == 1){
                        data[i-1].valid = "Valid";
                    }else{
                        data[i-1].valid = "Used";
                    }
    
                     $('#load_tickets_table').append('<tr><td>' + i + '</td><td>' +data[i-1].id+'</td><td>'+data[i-1].email+'</td><td>'+data[i-1].value+'</td><td>'+ data[i-1].type + 
                     '</td><td>'+ data[i-1].expire + '</td><td>'+ data[i-1].valid +'</td></tr>');
    
                }
            }
         });
    });

    $("#verify_ticket_button").click(function () {
        $("#load_tickets_container").hide();
        $("#verify_ticket_container").show();
    });

    $("#activate_fob_form").submit(function (e) {
        event.preventDefault();
        $('#input_ticket_fob').val(ticketID);
        $.ajax({
            url: requestURL + '/activatefob',
            method:"POST",
            data:$('#activate_fob_form').serialize(),
            success: function(data) {
                if(data.code == 1){
                    $('#input_ticket_id').val('');
                    $('#input_register_fob').val('');
                    $("#verify_ticket_container").show();
                    $("#activate_fob_container").hide();
                    $("#verify_ticket_feedback").attr("placeholder", "Please scan a ticket ");
                    $("#register_fob_feedback").attr("placeholder", "Please tap a fob ");
                    ticketID = undefined;                
                }else{
                    $("#register_fob_feedback").attr("placeholder", data.message);
                    $('#input_register_fob').val('');
                }            
            },
            fail : function(data){
                console.log("Fail: " + data);
            }
        })
    })
})

var ticketID;

function submitVerifyTicket(){
    $.ajax({
        url: requestURL + '/verifytickets',
        method:"POST",
        data:$('#verify_ticket_form').serialize(),
        success: function(data) {
            $("#verify_ticket_feedback").attr("placeholder", data.message);
            if(data.code == 1){
                $("#verify_ticket_container").hide();
                $("#activate_fob_container").show();
                ticketID = $('#input_ticket_id').val();
                $("#input_register_fob").val("");
            }else{
                $('#input_ticket_id').val('');
            }
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}
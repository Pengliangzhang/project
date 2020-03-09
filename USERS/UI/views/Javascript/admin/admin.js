$(document).ready(function(){
    $.ajax({
        url: requestURL + '/userInfo',
        method:"GET",
        xhrFields:{
            withCredentials:true
        },
        crossDomain:true,
        success: function(data) {
            if(data.code == 1){
                $("#admin_button_container").show();
                
            }else{
                window.location.replace("/login");
            }
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
     
     $("#load_ticket_button").click(function () {
        $("#load_tickets_container").show();
        $("#buy_parking_container").hide();
        $("#buy_ticket_container").hide();
        $("#response_container").hide();
        $.ajax({
            url: requestURL + '/getusertickets',
            method: "get",
            xhrFields:{
                withCredentials:true
            },
            crossDomain:true,
            success: function (data) {
                if(data.code == 1){
                    $( "#load_tickets_table" ).empty();
                    data = data.message;
                    for(var i=1;i<=data.length; i++){
                        if(data[i-1].valid == 1){
                            data[i-1].valid = "Valid";
                        }else{
                            data[i-1].valid = "Used";
                        }
        
                        $('#load_tickets_table').append('<tr><td>' + i + '</td><td>' +data[i-1].id+'</td><td>'+data[i-1].email+'</td><td>'+data[i-1].value+'</td><td>'+ data[i-1].type + 
                        '</td><td>'+ data[i-1].expire + '</td><td>'+ data[i-1].valid +'</td><td>'+
                        '<button type="button" class="btn btn-info" data-toggle="modal" data-target="#exampleModal">Ticket</button>' +
                         '</td></tr>');
                    }
                }
            }
         });
    });

    $("#load_tickets_table").on('click','.btn',function(){
        // get the current row
        var currentRow=$(this).closest("tr");
        var id=currentRow.find("td:eq(1)").text(); // get current row 2nd TD
        $("#modal_qr").attr('src', requestURL+'/create_qrcode?id='+id)
        
   });

    $("#buy_ticket_button").click(function () {
        $("#load_tickets_container").hide();
        $("#buy_parking_container").hide();
        $("#buy_ticket_container").show();
        $("#response_container").hide();
    });

    $("#buy_parking_button").click(function () {
        $("#load_tickets_container").hide();
        $("#buy_parking_container").show();
        $("#buy_ticket_container").hide();
        $("#response_container").hide();
    });
})

var ticketID;

function submitBuyTicket(){
    $.ajax({
        url: requestURL + '/buytickets',
        method:"POST",
        data:$('#buy_ticket_form').serialize(),
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success: function(data) {
            if(data.code == 1){
                $("#load_tickets_container").hide();
                $("#buy_parking_container").hide();
                $("#buy_ticket_container").hide();
                $("#response_container").show();
                $("#response_p_1").html('<img src="" id="response_qr" alt="QR" height="200px" width="200px"></img> <br>' +
                                           '<p> You have a valid eTicket </p>' );
                $("#response_qr").attr('src', requestURL+'/create_qrcode?id='+data.message.id)
                console.log(data);
            } else {
                $("#load_tickets_container").hide();
                $("#buy_parking_container").hide();
                $("#buy_ticket_container").hide();
                $("#response_container").show();
                $("#response_p_1").html(data.message);
            }
            
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}

function submitBuyParking(){
    $.ajax({
        url: requestURL + '/buyparkingspot',
        method:"POST",
        data:$('#buy_parking_form').serialize(),
        xhrFields:{
            withCredentials:true
        },
        crossDomain:true,
        success: function(data) {
            $("#load_tickets_container").hide();
            $("#buy_parking_container").hide();
            $("#buy_ticket_container").hide();
            $("#response_container").show();
            $("#response_p_1").html(data.message);
        },
        fail : function(data){
            console.log("Fail: " + data);
        }
    })
}
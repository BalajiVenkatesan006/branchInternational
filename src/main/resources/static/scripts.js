var stompClient = null;
var globalUserName = null;
var notificationCount = 0;
var userType = null;
var toUser = null;
var selectedUser = null;
const url = 'http://localhost:8085';

$(document).ready(function() {
    console.log("Index page is ready");

    $('#registerationForm').hide();
    $('#privaterow').hide();
    $('#replyrow').hide();
    $('#refreshMessages').hide();
    $('#activeUsersRow').hide();
    $('#agent').click(function(){
        userType = "AGENT";
        $('#userTypeSelection').hide();
        $('#registerationForm').show();
    });
    $('#customer').click(function(){
          userType = "CUSTOMER";
          $('#userTypeSelection').hide();
          $('#registerationForm').show();
     });
    $("#registerUser").click(function() {
            registration(userType);
        });
    $("#send").click(function() {
        sendMessage();
    });

     $("#refreshBtn").click(function() {
         fetchUsersMessageHistory(globalUserName);
     });

    $("#logoutUser").click(function() {
            logout(globalUserName);
    });

    $("#send-private").click(function() {
        if(userType == "CUSTOMER"){
           sendNewMsg(globalUserName, globalUserName);
        }
        else{
           sendNewMsg(globalUserName, selectedUser);
        }
    });

    $("#refreshActiveUsersBtn").click(function() {
       refreshActiveUsersBtnClick();
    });

    $("#notifications").click(function() {
        resetNotificationCount();
    });

     $("#chat-done").click(function() {
            charDoneClick();
     });
});

function connect(username) {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
       // updateNotificationDisplay();
        stompClient.subscribe('/topic/messages/'+username, function (message) {
            showMessage(JSON.parse(message.body).messageContent);
        });

        if(userType == "AGENT"){
            stompClient.subscribe('/topic/activeUsers', function (questioningUsers) {
               fetchQuestioningUsers(questioningUsers)
            });
        }

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            showMessage(JSON.parse(message.body).content);
        });

        stompClient.subscribe('/topic/global-notifications', function (message) {
            notificationCount = notificationCount + 1;
            updateNotificationDisplay();
        });

        stompClient.subscribe('/user/topic/private-notifications', function (message) {
            notificationCount = notificationCount + 1;
            updateNotificationDisplay();
        });
    });
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage() {
    console.log("sending message");
    stompClient.send("/ws/message", {}, JSON.stringify({'messageContent': $("#message").val()}));
}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/private-message", {}, JSON.stringify({'messageContent': $("#private-message").val()}));
}

function updateNotificationDisplay() {
    if (notificationCount == 0) {
        $('#notifications').hide();
    } else {
        $('#notifications').show();
        $('#notifications').text(notificationCount);
    }
}

function resetNotificationCount() {
    notificationCount = 0;
    updateNotificationDisplay();
}


function registration(userType) {
    let userName = document.getElementById("userName").value;
    $.get(url + "/registration/" + userName+"/"+userType, function (response) {
       if(response.success){
          globalUserName = userName;
          connect(globalUserName);
          if(userType == "CUSTOMER"){
              $('#refreshMessages').show();
              $('#replyrow').show();
              $('#privaterow').show();
          }
          else{
              $('#activeUsersRow').show();
          }
       }
       else if(!response.success && response.errorMessage == "User Exists"){
            login(userName);
       }

    }).fail(function (error) {
        if (error.status === 400) {
            alert("Login is already busy!");
        }
    })
}

function login(userName){
   $.get(url + "/login/" + userName+"/"+userType, function (response) {
          if(response.success){
             alert("login success");
             globalUserName = userName;
             connect(userName);
             if(userType == "CUSTOMER"){
                   $('#refreshMessages').show();
                   $('#replyrow').show();
                   $('#privaterow').show();
             }
             else{
                   $('#activeUsersRow').show();
             }
          }
          else{
             alert(response.errorMessage);
          }
       }).fail(function (error) {
           if (error.status === 400) {

           }
       })
}

function logout(userName){
   $.get(url + "/logout/" + userName+"/"+userType, function (response) {
             if(response.success){
                alert("Logout success");
                $('#privaterow').hide();
                $('#replyrow').hide();
                $('#refreshMessages').hide();
                $('#activeUsersRow').hide();
                stompClient.disconnect(function(frame) {
                  console.log("STOMP client succesfully disconnected.");
                 });
             }
             else{
                alert(response.errorMessage);
             }
          }).fail(function (error) {
              if (error.status === 400) {

              }
          })
}


function sendNewMsg(fromUser,toUser) {
    stompClient.send("/app/chat/" + fromUser+"/"+toUser, {},JSON.stringify({'messageContent': document.getElementById("private-message").value}) );
}

function fetchUsersMessageHistory(passedUserName) {
    $("#messages").empty();
    $.get(url + "/fetchMessages/"+passedUserName, function (response) {
        let users = response;
        let usersTemplateHTML = "";
        if(response.success){
           for(let i=0; i< response.messages.length ; i++){
               let msg = response.messages[i];
               if(msg.messageOwner == "CUSTOMER"){
                  showMessage("CUSTOMER : "+msg.userName+"  Msg:"+msg.message);
               }
               else{
                  showMessage("AGENT: "+msg.userName+" Msg: "+msg.message);
               }
           }
        }
//        for (let i = 0; i < users.length; i++) {
//
//        }
       // $('#usersList').html(usersTemplateHTML);
    });
}


function fetchQuestioningUsers(questioningUsers){
    refreshActiveUsersBtnClick();
}

function refreshActiveUsersBtnClick(){
   $('#activeUserscontainer').empty();

   $.get(url + "/fetchUsers", function (response) {
           let users = response;
           let usersTemplateHTML = "";
           if(response.success){
              for(let i=0; i< response.activeUsers.length ; i++){
                  let msg = response.activeUsers[i];
                  appendActiveUser(msg);
              }
           }
   //        for (let i = 0; i < users.length; i++) {
   //
   //        }
          // $('#usersList').html(usersTemplateHTML);
       });
}

function appendActiveUser(value){
 $('#activeUserscontainer')
                .append(`<input type="radio" id="${value}" onclick="activeUsersOnClick(this.id)" name="contact" value="${value}">`)
                .append(`<label for="${value}">${value}</label></div>`)
                .append(`<br>`);
}

function activeUsersOnClick(clickedId){
   toUser = document.getElementById(clickedId).value;
   $.get(url + "/updateAgentToUser/"+toUser+"/"+globalUserName, function (response) {
              let users = response;
              let usersTemplateHTML = "";
              if(response.success){
              selectedUser = toUser;
                alert("This user is assigned to you.");
                $('#replyrow').show();
                $('#privaterow').show();
                $('#chat-done').hide();
                fetchUsersMessageHistory(document.getElementById(clickedId).value)
              }
              else{
                alert("The user is already assigned to an agent");
              }
   });
}


function charDoneClick(){
   $.get(url + "/chatDone/"+globalUserName, function (response) {
                 let users = response;
                 let usersTemplateHTML = "";
                 if(!response.success){
                    alert("chat done function failed");
                 }
      });
}
# chat
Backend for simple chat application

# endpoints
Stomp/Socket - /ws

Stomp/Subscribe - /user/queue/messages

Stomp/Send - /chat/ws

Load paginated chat history - /previous/{chatId}/{fromUname}/{pageId}

Create new group chat - /newchat

Get Chat entity - /chatid/{chatName}

# documentation

For non-Stomp endpoints you can check swagger/openapi generated docs on /swagger-ui/index.html

### /ws
Create socket and connect to STOMP client over this socket. Example:
```
var socket = new SockJS('http://localhost:8080/ws');
var stompClient = Stomp.over(socket);
```
### /user/queue/messages
Connect and subscribe to created STOMP client with "username" header containing userId. Example:
```
stompClient.connect({username: fromUname}, function (frame) {
  console.log('Connected: ' + frame);
    stompClient.subscribe('/user/queue/messages', function (output) {
    });
});
```

### /chat/ws
Send message to this STOMP destination endpoint. Message need to be in json format and follow next structure(For one-to-one chat just use UserId of receiver):
```
{
    "toUname": "chatName",
    "content": "text-msg",
    "isGroup": true
}
```
STOMP endpoints have folloing exceptions
SafeHTML - throws when malformed html or js detected in message and returns "Malformed message detected" text back to sender

WrongValue - throws when trying to send message to non-existing group chat and returns "Chat with this name doesnt exist" text back to sender

BrokenMessage - Handle all other cases and returns error with trace back to sender

Here's full example of STOMP client implementation using SockJS
```
var stompClient = null;
    var fromUname = $("#fromUname").val();
    var chatName = $("#chatName").val();
    var socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({username: fromUname}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/messages', function (output) {
            });
        });
        
        function send() {
                var content = $("#message").val();
                console.log(content);
                stompClient.send("/chat/ws", {'username': fromUname},
                        JSON.stringify({
                        'toUname': chatName,
                        'content': content,
                        'isGroup': false}));                
                $("#message").val("");
        }
```




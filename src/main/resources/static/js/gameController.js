let socket = null;
let sessionId = null;


const GameMessage = function (command, message) {
    this.command = command;
    this.message = message;
}


let onopen = function () {
    showMessage("Connection opened");
}

let onSocketMessage = function (event) {
    let gameMessage = JSON.parse(event.data);
    switch (gameMessage.command) {
        case "getUserId":
            socket.send(Base.convertObjectToString(new GameMessage("getUserId", sessionId)));
            showMessage("Send UserId");
            break;
        case "info":
            showMessage(gameMessage.message);
            break;
        case "board":
            if (littleCircuitContext.getContext() != null) littleCircuitUpdate(littleCircuitContext, gameMessage.message)
            break;
        case "error":
            showMessage("Socket Error: " + gameMessage.message);
            break;
        case "master" :
            showMessage(gameMessage.message);
            break;
        default :
            showMessage("Socket Error: unknown command " + gameMessage.command);
    }
};

let onerror = function (e) {
    showMessage(e.toString());
}

let onclose = function () {
    showMessage('Connection closed');
};

function showMessage(message) {
    $('#littleCircuitInfo').append("<tr><td>" + message + "</td></tr>");
    let rowCount = $('table > tbody tr').length;
    // Go to the end
    $('#messagesTable').animate({
        scrollTop: $('#littleCircuitInfo').height()
    }, 500);
}


function onWebMessage(text) {
    let gameMessage = Base.convertDataToObject(text);
    switch (gameMessage.command) {
        case "error" :
            showMessage("Web Error: " + gameMessage.message);
            break;
        case "startSession":
            sessionId = gameMessage.message;
            socket = new Base.Socket('http://localhost:8080/gameMessageHandler', onopen, onSocketMessage, onerror, onclose);
            if (socket.connect() != true) showMessage("Socket connection error");
            break;
        case "info":
            showMessage(gameMessage.message);
            break;
        default :
            showMessage("Web Error: unknown command " + gameMessage.command);
    }

}

window.onload = function () {
    Base.sendPostRequest("/session/littleCircuit", "onload", onWebMessage);
}

$(document).ready(function () {
    //socket = new Base.Socket('http://localhost:8080/gameMessageHandler', onopen, onmessage, onerror, onclose);

    $('#webSocketConnect').click(function () {
        socket.connect();
    });

    $('#webSocketDisconnect').click(function () {
        socket.disconnect();
    });

    $('#webSocketSend').click(function () {
        socket.send($('#webSocketMessage').val());
        $('#webSocketMessage').val('');
    });

    $('#playLittleCircuit').click(function () {
        Base.sendPostRequest("/session/littleCircuit", "start", onWebMessage);
    });

    $('#exitLittleCircuit').click(function () {
        Base.sendPostRequest("/session/littleCircuit", "exit", onWebMessage);
    });

    $('#throwADice').click(function () {
        socket.send(Base.convertObjectToString(new GameMessage("check", null)));
        socket.send(Base.convertObjectToString(new GameMessage("dice", null)));
    });

});







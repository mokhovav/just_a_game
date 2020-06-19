let onopen = function () {
    showWebSocketMessage('Connection opened');
}

let onmessage = function (event) {
    showWebSocketMessage('Receive: ' + JSON.parse(event.data).message);
};

let onerror = function (e) {
    showWebSocketMessage(e.toString());
}

let onclose = function () {
    showWebSocketMessage('Connection closed');
};

function showWebSocketMessage(message) {
    $('#webSocketMessages').append("<tr><td>" + message + "</td></tr>");
}

$(document).ready(function () {
    let socket = new Base.Socket('http://localhost:8080/gameMessageHandler', onopen, onmessage, onerror, onclose);
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
});



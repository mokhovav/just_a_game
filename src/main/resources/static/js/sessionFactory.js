function wantToPlay(context) {
    Base.sendPostRequest(context,"/session","littleCircuit",waitToPlay);
}


function waitToPlay(context, text) {
    document.getElementById("playInfo").innerHTML = text;
}
function boardInit (context) {
    drawLine(context, 100,100,200,100, "gray",80);
    drawCircle(context,100,100,40,"red", 0.8);
    drawCircle(context,150,100,40,"green", 0.8);
    drawCircle(context,200,100,40,"blue", 0.8);
   // sendPostRequest(context,"/test","firstBoard",boardUpdate);
}
function boardUpdate(context, text){
    document.getElementById("demo2").innerHTML = "text";
}

function webgl2Init(gl){
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.clear(gl.COLOR_BUFFER_BIT);
}

/*****************************************************************************/
//little circuit
function littleCircuitInit (context) {
    sendPostRequest(context,"/test","littleCircuit",littleCircuitUpdate);
}

function littleCircuitUpdate(context, text) {
    drawLinks(context, text,"64,128,64",40);
    drawCircles(context, text, 20);
    drawItems(context, text);
}

function throwADie(context){
    sendPostRequest(context, "/roll","dice", setDice)
}

function setDice(context, text){
    //document.getElementById("dice").innerHTML = text;
    littleCircuitUpdate(context, text);
}

function drawItems(context, text) {
    let board = JSON.parse(text);
    let length;
    let i;
    length = board.ITEMS.length;
    for(i =0; i < length; i++) {
        let str1 = board.ITEMS[i].position;
        let a = str1.indexOf(";");
        let b = str1.lastIndexOf(";");
        drawCircle(context,str1.substr(0,a),str1.substr(a+1,b-a-1),10,"black", 1.0);
    }

}
/*****************************************************************************/

/*****************************************************************************/
// Monopoly
function MonopolyInit (context) {
    sendPostRequest(context,"/test","Monopoly",MonopolyUpdate);
}

function MonopolyUpdate(context, text) {
    drawLinks(context, text,"black",20);
    drawRectangles(context, text, 80, 80)
}
/*****************************************************************************/

/*****************************************************************************/
// Chess
function ChessInit(context) {
    sendPostRequest(context,"/test","Chess",ChessUpdate);
}

function ChessUpdate(context, text) {
    drawRectangle(context,0,0,900, 900,"maroon",1.0);
    drawLinks(context, text,"64,128,64",10);
    drawRectangles(context, text, 80, 80)
}
/*****************************************************************************/

/*****************************************************************************/
// Canvas service functions
const Context = function (name, id = "2d", attributes) {
    this.name = name;
    this.id = id;
    this.attributes = attributes;
    return this.getContext();
};

Context.prototype.getCanvas = function(){
    if (typeof(this.canvas) == "undefined") {
        this.canvas = document.getElementById(this.name);
        if(!this.canvas) {
            console.log("Failed to retrieve the <canvas> element");
            return null;
        }
        console.log("Create new canvas for " + this.name);
    }
    return this.canvas ;
}

Context.prototype.getContext = function () {
    if (typeof (this.context) == "undefined") {
        if (this.getCanvas() == null) return null;
        this.context = this.canvas.getContext(this.id, this.attributes);
        console.log("Create new " + this.id + " context for " + this.name);
    }
    return this.context;
}
/*****************************************************************************/

/*****************************************************************************/
// Functions for drawing simple lines
function drawLine(ctx,fromX,fromY,toX,toY, color="black", thickness = 1){
    ctx.beginPath();
    ctx.moveTo(fromX,fromY);
    ctx.lineTo(toX,toY);
    ctx.lineWidth = thickness;
    ctx.strokeStyle = getRGBColor(color);
    ctx.stroke();
}
/*****************************************************************************/

/*****************************************************************************/
// Functions for drawing simple shapes
function drawCircle(ctx,x,y,r,color="green", alpha=1.0){
    ctx.beginPath();
    ctx.arc(x,y,r,0,Math.PI*2,true);
    ctx.fillStyle = getRGBAColor(color, alpha);
    ctx.fill();
}

function drawRectangle(ctx,x,y,width,height,color,alpha){
    ctx.fillStyle =getRGBAColor(color,alpha);
    ctx.fillRect(x,y,width,height);
}
/*****************************************************************************/

/*****************************************************************************/
// Functions for drawing a board
function drawLinks(context, text, color, thickness){
    let board = JSON.parse(text);
    let length;
    let i;
    length = board.LINKS.length;
    for(i =0; i < length; i++) {
        let str1 = board.LINKS[i].from;
        let a = str1.indexOf(";");
        let b = str1.lastIndexOf(";");
        let str2 = board.LINKS[i].to;
        let c = str2.indexOf(";");
        let d = str2.lastIndexOf(";");
        drawLine(context, str1.substr(0,a),str1.substr(a+1,b-a-1),str2.substr(0,c),str2.substr(c+1,d-c-1), color,thickness);
    }
}

function drawRectangles(context, text, width = 100, height = 100) {
    let board = JSON.parse(text);
    let i;
    length = board.FIELDS.length;
    for(i =0; i < length; i++){
        let str = board.FIELDS[i].properties.position;
        let a = str.indexOf(";");
        let b = str.lastIndexOf(";");
        drawRectangle(context,Number(str.substr(0,a)) - width/2,Number(str.substr(a+1,b-a-1)) - height/2,width,height,board.FIELDS[i].properties.color,1.0);
    }
}

function drawCircles(context, text, radius = 50) {
    let board = JSON.parse(text);
    let i;
    length = board.FIELDS.length;
    for(i =0; i < length; i++){
        let str = board.FIELDS[i].properties.position;
        let a = str.indexOf(";");
        let b = str.lastIndexOf(";");
        drawCircle(context,str.substr(0,a),str.substr(a+1,b-a-1),radius,board.FIELDS[i].properties.color, 1.0);
    }
}
/*****************************************************************************/

/*****************************************************************************/
// Returns the color code by its name.
// Instead of the name, you can use the combination of "R,G,B"
function getRGBAColor(color, alpha){
    let rgb = getRGBColor(color);
    return "rgba("+ rgb.substr(4, rgb.length - 5) + ","+alpha+")";
}

function getRGBColor(color){
    switch (color) {
        case "white" : return "rgb(255,255,255)";
        case "silver" : return "rgb(192,192,192)";
        case "gray" : return "rgb(128,128,128)";
        case "black" : return "rgb(0,0,0)";
        case "maroon" : return "rgb(128,0,0)";
        case "red" : return "rgb(255,0,0)";
        case "orange" : return "rgb(255,165,0)";
        case "yellow" : return "rgb(255,255,0)";
        case "olive" : return "rgb(128,128,0)";
        case "lime" : return "rgb(0,255,0)";
        case "green" : return "rgb(0,128,0)";
        case "aqua" : return "rgb(0,255,255)";
        case "blue" : return "rgb(0,0,255)";
        case "navy" : return "rgb(0,0,128)";
        case "teal" : return "rgb(0,128,128)";
        case "fuchsia" : return "rgb(255,0,255)";
        case "purple" : return "rgb(128,0,128)";
        default: return "rgb("+color+")";
    }
}
/*****************************************************************************/

/*****************************************************************************/
// Functions for sending and receiving messages using the POST method
function sendPostRequest(context, address="/", message, func) {
    let xmlHttp;
    if (window.XMLHttpRequest) {
        // code for modern browsers
        xmlHttp = new XMLHttpRequest();
    } else {
        // code for old IE browsers
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlHttp.open("POST", address+"/" + JSON.stringify(message), true);
    xmlHttp.send();

    xmlHttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            func(context, this.responseText);
        }
    }
}
/*****************************************************************************/
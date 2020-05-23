function boardInit (context) {
    Inspiration.drawLine(context, 100,100,200,100, "gray",80);
    Inspiration.drawCircle(context,100,100,40,"red", 0.8);
    Inspiration.drawCircle(context,150,100,40,"green", 0.8);
    Inspiration.drawCircle(context,200,100,40,"blue", 0.8);
}
function boardUpdate(context, text){
    document.getElementById("demo2").innerHTML = "text";
}

function webgl2Init(context){
    let ctx = context.getContext();
    ctx.clearColor(0.0, 0.0, 0.0, 1.0);
    ctx.clear(ctx.COLOR_BUFFER_BIT);
}

/*****************************************************************************/
//little circuit
function littleCircuitInit (context) {
    /*Hex    RGB
#011f4b    (1,31,75)
#03396c    (3,57,108)
#005b96    (0,91,150)
#6497b1    (100,151,177)
#b3cde0    (179,205,224)
*/
    Inspiration.sendPostRequest(context,"/test","littleCircuit",littleCircuitUpdate);
}

function littleCircuitUpdate(context, text) {
    let board = Inspiration.convertDataToObject(text);
    document.getElementById("dice").innerHTML = board.MESSAGES.dice;
    Inspiration.drawRectangle(context,0,0,1600, 250,"1,31,75",1.0);


    Inspiration.drawLinks(context, board,"64,128,64",40);
    Inspiration.drawCircles(context, board, 20);

    let length;
    let i;
    length = board.ITEMS.length;
    for(i =0; i < length; i++) {
        let str1 = board.ITEMS[i].position;
        let a = str1.indexOf(";");
        let b = str1.lastIndexOf(";");
        Inspiration.drawCircle(context,
            Number(str1.substr(0,a)) + Number(board.ITEMS[i].properties.offsetX),
            Number(str1.substr(a+1,b-a-1)) + Number(board.ITEMS[i].properties.offsetY),
            10,
            board.ITEMS[i].properties.color, 1.0);
        Inspiration.drawCircle(context, 40, 140 + i*25, 10, board.ITEMS[i].properties.color );

        let ctx = context.getContext();
        ctx.fillStyle = "#FFF";
        ctx.font = "italic 10pt Arial";
        ctx.fillText(board.ITEMS[i].name, 60, 145 + i*25);


    }
}

function throwADie(context){
    Inspiration.sendPostRequest(context, "/roll","dice", setDice)
}

function setDice(context, text){
    littleCircuitUpdate(context, text);
}

function newGame(context){
    Inspiration.sendPostRequest(context, "/roll","newGame", setNewGame)
}

function setNewGame(context, text){
    Inspiration.sendPostRequest(context,"/test","littleCircuit",littleCircuitUpdate);
}
/*****************************************************************************/

/*****************************************************************************/
// Monopoly
function MonopolyInit (context) {
    Inspiration.sendPostRequest(context,"/test","Monopoly",MonopolyUpdate);
}

function MonopolyUpdate(context, text) {

    let board = Inspiration.convertDataToObject(text);
    document.getElementById("dices").innerHTML = board.MESSAGES.dice;
    Inspiration.drawLinks(context, board,"black",20);
    Inspiration.drawRectangles(context, board, 80, 80);
    Inspiration.drawItems(context, board);

}

function throwMonopolyDices(context){
    Inspiration.sendPostRequest(context, "/roll","dices", setDices)
}

function setDices(context, text){

    MonopolyUpdate(context, text);
}
/*****************************************************************************/

/*****************************************************************************/
// Chess
function ChessInit(context) {
    Inspiration.sendPostRequest(context,"/test","Chess",ChessUpdate);
}

function ChessUpdate(context, text) {
    let board = Inspiration.convertDataToObject(text);
    Inspiration.drawRectangle(context,0,0,900, 900,"maroon",1.0);
    Inspiration.drawLinks(context, board,"64,128,64",10);
    Inspiration.drawRectangles(context, board, 80, 80)
}
/*****************************************************************************/
<!DOCTYPE html>
<html lang="en" xmlns="">
<head>
    <title>Draw a blue rectangle</title>
    <script src="js/boardArea.js"></script>
</head>

<body>
    <noscript>Sorry, your browser does not support JavaScript!</noscript>
    <div>
        <canvas id="boardArea" width="800" height="200">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <canvas id="boardArea2" width="800" height="200">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <canvas id="LittleCircuit" width="1800" height="200">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <button name="throwADie" onclick="throwADie(ctx3);">Dice</button>
        <p id="dice"></p>
    </div>
    <div>
        <canvas id="Monopoly" width="1000" height="1000">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <canvas id="Chess" width="1000" height="1000">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <script>
        ctx1 = new Context("boardArea");
        ctx2 = new Context("boardArea2", "webgl2", { antialias: false });
        ctx3 = new Context("LittleCircuit");
        ctx4 = new Context("Monopoly");
        ctx5 = new Context("Chess");
        if (ctx1 != null) boardInit(ctx1);
        if (ctx2 != null) webgl2Init(ctx2);
        if (ctx3 != null) littleCircuitInit(ctx3);
        if (ctx4 != null) MonopolyInit(ctx4);
        if (ctx5 != null) ChessInit(ctx5);

    </script>
    <p id="demo2"></p>
    <p id="demo"></p>
</body>
</html>
<!DOCTYPE html>
<html lang="en" xmlns="">
<head>
    <title>Draw a blue rectangle</title>
    <script src="js/boardArea.js"></script>
    <script src="js/inspiration.js"></script>
    <script src="js/WEBGL.js"></script>

</head>

<body>
    <noscript>Sorry, your browser does not support JavaScript!</noscript>
    Little Circuit game:
    <div>
        <button name="NewGame" onclick="newGame(ctx3);">NewGame</button>
    </div>

    <div>
        <canvas id="LittleCircuit" width="1800" height="250">
            Please use a browser that supports "canvas"
        </canvas>
    </div>

    <div>
        <button name="throwADie" onclick="throwADie(ctx3);">Dice</button>
        <p id="dice">0</p>
    </div>

    <div>
        <canvas id="boardArea" width="800" height="200">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <canvas id="webgl" width="800" height="200">
            Please use a browser that supports "canvas"
        </canvas>
    </div>


    <div>
        <canvas id="Monopoly" width="1000" height="1000">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <div>
        <button name="throwMonopolyDices" onclick="throwMonopolyDices(ctx2);">Dices</button>
        <p id="dices"></p>
    </div>
    <div>
        <canvas id="Chess" width="1000" height="1000">
            Please use a browser that supports "canvas"
        </canvas>
    </div>
    <script>
        ctx1 = new Inspiration.Context("boardArea");
        if (ctx1.getContext() != null) boardInit(ctx1);
        WEBGLInit("webgl");

        ctx2 = new Inspiration.Context("Monopoly");
        if (ctx2.getContext() != null) MonopolyInit(ctx2);

        ctx3 = new Inspiration.Context("LittleCircuit");
        if (ctx3.getContext() != null) littleCircuitInit(ctx3);
    </script>


    <p id="demo2"></p>
    <p id="demo"></p>
</body>
</html>
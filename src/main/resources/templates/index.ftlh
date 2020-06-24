<#import "parts/projectPage.ftlh" as common>
<@common.head title = "Games">
    <link rel="stylesheet" href="css/index.css">
</@common.head>

<@common.body navBarTitle = "justAGame">
    <noscript>
        <h2 style="color: #ff0000">
            Seems your browser doesn't support Javascript!
            Websocket relies on Javascript being enabled.
            Please enable Javascript and reload this page!
        </h2>
    </noscript>

    <script src="js/base.js"></script>
    <script src="js/boardArea.js"></script>
    <script src="js/inspiration.js"></script>
    <script src="js/WEBGL.js"></script>
    <script src="js/stompWebSocket.js"></script>
    <script src="js/gameController.js"></script>

    Little Circuit game:
    <div>
        <button id="playLittleCircuit">PLAY</button>
        <button id="exitLittleCircuit">Exit</button>
    </div>

    <div>
        <canvas id="LittleCircuit" width="1800" height="250">
            Please use a browser that supports "canvas"
        </canvas>
    </div>

    <div>
        <button id="throwADice">Dice</button>
        <p id="diceInfo">0</p>
    </div>

    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
                <div class="table-responsive" >
                    <table class="table table-striped table-hover table-condensed table-sm">
                        <thead>
                        <tr>
                            <th width="25%">Messages</th>
                        </tr>
                        </thead>
                    </table>

                    <div class="scrollable bodyContainer" id="messagesTable">
                        <table class="table table-hover table-striped table-condensed table-scrollable table-sm">
                            <tbody id="littleCircuitInfo">
                            <!-- add rows here, specifying same widths as in header, at least on one row -->
                            </tbody>
                        </table>
                    </div>
                    <table class="table table-hover table-striped table-condensed">
                        <tfoot>
                        <!-- add your footer here... -->
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
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
    <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <script>
        let littleCircuitContext = new Inspiration.Context("LittleCircuit");

        ctx1 = new Inspiration.Context("boardArea");
        if (ctx1.getContext() != null) boardInit(ctx1);

        WEBGLInit("webgl");

        ctx2 = new Inspiration.Context("Monopoly");
        if (ctx2.getContext() != null) MonopolyInit(ctx2);
    </script>

    <p id="demo2"></p>
    <p id="demo"></p>
</@common.body>


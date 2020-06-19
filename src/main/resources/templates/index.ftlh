<#import "parts/projectPage.ftlh" as common>
<@common.head title = "Games">

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
    <script src="js/webSocket.js"></script>

    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="connect">Native WebSocket connection:</label>
                    <div id="webSocketConnect" class="btn btn-default" >Connect</div>
                    <button id="webSocketDisconnect" class="btn btn-default" type="submit">Disconnect</button>
                </div>

            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="webSocketMessage">What is your name?</label>
                    <input type="text" id="webSocketMessage" class="form-control" placeholder="Your message here...">
                </div>
                <button id="webSocketSend" class="btn btn-default" type="submit">Send</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="conversation" class="table">
                    <thead>
                        <tr>
                            <th>Messages</th>
                        </tr>
                    </thead>
                    <tbody id="webSocketMessages">
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    Little Circuit game:

    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
                    <div class="form-group">
                        <label for="connect">WebSocket connection:</label>
                        <button id="connect"  class="btn btn-default" type="submit">Connect</button>
                        <button id="disconnect"  class="btn btn-default" type="submit" disabled="disabled">Disconnect
                        </button>
                    </div>

            </div>
            <div class="col-md-6">

                    <div class="form-group">
                        <label for="name">What is your name?</label>
                        <input type="text" id="name" class="form-control" placeholder="Your name here...">
                    </div>
                    <button id="send"  class="btn btn-default" type="submit">Send</button>

            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="conversation" class="table table-striped">
                    <thead>
                    <tr>
                        <th>Greetings</th>
                    </tr>
                    </thead>
                    <tbody id="greetings">
                    </tbody>
                </table>
            </div>
        </div>
    </div>


    <div>
        <button name="Play" onclick="wantToPlay(ctx3);">PLAY</button>
        <p id="playInfo">0</p>
    </div>

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
    <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <script>
        ctx1 = new Inspiration.Context("boardArea");
        if (ctx1.getContext() != null) boardInit(ctx1);
        WEBGLInit("webgl");

        ctx2 = new Inspiration.Context("Monopoly");
        if (ctx2.getContext() != null) MonopolyInit(ctx2);

    </script>


    <p id="demo2"></p>
    <p id="demo"></p>
</@common.body>

